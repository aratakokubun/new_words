package com.kkbnart.wordis.game.animation;

public class BlockFallSpeed {
	// Block id
	private int id;

	// Fall x enabled
	private boolean fallXEnabled;
	// x speed
	private float fallXSpeed = 0.f;
	
	// Fall y enabled
	private boolean fallYEnabled;
	// y speed
	private float fallYSpeed = 0.f;
	
	public BlockFallSpeed(final int id, final boolean xEnabled, final boolean yEnabled) {
		this.id = id;
		this.fallXEnabled = xEnabled;
		this.fallYEnabled = yEnabled;
	}
	
	public int getId() {
		return id;
	}
	
	public void setFallXEnabled(final boolean xEnabled) {
		this.fallXEnabled = xEnabled;
		this.fallXSpeed = 0.f;
	}
	
	public void setFallXSpeed(final float xSpeed) {
		this.fallXSpeed = xSpeed;
	}
	
	public boolean getFallXEnabled() {
		return fallXEnabled;
	}
	
	public float getFallXSpeed() {
		return fallXSpeed;
	}
	
	public void setFallYEnabled(final boolean yEnabled) {
		this.fallYEnabled = yEnabled;
		this.fallYSpeed = 0.f;
	}
	
	public void setFallYSpeed(final float ySpeed) {
		this.fallYSpeed = ySpeed;
	}
	
	public boolean getFallYEnabled() {
		return fallYEnabled;
	}
	
	public float getFallYSpeed() {
		return fallYSpeed;
	}
	
	public void updateFallSpeed(final float dvx, final float dvy) {
		if (fallXEnabled) {
			fallXSpeed += dvx;
		}
		if (fallYEnabled) {
			fallYSpeed += dvy;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BlockFallSpeed) {
			return id == ((BlockFallSpeed)other).id;
		}
		return false;
	}
}
