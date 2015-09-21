package com.kkbnart.wordis.game.util;

/**
 * Specify the directions in this game. <br>
 * If you want to add a direction (ex. for new button), add definition in this class.
 * 
 * @author kkbnart
 */
public enum Direction {
	LEFT (0, -1, 0),
	RIGHT (1, 1, 0),
	UP (2, 0, -1),
	DOWN (3, 0, 1);
	
	private int id;
	// Specify the direction in the game field
	private int sampleX;
	private int sampleY;
	
	private Direction(final int id, final int x, final int y) {
		this.id = id;
		this.sampleX = x;
		this.sampleY = y;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSampleX() {
		return sampleX;
	}
	
	public int getSampleY() {
		return sampleY;
	}
}
