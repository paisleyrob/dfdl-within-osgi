package com.rpaisley.example.osgi.xml.impl;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.osgi.service.log.LogService;

import com.rpaisley.example.osgi.thread.ThreadService;

public class XMLServiceImpl extends ThreadService {
	private volatile LogService logService;

	@Override
	protected void setup() {
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " starting");
		try {
			ClassLoader original = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(XMLServiceImpl.class.getClassLoader());
				SchemaFactory factory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				logService.log(LogService.LOG_DEBUG,
						"Lookup of schema: " + XMLConstants.W3C_XML_SCHEMA_NS_URI
								+ " yielded factory: " + factory.getClass().getName());
			} finally {
				Thread.currentThread().setContextClassLoader(original);
			}
		} catch (Throwable t) {
			logService.log(LogService.LOG_DEBUG, "Log me stuff");
		}
	}

	@Override
	protected void process() {
		try {
			logService.log(LogService.LOG_DEBUG, "Nothing left to do, waiting()");
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}
}