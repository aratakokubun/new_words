package com.kkbnart.wordis.game.object.color;

import android.graphics.Color;

import com.kkbnart.wordis.R;

public enum ColorDefinition {
	RED		(0, Color.RED, 						R.drawable.block_red),
	BLUE	(1, Color.BLUE, 					R.drawable.block_blue),
	GREEN	(2, Color.GREEN, 					R.drawable.block_green),
	ORANGE	(3, Color.argb(255, 255, 165,   0), R.drawable.block_orange),
	PURPLE	(4, Color.argb(255, 144,   0, 144), R.drawable.block_purple),
	YELLOW	(5, Color.argb(255, 240, 240,   0), R.drawable.block_yellow),
	WHITE	(6, Color.argb(255, 255, 255, 255), R.drawable.block_white),
	GRAY	(7, Color.argb(255, 128, 128, 128), R.drawable.block_gray), ;
	
	private int id;
	private int color;
	private int imageId;
	private ColorDefinition(final int id, final int color, final int imageId) {
		this.id = id;
		this.color = color;
		this.imageId = imageId;
	}
	public int getId() {
		return id;
	}
	public int getColor() {
		return color;
	}
	public int getImageId() {
		return imageId;
	}
}
