package com.rpaisley.example.osgi.dfdl.impl;

import java.net.URL;

import edu.illinois.ncsa.daffodil.japi.Compiler;
import edu.illinois.ncsa.daffodil.japi.Daffodil;

public class DFDLServiceImpl implements DFDLService {
	private static final String SCHEMA = "/LV.dfdl.xsd";
	private final Compiler compiler;

	public DFDLServiceImpl() {
		compiler = Daffodil.compiler();
		compiler.setValidateDFDLSchemas(true);

		lookupClass("org.apache.xerces.jaxp.validation.XMLSchemaFactory");

		try {
			URL url = DFDLServiceImpl.class.getResource(SCHEMA);
			if (null != url) {
				System.out.println("URI for resource: " + url.toURI());
				compiler.compileSource(url.toURI());
				System.out.println("Compiled schema");
			}
		} catch (Throwable t) {
			System.out.println("Failed to compile schema: " + SCHEMA);
			t.printStackTrace();
		}
	}

	private void lookupClass(String name) {
		for (ClassLoader cl : new ClassLoader[] { ClassLoader.getSystemClassLoader(),
				this.getClass().getClassLoader() }) {
			try {
				Class.forName(name, true, cl);
				System.out.println(
						"Found class: " + name + " with loader: " + cl.getClass().getName());
			} catch (Throwable t) {
				System.out.println("Unable to locate class: " + name + " with loader: "
						+ cl.getClass().getName());
			}
		}
	}
}
