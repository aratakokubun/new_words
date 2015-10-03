package com.kkbnart.wordis.game.animation;

public enum GameAnimationType {
	GAME_OVER (0),
	;
	
	private int id;
	private GameAnimationType(final int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
