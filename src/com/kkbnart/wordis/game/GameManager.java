package com.kkbnart.wordis.game;

import java.util.Set;

import android.app.Activity;
import android.view.MotionEvent;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.LoadPropertyException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.animation.AnimationManager;
import com.kkbnart.wordis.game.animation.GameAnimationType;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.board.NextBlocks;
import com.kkbnart.wordis.game.board.OperatedBlocks;
import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockColorSet;
import com.kkbnart.wordis.game.object.BlockIdFactory;
import com.kkbnart.wordis.game.object.BlockSetFactory;
import com.kkbnart.wordis.game.object.CharacterSet;
import com.kkbnart.wordis.game.object.Collision;
import com.kkbnart.wordis.game.rule.DeleteBlockLine;

public class GameManager implements Runnable {
	// Game board
	private Board board = null;
	// Operated block
	private OperatedBlocks operated = null;
	// Next block
	private NextBlocks next = null;
	// GameSurfaceView
	private GameSurfaceView gsv = null;
	// Factory to create new set of block
	private BlockSetFactory blockSetFactory = null;
	// Manage and execute animations
	private AnimationManager animationManager = null;
	// Flag to continue game
	private boolean continueGame = true;
	
	// Sleep time [ms]
	private static final long SLEEP = 30;
	
	public void setGameSurfaceView(final GameSurfaceView gsv) {
		this.gsv = gsv;
	}
	
	/**
	 * Set parameters to start game. <br>
	 * 
	 * @throws BlockCreateException Can not create blocks
	 */
	public void startGame() throws BlockCreateException {
		// Set next prepared to start
		next.setFactory(blockSetFactory);
		final GameTypeDefinition gtd = GameTypeDefinition.getInstance();
		next.initializeBlockSet(gtd.nextSize);
		operated.setBlocks(next.releaseNextBlocks());
		
		continueGame = true;
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
	
	/**
	 * Change view size. <br>
	 * 
	 * @param activity	Game Activity
	 * @param width		View width
	 * @param height	View height
	 * @throws BlockCreateException			Can not create new set of blocks
	 * @throws InvalidParameterException 	Invalid parameters are specified
	 * @throws NoAnimationException 
	 */
	public void surfaceSizeChanged(final Activity activity, final int width, final int height) throws BlockCreateException, InvalidParameterException, NoAnimationException {
		final GameTypeDefinition gtd = GameTypeDefinition.getInstance();
		
		// Change board size depends on view size
		final int x = (int)(gtd.boardXRate * width);
		final int y = (int)(gtd.boardYRate * height);
		final int w = (int)(gtd.boardWRate * width);
		final int h = (int)(gtd.boardHRate * height);
		if (board == null) {
			board = new Board(x, y, w, h, gtd.boardCol, gtd.boardRow,
					gtd.boardCollisionX, gtd.boardCollisionY, gtd.boardCollisionCol, gtd.boardCollisionRow);
		} else {
			board.updateBoardArea(x, y, w, h);
			board.updateBoardSize(gtd.boardCol, gtd.boardRow,
					gtd.boardCollisionX, gtd.boardCollisionY, gtd.boardCollisionCol, gtd.boardCollisionRow);
		}
		
		// Create operated block set
		if (operated == null) {
			operated = new OperatedBlocks(gtd.operatedX, gtd.operatedY);
		} else {
			operated.setRoot(gtd.operatedX, gtd.operatedY);
		}
		
		// Create next block set
		if (next == null) {
			next = new NextBlocks(gtd.nextX, gtd.nextY, gtd.nextMarginX, gtd.nextMarginY);
		} else {
			next.setCoordinate(gtd.nextX, gtd.nextY, gtd.nextMarginX, gtd.nextMarginY);
		}
		
		// FIXME
		// Create animation manager
		if (animationManager == null) {
			animationManager = new AnimationManager(WordisPlayer.PLAYER1, gtd.boardCol, gtd.boardRow, w, h);
		} else {
			animationManager.onSurfaceChange(WordisPlayer.PLAYER1, gtd.boardCol, gtd.boardRow, w, h);
		}
	}
	
	/**
	 * Create block set factory depends on {@code word}. <br>
	 * 
	 * @param activity	Activity to get resources
	 * @throws BlockCreateException  Can not create new block
	 * @throws LoadPropertyException Can not read property file
	 */
	public void createBlockSetFactory(final Activity activity, final String property, final String word) throws BlockCreateException, LoadPropertyException {
		if (blockSetFactory == null) {
			BlockColorSet bcs = new BlockColorSet(activity.getResources());
			CharacterSet cs = new CharacterSet(activity.getResources());
			blockSetFactory = new BlockSetFactory(bcs, cs);
			blockSetFactory.readJson(property, activity);
		}
		blockSetFactory.registerCharacterPattern(word);
	}
	
	@Override
	public void run() {
		final long sleepTime = 10;
		long prevTime = System.currentTimeMillis();
		while (continueGame()) {
			// Wait for SLEEP [ms]
			long elapsedTime = System.currentTimeMillis() - prevTime;
			while (elapsedTime < SLEEP) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO
					// Handle exception
				}
				elapsedTime = System.currentTimeMillis() - prevTime;
			}
			// Update previous time
			prevTime = System.currentTimeMillis();
			
			invokeMainProcess();
		}
		
