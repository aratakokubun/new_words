package com.kkbnart.wordis.game.animation;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.GameStatus;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.player.WordisPlayer;

/**
 * <p>
 * This class manages animation flags. <br>
 * Judge the priority of flags and set them to queue to execute. <br>
 * </p>
 * 
 * @author kkbnart
 */
public class AnimationManager {
	// Manage and execute animations
	private AnimationExecutor animationExecutor = null;
	// Factory to create game animations
	GameAnimationFactory factory = null;
	
	public AnimationManager(final WordisPlayer player, final int col, final int row, final int width, final int height) throws NoAnimationException {
		animationExecutor = new AnimationExecutor();
		factory = new GameAnimationFactory(player, col, row, width, height);
	}
	
	public void onSurfaceChange(final WordisPlayer player, final int col, final int row, final int width, final int height) throws NoAnimationException {
		factory.updateAnimationSize(player, col, row, width, height);
	}
	
	public boolean hasAnimation() {
		return animationExecutor.hasAnimation();
	}
	
	/**
	 * Execute animation and return game action to be taken after animation. <br>
	 * 
	 * @param canvas	Surface view canvas
	 * @param board		Current Board
	 * @return	Game action
	 */
	public GameStatus executeAnimation(final Canvas canvas, final Board board) {
		return animationExecutor.execute(canvas, board);
	}
	
	/**
	 * Add specified animation. <br>
	 * 
	 * @param type Animation Type to be added
	 * @throws NoAnimationException Specified animation type is not defined in factory.
	 */
	public void addAnimation(final GameAnimationType type) throws NoAnimationException {
		animationExecutor.assignAnimation(factory.create(type));
	}
	
	/**
	 * On surface touched while animation. <br>
	 * 
	 * @param event Touch event
	 */
	public void onSurfaceTouched(MotionEvent event) {
		// TODO
	}
}
