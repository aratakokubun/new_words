package com.kkbnart.wordis.game.rule;


/**
 * Control free fall of blocks
 * @author kkbnart
 * singleton instance
 */
public class FreeFall {
	// Singleton instance
	private static FreeFall instance = null;
	// Speed to move on each frame
	private float xPerFrame = 0.f;
	private float yPerFrame = .05f;

	/**
	 * Get instance of this class
	 * @return instance of FreeFall
	 */
	public static FreeFall getInstance() {
		if (instance == null) {
			instance = new FreeFall();
		}
		return instance;
	}
	
	/**
	 * Get speed to move x on each frame
	 * @return x speed on each frame
	 */
	public float getXPerFrame() {
		return xPerFrame;
	}
	
	/**
	 * Get speed to move y on each frame
	 * @return y speed on each frame
	 */
	public float getYPerFrame() {
		return yPerFrame;
	}
	
	/**
	 * Set speed to move x on each frame
	 * @param xPerFrame
	 */
	public void setXPerFrame(final float xPerFrame) {
		this.xPerFrame = xPerFrame;
	}
	
	/**
	 * Set speed to move y on each frame
	 * @param yPerFrame
	 */
	public void setYPerFrame(final float yPerFrame) {
		this.yPerFrame = yPerFrame;
	}
}
