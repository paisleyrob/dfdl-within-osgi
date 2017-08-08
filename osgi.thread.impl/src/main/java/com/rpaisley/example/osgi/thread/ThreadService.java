package com.rpaisley.example.osgi.thread;

public abstract class ThreadService implements Runnable {
	private boolean started = false;
	private boolean stopped = false;
	private boolean shutdown = false;
	private Thread thread = null;

	protected void setup() {
	}

	public synchronized void start() throws InterruptedException {
		thread = new Thread(null, this, this.getClass().getSimpleName());
		thread.start();
		while (!started) {
			wait();
		}
	}

	public synchronized void stop() throws InterruptedException {
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
}