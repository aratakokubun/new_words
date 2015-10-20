package com.kkbnart.wordis.game;

import android.view.MotionEvent;

public interface IGameActivity {
	/**
	 * Change parameters depending on surface size change. <br>
	 * 
	 * @param width		View surface size x
	 * @param height	View surface size y
	 */
	public void surfaceSizeChanged(final int width, final int height);
	
	/**
	 * On view touched. <br>
	 * 
	 * @param event Touch event
	 */
	public void onSurfaceTouched(final MotionEvent event);
}
