package com.kkbnart.wordis.game.rule;

/**
 * Control fall of blocks
 * @author kkbnart
 * singleton instance
 */
public class Fall {
	// Singleton instance
	private static Fall instance = null;
	// Speed to move per [msec]
	private float xPerMSec = 0.f;
	private float yPerMSec = 0.002f;
	// Acceleration per [msec]
	private float axPerMSec = 0.f;
	private float ayPerMSec = 0.002f;

	/**
	 * Get instance of this class
	 * @return instance of FreeFall
	 */
	public static Fall getInstance() {
		if (instance == null) {
			instance = new Fall();
		}
		return instance;
	}
	
	/**
	 * Get speed to move x per milli second. <br>
	 * 
	 * @return x speed on each frame
	 */
	public float getXPerMSec() {
		return xPerMSec;
	}
	
	/**
	 * Get speed to move y per milli second. <br>
	 * 
	 * @return y speed on each frame
	 */
	public float getYPerMSec() {
		return yPerMSec;
	}
	
	/**
	 * Get acceleration x per milli second. <br>
	 * 
	 * @return x acceleration per milli second
	 */
	public float getAXPerMSec() {
		return axPerMSec;
	}
	
	/**
	 * Get acceleration y per milli second. <br>
	 * 
	 * @return y acceleration per milli second
	 */
	public float getAYPerMSec() {
		return ayPerMSec;
	}
	
	/**
	 * Set falling move per milli second. <br>
	 * 
	 * @param xPerMSec	Move x per milli second.
	 * @param yPerMSec	Move y per milli second.
	 */
	public void setFallPerMSec(final float xPerMSec, final float yPerMSec) {
		this.xPerMSec = xPerMSec;
		this.yPerMSec = yPerMSec;
	}
	
	/**
	 * Set acceleration per milli second on free fall. <br>
	 * 
	 * @param axPerMSec	X acceleration per milli second. 
	 * @param ayPerMSec	Y acceleration per milli second.
	 */
	public void setAccellPerMSec(final float axPerMSec, final float ayPerMSec) {
		this.axPerMSec = axPerMSec;
		this.ayPerMSec = ayPerMSec;
	}
}