		// TODO
		// End of the game
	}
	
	/**
	 * Judge if continue game. <br>
	 * 
	 * @return true  : continue game <br>
	 * 		   false : terminate game <br>
	 */
	private boolean continueGame() {
		return continueGame;
	}
	
	/**
	 * Periodical process while game
	 * Synchronized because update is conflicted with user operation.
	 */
	private synchronized void invokeMainProcess() {
		if (!checkIsNull()) {
			if (animationManager.hasAnimation()) {
				updateAnimation();
			} else {
				// Do update and draw while not animation
				try {
					updateBlocks();
				} catch (BlockCreateException | NoAnimationException e) {
					// TODO
					// Handle exception with showing some message and terminate game.
				}
				updateView();
			}
		}
	}
	
	/**
	 * Update view animation and take game actions after the animation. <br>
	 */
	private void updateAnimation() {
		GameAction action = gsv.drawAnimation(animationManager, board);
		switch (action) {
		case GAMEFINISH:
			continueGame = false;
			break;
		case PAUSE:
			// TODO
			break;
		default:
			break;
		}
	}
	
	/**
	 * Update state of blocks in board. <br>
	 * 
	 * @throws BlockCreateException Can not create new block set
	 * @throws NoAnimationException Specified animation is not registered 
	 */
	private void updateBlocks() throws BlockCreateException, NoAnimationException  {
		if (Collision.isCollided(board, operated)) {
			// If operated block is collided to walls or other blocks, game over!!
			animationManager.addAnimation(GameAnimationType.GAME_OVER);
		} else if (Collision.isContacted(board, operated)) {
			// If contacted to walls or other blocks, stable the operated blocks and set next
			board.addBlockSet(operated);
			operated.setBlocks(next.releaseNextBlocks());
			// TODO
			// Delete lines
			deleteBlocks();
			// TODO
			// Set animation
			// Create animation class and raise flags in that class
		} else {
			// Automatically update block
			operated.autoUpdate();
		}
	}
	
	/**
	 * Delete blocks in board. <br>
	 */
	private void deleteBlocks() {
		final Block[][] matrix = board.getMatrixedBlocks();
		
		final String word = blockSetFactory.getWord();
		final int order = 0;
		Set<Integer> deletedIds = DeleteBlockLine.deleteWordLine(matrix, word, order);
		// Delete specified blocks from board
		board.deleteBlocks(deletedIds);
		// Release ids of deleted blocks from id factory
		BlockIdFactory.getInstance().dissociateIds(deletedIds);
	}
	
	/**
	 * Update view graphics. <br>
	 */
	private void updateView() {
		gsv.draw(board, operated, next);
	}
	
	/**
	 * Check if class members are initialized. <br>
	 * 
	 * @return true : Some members are null. <br>
	 * 		   false: All members are not null. <br>
	 */
	private boolean checkIsNull() {
		return board == null || operated == null || next == null || gsv == null
				|| blockSetFactory == null || animationManager == null;
	}

	/**
	 * Move blocks. <br>
	 * Block operations are valid while not animation. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param dx	x direction move
	 * @param dy	y direction move
	 */
	public synchronized void moveBlock(final float dx, final float dy) {
		if (!animationManager.hasAnimation()) {
			operated.operate(board, dx, dy);
		}
	}
	
	/**
	 * Rotate blocks. <br>
	 * Block operations are valid while not animation. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param clockWise	Is clockwise rotation or not
	 */
	public synchronized void rotateBlock(final boolean clockWise) {
		if (!animationManager.hasAnimation()) {
			operated.operate(board, clockWise);
		}
	}
	
	/**
	 * On surface touched. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param event Touch event
	 */
	public synchronized void onSurfaceTouched(MotionEvent event) {
		animationManager.onSurfaceTouched(event);
	}
}