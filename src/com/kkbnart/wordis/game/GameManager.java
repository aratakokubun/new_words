package com.kkbnart.wordis.game;

import java.util.Set;

import android.app.Activity;

import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.board.NextBlocks;
import com.kkbnart.wordis.game.board.OperatedBlocks;
import com.kkbnart.wordis.game.exception.BlockCreateException;
import com.kkbnart.wordis.game.exception.InvalidParameterException;
import com.kkbnart.wordis.game.exception.LoadPropertyException;
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
	
	// Sleep time [ms]
	private static final long SLEEP = 50;
	
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
	 */
	public void surfaceSizeChanged(final Activity activity, final int width, final int height) throws BlockCreateException, InvalidParameterException {
		final GameTypeDefinition gtd = GameTypeDefinition.getInstance();
		
		// Change board size depends on view size
		final int x = (int)(gtd.boardXRate * width);
		final int y = (int)(gtd.boardYRate * height);
		final int w = (int)(gtd.boardWRate * width);
		final int h = (int)(gtd.boardHRate * height);
		if (board == null) {
			board = new Board(x, y, w, h, gtd.boardRow, gtd.boardCol,
					gtd.boardCollisionX, gtd.boardCollisionY, gtd.boardCollisionRow, gtd.boardCollisionCol);
		} else {
			board.updateBoardArea(x, y, w, h);
			board.updateBoardSize(gtd.boardRow, gtd.boardCol,
					gtd.boardCollisionX, gtd.boardCollisionY, gtd.boardCollisionRow, gtd.boardCollisionCol);
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
	}
	
	/**
	 * Create block set factory depends on {@code word}. <br>
	 * 
	 * @param activity	Activity to get resources
	 * @throws BlockCreateException  Can not create new block
	 * @throws LoadPropertyException Can not read property file
	 */
	public void createBlockSetFactory(final Activity activity, final String word) throws BlockCreateException, LoadPropertyException {
		if (blockSetFactory == null) {
			BlockColorSet bcs = new BlockColorSet(activity.getResources());
			CharacterSet cs = new CharacterSet(activity.getResources());
			blockSetFactory = new BlockSetFactory(bcs, cs);
			blockSetFactory.readJson("normal", activity);
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

			// Do update and draw if all class variables are initialized
			if (!checkIsNull()) {
				try {
					updateBlocks();
				} catch (BlockCreateException e) {
					// TODO
					// Handle exception with showing some message and terminate game.
				}
				updateView();
			}
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
		// FIXME
		return true;
	}
	
	/**
	 * Update state of blocks in board. <br>
	 * 
	 * @throws BlockCreateException Can not create new block set
	 */
	private void updateBlocks() throws BlockCreateException {
		// If contacted to walls or other blocks, stable the operated blocks and set next
		if (Collision.isContacted(board, operated)) {
			board.addBlockSet(operated);
			operated.setBlocks(next.releaseNextBlocks());
			// TODO
			// Delete lines
			deleteBlocks();
			// TODO
			// Set animation
			// Create animation class and raise flags in that class
		} else {
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
		return board == null || operated == null || next == null || gsv == null || blockSetFactory == null;
	}

	// Block operations
	public void moveBlock(final float dx, final float dy) {
		operated.operate(board, dx, dy);
	}
	public void rotateBlock(final boolean clockWise) {
		operated.operate(board, clockWise);
	}
}