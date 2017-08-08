package com.rpaisley.example.osgi.thread;

import java.util.Dictionary;

import org.apache.felix.dm.Component;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public abstract class ThreadService implements ManagedService, Runnable {
	private boolean started = false;
	private boolean stopped = false;
	private boolean shutdown = false;
	private Thread thread = null;

	protected void setup() {
	}

	public synchronized void start(@SuppressWarnings("unused") Component component)
			throws InterruptedException {
		thread = new Thread(null, this, this.getClass().getSimpleName());
		thread.start();
		while (!started) {
			wait();
		}
	}

	public synchronized void stop(@SuppressWarnings("unused") Component component)
			throws InterruptedException {
		shutdown = true;
		thread.interrupt();
		while (!stopped) {
			wait();
		}
		thread.join();
	}

	protected abstract void process();

	@Override
	public void run() {
		synchronized (this) {
			started = true;
			notify();
		}
		try {
			setup();
			while (!shutdown) {
				process();
			}
		} finally {
			synchronized (this) {
				stopped = true;
				notifyAll();
			}
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
	}
}