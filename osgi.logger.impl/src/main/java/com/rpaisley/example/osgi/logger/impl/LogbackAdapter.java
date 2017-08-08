package com.rpaisley.example.osgi.logger.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackAdapter implements LogListener {
	private Map<Long, Logger> loggers = new HashMap<>();

	@Override
	public void logged(LogEntry entry) {
		if (null == entry.getBundle() || null == entry.getBundle().getSymbolicName()) {
			return;
		}

		Logger l = loggers.get(entry.getBundle().getBundleId());
		if (null == l) {
			l = LoggerFactory.getLogger(entry.getBundle().getSymbolicName());
			loggers.put(entry.getBundle().getBundleId(), l);
		}

		if (null != entry.getException()) {
			switch (entry.getLevel()) {
			case LogService.LOG_DEBUG:
				l.debug(entry.getMessage(), entry.getException());
				break;

			case LogService.LOG_INFO:
				l.info(entry.getMessage(), entry.getException());
				break;

			case LogService.LOG_WARNING:
				l.warn(entry.getMessage(), entry.getException());
				break;

			case LogService.LOG_ERROR:
			default:
				l.error(entry.getMessage(), entry.getException());
				break;

			}
		} else {
			switch (entry.getLevel()) {
			case LogService.LOG_DEBUG:
				l.debug(entry.getMessage());
				break;

			case LogService.LOG_INFO:
				l.info(entry.getMessage());
				break;

			case LogService.LOG_WARNING:
				l.warn(entry.getMessage());
				break;

			case LogService.LOG_ERROR:
			default:
				l.error(entry.getMessage());
				break;

			}
		}
	}
}
