package com.kkbnart.wordis.game.thread;

import java.util.HashSet;

import android.os.Handler;
import android.os.Message;

public class GameThread implements Runnable {
	private HashSet<ThreadListenerCallback> listeners = new HashSet<ThreadListenerCallback>();
	
	private Thread thread = null;
	
	// Sleep time [ms]
	private static final long SLEEP = 25;
	
	public void addCallbackListener(final ThreadListenerCallback manager) {
		listeners.add(manager);
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
		while (continueGame()) {
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
			
			invokeMainProcess(elapsedTime);
		}
		
		handleFinishGame();
	}
	
	private boolean continueGame() {
		for (ThreadListenerCallback manager : listeners) {
			if (!manager.continueGame()) {
				return false;
			}
		}
		return true;
	}
	
	private void invokeMainProcess(final long elapsedTime) {
		for (ThreadListenerCallback manager : listeners) {
			manager.invokeMainProcess(elapsedTime);			
		}
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
				finishGame();
				return true;
			}
			return false;
		}
	});
	
	private void finishGame() {
		for (ThreadListenerCallback manager : listeners) {
			manager.finishGame();
		}
	}
}
