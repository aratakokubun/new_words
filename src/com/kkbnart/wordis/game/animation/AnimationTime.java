package com.kkbnart.wordis.game.animation;

public class AnimationTime {
	// Start time milli second
	public int start;
	// End time milli second
	public int end;
	
	public AnimationTime(final int start, final int end) {
		this.start = start;
		this.end = end;
	}
	
	public int getLength() {
		return end - start;
	}
}
