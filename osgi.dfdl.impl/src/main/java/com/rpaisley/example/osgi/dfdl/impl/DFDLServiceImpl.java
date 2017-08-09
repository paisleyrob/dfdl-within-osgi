package com.rpaisley.example.osgi.dfdl.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.osgi.service.log.LogService;

import com.rpaisley.example.osgi.thread.DefaultThreadService;

import edu.illinois.ncsa.daffodil.japi.Compiler;
import edu.illinois.ncsa.daffodil.japi.Daffodil;
import edu.illinois.ncsa.daffodil.japi.DataProcessor;
import edu.illinois.ncsa.daffodil.japi.Diagnostic;
import edu.illinois.ncsa.daffodil.japi.ParseResult;
import edu.illinois.ncsa.daffodil.japi.ProcessorFactory;
import edu.illinois.ncsa.daffodil.japi.UnparseResult;
import edu.illinois.ncsa.daffodil.japi.infoset.JDOMInfosetInputter;
import edu.illinois.ncsa.daffodil.japi.infoset.JDOMInfosetOutputter;

public class DFDLServiceImpl extends DefaultThreadService {
	private static final String SCHEMA = "/LV.dfdl.xsd";
	private final Compiler compiler;
	private final Map<String, DataProcessor> processors = new HashMap<>();
	private volatile LogService logService;

	public DFDLServiceImpl() {
		compiler = Daffodil.compiler();
		compiler.setValidateDFDLSchemas(true);
	}

	@Override
	protected LogService getLogService() {
		return logService;
	}

	protected void setLogService(LogService logService) {
		this.logService = logService;
	}

	@Override
	protected void setup() {
		debug("Setting up");
		try (InputStream is = DFDLServiceImpl.class.getResourceAsStream(SCHEMA)) {
			File temp = File.createTempFile("DFDLServiceImpl", "xsd");
			temp.deleteOnExit();
			Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			try {
				ClassLoader original = Thread.currentThread().getContextClassLoader();
				debug("Original classloader: " + original);
				System.out.println("Original classloader: " + original);
				try {
					Thread.currentThread()
							.setContextClassLoader(DFDLServiceImpl.class.getClassLoader());
					ProcessorFactory pf = compiler.compileFile(temp);
					if (!pf.isError()) {
						debug("Compiled schema: " + SCHEMA + " without error");
						DataProcessor processor = pf.onPath("/");
						processors.put(SCHEMA, processor);
					} else {
						for (Diagnostic d : pf.getDiagnostics()) {
							debug("DIAG: " + d.getMessage());
						}
					}
				} finally {
					Thread.currentThread().setContextClassLoader(original);
				}
			} catch (Throwable t) {
				debug("Failed to compile schema: " + SCHEMA, t);
			}
		} catch (IOException e) {
			debug("Failed to create temp file", e);
		}

		try (InputStream is = DFDLServiceImpl.class.getResourceAsStream("/example.xml")) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(is);
			JDOMInfosetInputter infoset = new JDOMInfosetInputter(doc);
			DataProcessor processor = processors.get(SCHEMA);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			UnparseResult uResult = processor.unparse(infoset, Channels.newChannel(baos));
			if (uResult.isError()) {
				for (Diagnostic d : uResult.getDiagnostics()) {
					warn(d.getMessage());
				}
			} else {
				debug("BAOS: " + baos.size() + " bytes: " + baos.toString());
			}

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			JDOMInfosetOutputter outDoc = new JDOMInfosetOutputter();
			ParseResult pResult = processor.parse(Channels.newChannel(bais), outDoc);
			if (pResult.isError()) {
				for (Diagnostic d : uResult.getDiagnostics()) {
					warn(d.getMessage());
				}
			} else {
				debug("outDoc: " + outDoc);
				Document d = outDoc.getResult();
				XMLOutputter outputter = new XMLOutputter();
				debug(outputter.outputString(d));
			}
		} catch (Throwable e) {
			debug("Failed to load example.xml", e);
		}
	}
}
