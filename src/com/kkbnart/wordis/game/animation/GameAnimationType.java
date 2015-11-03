package com.kkbnart.wordis.game.animation;

public enum GameAnimationType {
	GAME_START (0),
	GAME_OVER (1),
	BLOCK_DELETE(2),
	BLOCK_FALL (3);
	
	private int id;
	private GameAnimationType(final int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
