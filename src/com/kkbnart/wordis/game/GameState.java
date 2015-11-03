package com.kkbnart.wordis.game;

public enum GameState {
	GAMEFINISH (0),
	PAUSE (1),
	ANIMATION (2),
	DELETE(3),
	FALL(4),
	RELEASE_NEXT(5),
	CONTROL(6),
	NONE (7),
	;
	
	private int priority;
	private GameState(final int priority) {
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
	public GameState compare(final GameState other) {
		if (this.priority < other.priority) {
			return this;
		} else {
			return other;
		}
	}
}
