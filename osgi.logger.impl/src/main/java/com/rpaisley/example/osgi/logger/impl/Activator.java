package com.rpaisley.example.osgi.logger.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogReaderService;

public class Activator implements BundleActivator, ServiceListener {
	private final LogbackAdapter logbackLogger = new LogbackAdapter();
	private final List<LogReaderService> readers = new LinkedList<>();
	private BundleContext context;

	@Override
	public void serviceChanged(ServiceEvent event) {
	}

	@Override
	public void start(BundleContext ctx) throws Exception {
		this.context = ctx;
		String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
		try {
			context.addServiceListener(this, filter);

			ServiceReference<?>[] refs = context
					.getServiceReferences(LogReaderService.class.getName(), null);
			if (null != refs) {
				for (int i = 0; i < refs.length; i += 1) {
					LogReaderService lrs = (LogReaderService) context.getService(refs[i]);
					if (null != lrs) {
						readers.add(lrs);
						lrs.addLogListener(logbackLogger);
					}
				}
			}
		} catch (InvalidSyntaxException e) {
			System.err.println("Unable to add service with filter: " + filter);
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		for (Iterator<LogReaderService> i = readers.iterator(); i.hasNext();) {
			i.next().removeLogListener(logbackLogger);
			i.remove();
		}
	}
}
