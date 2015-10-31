package com.kkbnart.wordis.game.object.block;

import android.graphics.Bitmap;

public class BlockColor {	
	private int color;
	private Bitmap bmd;
	public BlockColor(final int color, final Bitmap bmd) {
		this.color = color;
		this.bmd = bmd;
	}
	protected int getColor() {
		return color;
	}
	protected Bitmap getImage() {
		return bmd;
	}
}
