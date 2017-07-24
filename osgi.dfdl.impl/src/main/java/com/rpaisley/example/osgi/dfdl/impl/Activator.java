package com.rpaisley.example.osgi.dfdl.impl;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent().setImplementation(DFDLServiceImpl.class).setInterface(
				new String[] { DFDLService.class.getName(), ManagedService.class.getName() },
				null));
	}
}
