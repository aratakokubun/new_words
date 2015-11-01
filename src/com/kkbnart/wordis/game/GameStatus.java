package com.kkbnart.wordis.game;

public enum GameStatus {
	NONE (0),
	CONTROL(1),
	DELETE(2),
	ANIMATION (3),
	RELEASE_NEXT(4),
	GAMEFINISH (5),
	PAUSE (6),
	;
	
	private int priority;
	private GameStatus(final int priority) {
		this.priority = priority;
	}
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Compare two game type and return one which has more priority value. <br>
	 * 
	 * @return Prior game action
	 */
	public GameStatus compare(final GameStatus other) {
		if (this.priority > other.priority) {
			return this;
		} else {
			return other;
		}
	}
}
