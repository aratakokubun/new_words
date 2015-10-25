package com.kkbnart.wordis.game.animation;

import android.graphics.Canvas;

import com.kkbnart.wordis.game.GameStatus;
import com.kkbnart.wordis.game.board.Board;

/**
 * Base class for game animation. <br>
 * This class define animation time and behavior on execute. <br>
 * Concrete animation should be defined in extended class. <br>
 * 
 * @author kkbnart
 */
public abstract class GameAnimation {
	// Flag if this animation is enabled or not
	private boolean enabled = false;
	// Animation milli time length
	private long animationTime;
	// Animation start milli time
	private long startTime;
	// Previous animation time
	private long previousTime;
	// Action after this animation
	private GameStatus postAction;
	
	public GameAnimation(final long animationTime, final GameStatus priorAction) {
		this.animationTime = animationTime;
		this.postAction = priorAction;
	}
	
	public void setAnimationTime(final long animationTime) {
		this.animationTime = animationTime;
	}
	
	public long getAnimationTime() {
		return animationTime;
	}
	
	public void setPriorAction(final GameStatus priorAction) {
		this.postAction = priorAction;
	}
	
	public GameStatus getPostAction() {
		return postAction;
	}
	
	public long getPreviousTime() {
		return previousTime;
	}
	
	/**
	 * Get if animation time is over. <br>
	 * 
	 * @return	true  : Continue animation <br>
	 * 			false : Animation time is over <br>
	 */
	private boolean isAnimationContinue() {
		final long elapsedTime = System.currentTimeMillis() - startTime;
		if (elapsedTime > animationTime) {
			enabled = false;
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Update animation defined in the class. <br>
	 * 
	 * @param canvas	Surface view canvas
	 * @param board		Current board
	 * @return	true  : Continue animation <br>
	 * 			false : Animation time is over <br>
	 */
	public boolean executeAnimationUpdate(final Canvas canvas, final Board board) {
		final long now = System.currentTimeMillis();
		// If not executed, raise flag and set start time
		if (!enabled) {
			setAnimationStart(now);
		}
		final long elapsedTime = now - startTime;
		final long diffTime = now-previousTime;
		
		drawAnimation(canvas, board, elapsedTime, diffTime);
		previousTime = now;
		
		return isAnimationContinue();
	}
	
	/**
	 * Set animation start true and start time. <br>
	 * {@link super#setAnimationStart(long)} has to be called {@code @override} method. <br>
	 * 
	 * @param now Current time
	 */
	protected void setAnimationStart(final long now) {
		enabled = true;
		startTime = now;
		previousTime = now;
	}
	
	/**
	 * Draw animation on {@code canvas}. <br>
	 * 
	 * @param canvas		Surface view canvas
	 * @param board			Current board
	 * @param elapsedTime	Elapsed time from animation start
	 * @param diffTime		Elapsed time from previous animation time
	 */
	protected abstract void drawAnimation(final Canvas canvas, final Board board, final long elapsedTime, final long diffTime);
}
