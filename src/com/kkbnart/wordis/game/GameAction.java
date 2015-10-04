package com.kkbnart.wordis.game;

public enum GameAction {
	NONE (0),
	GAMEFINISH (3),
	PAUSE (2),
	ANIMATION (1);
	
	private int priority;
	private GameAction(final int priority) {
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
	public GameAction compare(final GameAction other) {
		if (this.priority > other.priority) {
			return this;
		} else {
			return other;
		}
	}
}
