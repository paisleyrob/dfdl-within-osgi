package com.rpaisley.example.osgi.xml.impl;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent().setImplementation(XMLServiceImpl.class)
				.add(createServiceDependency().setService(LogService.class).setRequired(true)));
	}
}
