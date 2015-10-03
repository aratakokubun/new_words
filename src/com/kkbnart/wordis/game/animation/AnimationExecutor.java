package com.kkbnart.wordis.game.animation;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;

import com.kkbnart.wordis.game.GameAction;
import com.kkbnart.wordis.game.board.Board;

/**
 * Execute assigned animations. <br>
 * 
 * @author kkbnart
 */
public class AnimationExecutor {
	// Current animations to execute
	private Set<GameAnimation> animations = new HashSet<GameAnimation>();
	
	/**
	 * Assign new animation to {@link AnimationExecutor#animations}. <br>
	 * 
	 * @param animation New animation
	 */
	public void assignAnimation(GameAnimation animation) {
		this.animations.add(animation);
	}

	/**
	 * Execute preserved animations. <br>
	 * Remove animations time of which is over. <br>
	 * 
	 * @param canvas	Surface view canvas
	 * @param board		Current board
	 * @return action	Game action to be taken after animation
	 */
	public GameAction execute(final Canvas canvas, final Board board) {
		// Action to be taken after animation (default None)
		GameAction action = GameAction.NONE;
		Set<GameAnimation> removeAnimations = new HashSet<GameAnimation>();
		for (GameAnimation animation : animations) {
			if (!animation.executeAnimationUpdate(canvas, board)) {
				// Update action
				action = action.compare(animation.getPostAction());
				removeAnimations.add(animation);
			}
		}
		// Remove animations
		for (GameAnimation animation : removeAnimations) {
			animations.remove(animation);
		}
		return action;
	}
	
	public boolean hasAnimation() {
		return !animations.isEmpty();
	}
}
