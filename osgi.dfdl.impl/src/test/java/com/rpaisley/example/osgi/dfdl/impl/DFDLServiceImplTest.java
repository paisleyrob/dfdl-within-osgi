package com.rpaisley.example.osgi.dfdl.impl;

import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class DFDLServiceImplTest {
	@Test
	public void constructor() {
		try {
			DFDLServiceImpl impl = new DFDLServiceImpl();
			LogService ls = new LogService() {

				@Override
				public void log(ServiceReference sr, int level, String message,
						Throwable exception) {
					log(level, message, exception);
				}

				@Override
				public void log(ServiceReference sr, int level, String message) {
					log(level, message);
				}

				@Override
				public void log(int level, String message, Throwable exception) {
					System.out.println(message);
					if (null != exception)
						exception.printStackTrace();
				}

				@Override
				public void log(int level, String message) {
					log(level, message, null);
				}
			};
			impl.setLogService(ls);
			impl.setup();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
