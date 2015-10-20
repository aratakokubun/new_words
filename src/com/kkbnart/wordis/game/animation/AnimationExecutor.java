package com.kkbnart.wordis.game.animation;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;

import com.kkbnart.wordis.game.GameStatus;
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
	public void assignAnimation(final GameAnimation animation) {
		animations.add(animation);
	}
	
	public void deleteAnimation(final GameAnimation animation) {
		animations.remove(animation);
	}

	/**
	 * Execute preserved animations. <br>
	 * Remove animations time of which is over. <br>
	 * 
	 * @param canvas	Surface view canvas
	 * @param board		Current board
	 * @return action	Game action to be taken after animation
	 */
	public GameStatus execute(final Canvas canvas, final Board board) {
		// Action to be taken after animation (default None)
		GameStatus action = GameStatus.NONE;
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
