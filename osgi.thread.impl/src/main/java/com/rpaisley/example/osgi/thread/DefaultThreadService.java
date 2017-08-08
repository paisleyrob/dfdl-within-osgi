package com.rpaisley.example.osgi.thread;

import org.osgi.service.log.LogService;

public abstract class DefaultThreadService extends ThreadService {
	@Override
	protected void process() {
		try {
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}

	protected abstract LogService getLogService();

	public void debug(String message, Throwable throwable) {
		getLogService().log(LogService.LOG_DEBUG, message, throwable);
	}

	public void debug(String message) {
		getLogService().log(LogService.LOG_DEBUG, message);
	}

	public void warn(String message, Throwable throwable) {
		getLogService().log(LogService.LOG_WARNING, message, throwable);
	}

	public void warn(String message) {
		getLogService().log(LogService.LOG_WARNING, message);
	}

	public void info(String message, Throwable throwable) {
		getLogService().log(LogService.LOG_INFO, message, throwable);
	}

	public void info(String message) {
		getLogService().log(LogService.LOG_INFO, message);
	}

	public void error(String message, Throwable throwable) {
		getLogService().log(LogService.LOG_ERROR, message, throwable);
	}

	public void error(String message) {
		getLogService().log(LogService.LOG_ERROR, message);
	}
}
