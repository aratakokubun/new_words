package com.kkbnart.wordis.game.animation;

import java.util.HashMap;

import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.GameStatus;
import com.kkbnart.wordis.game.player.WordisPlayer;

/**
 * Factory to create specific game animation
 * @author kkbnart
 */
public class GameAnimationFactory {
	// Game animation pattern
	private HashMap<GameAnimationType, GameAnimation> animations = new HashMap<GameAnimationType, GameAnimation>();
	
	public GameAnimationFactory(final WordisPlayer player, final int col, final int row, final int width, final int height) throws NoAnimationException {
		updateAnimationSize(player, col, row, width, height);
	}
	
	/**
	 * Register newly created size of game animation. <br>
	 * 
	 * @param col		Column of the board
	 * @param row		Row of the board
	 * @param width		Pixel width of the board 
	 * @param height	Pixel height of the board
	 * @throws NoAnimationException No game animation is defined in the factory
	 */
	public void updateAnimationSize(final WordisPlayer player, final int col, final int row, final int width, final int height) throws NoAnimationException {
		// Register animations to factory
		setAnimation(GameAnimationType.GAME_OVER, new GameOverAnimation(
				/* animation time= */5000, GameStatus.GAMEFINISH, col));
		setAnimation(GameAnimationType.GAME_START, new GameStartAnimation(
				/* animation time= */3000, GameStatus.NONE));
		setAnimation(GameAnimationType.BLOCK_FALL, new FreeFallAnimation(
				/* animation time= */0, GameStatus.NONE));
	}
	
	/**
	 * Register game animation. <br>
	 * 
	 * @param type		Type of the game animation
	 * @param animation	Game animation
	 */
	public void setAnimation(final GameAnimationType type, final GameAnimation animation) throws NoAnimationException {
		animations.put(type, animation);
	}
	
	public GameAnimation create(final GameAnimationType type) {
		return animations.get(type);
	}
}
