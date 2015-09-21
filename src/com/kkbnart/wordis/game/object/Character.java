package com.kkbnart.wordis.game.object;

import android.graphics.Bitmap;

public class Character {
	private String letter;
	private Bitmap bitmap;
	
	public Character(final String letter, final Bitmap bitmap) {
		this.letter = letter;
		this.bitmap = bitmap;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public Bitmap getImage() {
		return bitmap;
	}
}
