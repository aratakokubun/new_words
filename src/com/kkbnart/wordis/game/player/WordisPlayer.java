package com.kkbnart.wordis.game.player;

public enum WordisPlayer {
	MY_PLAYER (0),
	OPP_PLAYER (1),
	COM (1),
	;
	
	private int side;
	private WordisPlayer(final int side) {
		this.side = side;
	}
	
	public int getSide() {
		return side;
	}
}
