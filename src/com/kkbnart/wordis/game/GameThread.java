package com.kkbnart.wordis.game;

public class GameThread implements Runnable {
	private GameThreadManager manager;
	
	private Thread thread = null;
	
	// Sleep time [ms]
	private static final long SLEEP = 30;
	
	public GameThread(final GameThreadManager manager) {
		super();
		this.manager = manager;
	}
	
	public void startThread() {
		// Start game thread
		if (thread == null) {
			thread = new Thread(this);
		}
		if (!thread.isAlive()) {
			thread.start();
		}
	}
	
	public void initThread() {
		thread.interrupt();
		thread = null;
	}

	@Override
	public void run() {
		final long sleepTime = 10;
		long prevTime = System.currentTimeMillis();
		while (manager.continueGame()) {
			// Wait for SLEEP [ms]
			long elapsedTime = System.currentTimeMillis() - prevTime;
			while (elapsedTime < SLEEP) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					manager.finishGame();
					return;
				}
				elapsedTime = System.currentTimeMillis() - prevTime;
			}
			// Update previous time
			prevTime = System.currentTimeMillis();
			
			manager.invokeMainProcess();
		}
		
		manager.finishGame();
	}
}
