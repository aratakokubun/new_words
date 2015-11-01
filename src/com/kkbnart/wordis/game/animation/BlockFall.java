package com.kkbnart.wordis.game.animation;

public class BlockFall {
	// Block id
	private int id;
	
	// Fall x enabled
	private boolean xEnabled = false;
	// x speed
	private float xSpeed = 0.f;
	// x target to reach end of the fall
	private float xTarget = 0.f;
	// Fall y enabled
	private boolean yEnabled = false;
	// y speed
	private float ySpeed = 0.f;
	// y target to reach end of the fall
	private float yTarget = 0.f;
	
	public BlockFall(final int id) {
		this.id = id;
	}
	
	public void setXEnabled(final boolean xEnabled) {
		this.xEnabled = xEnabled;
	}
	
	public boolean getXEnabled() {
		return xEnabled;
	}
	
	public float getXSpeed() {
		return xSpeed;
	}
	
	public float getXTarget() {
		return xTarget;
	}
	
	public void setXFall(final float speed, final float target) {
		this.xEnabled = true;
		this.xSpeed = speed;
		this.xTarget = target;
	}
	
	public void setYEnabled(final boolean yEnabled) {
		this.yEnabled = yEnabled;
	}
	
	public boolean getYEnabled() {
		return yEnabled;
	}
	
	public float getYSpeed() {
		return ySpeed;
	}
	
	public float getYTarget() {
		return yTarget;
	}
	
	public void setYFall(final float speed, final float target) {
		this.yEnabled = true;
		this.ySpeed = speed;
		this.yTarget = target;
	}
	
	public void updateFallSpeed(final float xSpeed, final float ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BlockFall) {
			return id == ((BlockFall)other).id;
		}
		return false;
	}
}
