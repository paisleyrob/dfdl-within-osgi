package com.rpaisley.example.osgi.dfdl.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import edu.illinois.ncsa.daffodil.japi.Compiler;
import edu.illinois.ncsa.daffodil.japi.Daffodil;
import edu.illinois.ncsa.daffodil.japi.Diagnostic;
import edu.illinois.ncsa.daffodil.japi.ProcessorFactory;

public class DFDLServiceImpl implements DFDLService {
	private static final String SCHEMA = "/LV.dfdl.xsd";
	private final Compiler compiler;

	public DFDLServiceImpl() {
		compiler = Daffodil.compiler();
		compiler.setValidateDFDLSchemas(true);

		try (InputStream is = DFDLServiceImpl.class.getResourceAsStream(SCHEMA)) {
			File temp = File.createTempFile("DFDLServiceImpl", "xsd");
			temp.deleteOnExit();
			Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			try {
				ClassLoader original = Thread.currentThread().getContextClassLoader();
				try {
					Thread.currentThread()
							.setContextClassLoader(DFDLServiceImpl.class.getClassLoader());
					ProcessorFactory pf = compiler.compileFile(temp);
					System.out.println("Compiled schema");
					System.out.println("Processor factory error? " + pf.isError());
					if (pf.isError()) {
						for (Diagnostic d : pf.getDiagnostics()) {
							System.out.println("DIAG: " + d.getMessage());
						}
					}
				} finally {
					Thread.currentThread().setContextClassLoader(original);
				}
			} catch (Throwable t) {
				System.out.println("Failed to compile schema: " + SCHEMA);
				t.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
