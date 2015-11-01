package com.kkbnart.wordis.game.animation;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;

import com.kkbnart.wordis.game.GameState;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.NextBlocks;

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
	 * @param canvas		Surface view canvas
	 * @param board			Current board
	 * @param nextBlocks	Next block set
	 * @return action	Game action to be taken after animation
	 */
	public GameState execute(final Canvas canvas, final Board board, final NextBlocks nextBlocks) {
		// Action to be taken after animation (default None)
		GameState action = GameState.NONE;
		Set<GameAnimation> removeAnimations = new HashSet<GameAnimation>();
		for (GameAnimation animation : animations) {
			if (!animation.executeAnimationUpdate(canvas, board, nextBlocks)) {
				// Override action
				action = action.compare(animation.getPostAction());
				removeAnimations.add(animation);
			}
		}
		// Remove animations
		for (GameAnimation animation : removeAnimations) {
			animations.remove(animation);
		}
		
		// Return action if state is not NONE
		// Return ANIMATION if NONE
		if (action == GameState.NONE) {
			return GameState.ANIMATION;
		} else {
			return action;
		}
	}
	
	public boolean hasAnimation() {
		return !animations.isEmpty();
	}
}
