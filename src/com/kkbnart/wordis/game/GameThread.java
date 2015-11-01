package com.kkbnart.wordis.game;

import android.os.Handler;
import android.os.Message;

public class GameThread implements Runnable {
	private GameThreadManager manager;
	
	private Thread thread = null;
	
	// Sleep time [ms]
	private static final long SLEEP = 25;
	
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
		final long sleepTime = 5;
		long prevTime = System.currentTimeMillis();
		while (manager.continueGame()) {
			// Wait for SLEEP [ms]
			long elapsedTime = System.currentTimeMillis() - prevTime;
			while (elapsedTime < SLEEP) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					handleFinishGame();
					return;
				}
				elapsedTime = System.currentTimeMillis() - prevTime;
			}
			// Update previous time
			prevTime = System.currentTimeMillis();
			
			manager.invokeMainProcess(elapsedTime);
		}
		
		handleFinishGame();
	}
	
	public void handleFinishGame() {
		handler.sendEmptyMessage(FINISH_GAME);
		thread = null;
	}
	
	// Handler to inform action from game loop thread
	private static final int FINISH_GAME = 0;
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case FINISH_GAME:
				manager.finishGame();
				return true;
			}
			return false;
		}
	});
}
