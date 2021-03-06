package com.rpaisley.example.osgi.xml.impl;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.osgi.service.log.LogService;

import com.rpaisley.example.osgi.thread.DefaultThreadService;

public class XMLServiceImpl extends DefaultThreadService {
	private volatile LogService logService;

	@Override
	protected LogService getLogService() {
		return logService;
	}

	@Override
	protected void setup() {
		debug(this.getClass().getName() + " starting");
		try {
			ClassLoader original = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(XMLServiceImpl.class.getClassLoader());
				SchemaFactory factory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				debug("Lookup of schema: " + XMLConstants.W3C_XML_SCHEMA_NS_URI
						+ " yielded factory: " + factory.getClass().getName());
			} finally {
				Thread.currentThread().setContextClassLoader(original);
			}
		} catch (Throwable t) {
			debug("Log me stuff");
		}
	}
}