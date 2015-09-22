package com.kkbnart.wordis.game.util;

/**
 * Specify the directions in this game. <br>
 * If you want to add a direction (ex. for new button), add definition in this class.
 * 
 * @author kkbnart
 */
public enum Direction {
	LEFT (0, -1, 0, "left"),
	RIGHT (1, 1, 0, "right"),
	UP (2, 0, -1, "up"),
	DOWN (3, 0, 1, "down");
	
	private int id;
	// Specify the direction in the game field
	private int sampleX;
	private int sampleY;
	// Direction name
	private String name;
	
	private Direction(final int id, final int x, final int y, final String name) {
		this.id = id;
		this.sampleX = x;
		this.sampleY = y;
		this.name = name;
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
	
	public String getName() {
		return name;
	}
}
