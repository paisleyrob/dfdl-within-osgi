package com.rpaisley.example.osgi.xml.impl;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

public class XMLServiceImpl {
	public XMLServiceImpl() {
		System.out.println(this.getClass().getName() + " starting");
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			System.out.println("Lookup of schema: " + XMLConstants.W3C_XML_SCHEMA_NS_URI
					+ " yielded factory: " + factory.getClass().getName());
		} catch (Throwable t) {
			System.out.println("Failed to lookup schema: " + t);

		}
	}
}