package com.kkbnart.wordis.game;

import java.util.Set;

import android.app.Activity;

import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.board.NextBlocks;
import com.kkbnart.wordis.game.board.OperatedBlocks;
import com.kkbnart.wordis.game.exception.BlockCreateException;
import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockColorSet;
import com.kkbnart.wordis.game.object.BlockIdFactory;
import com.kkbnart.wordis.game.object.BlockSetFactory;
import com.kkbnart.wordis.game.object.CharacterSet;
import com.kkbnart.wordis.game.object.Collision;
import com.kkbnart.wordis.game.object.PatternDefinition;
import com.kkbnart.wordis.game.rule.DeleteBlockLine;

public class GameManager implements Runnable {
	// private static final String TAG = GameManager.class.getSimpleName();
	
	// Game board
	private Board board;
	// Operated block
	private OperatedBlocks operated;
	// Next block
	private NextBlocks next;
	// GameSurfaceView
	private GameSurfaceView gsv = null;
	// Factory to create new set of block
	private BlockSetFactory blockSetFactory;
	
	// Sleep time [ms]
	private static final long SLEEP = 50;
	
	/**
	 * Constructor with specifying game settings. <br>
	 * 
	 * @param parent Activity class
	 */
	public GameManager(final Activity parent, final GameSurfaceView gsv) {
		// FIXME
		// Test
		board = new Board(/* x= */10, /* y= */10, /* w= */200, /* h= */400,
				/* row= */12, /* col= */6, /* collisionX= */0, /* collisionY= */-3,
				/* collisionRow= */ 15, /* collisionCol= */6);
		operated = new OperatedBlocks(2, -2);
		BlockColorSet bcs = new BlockColorSet(parent.getResources());
		CharacterSet cs = new CharacterSet(parent);
		blockSetFactory = new BlockSetFactory(bcs, cs);
		//
		{
			blockSetFactory.registerBlockPatterns(PatternDefinition.BAR, 1.0);
			blockSetFactory.registerBlockPatterns(PatternDefinition.L, 0.5);
			blockSetFactory.registerBlockPatterns(PatternDefinition.INV_L, 0.5);
			blockSetFactory.registerBlockPatterns(PatternDefinition.Z, 0.5);
			blockSetFactory.registerBlockPatterns(PatternDefinition.INV_Z, 0.5);
			blockSetFactory.registerBlockPatterns(PatternDefinition.BUMP, 1.0);
			blockSetFactory.registerCharacterPattern("TEST");
		}
		//
		next = new NextBlocks(9, 1, 0, 4, blockSetFactory);
		
		this.gsv = gsv;
	}
	
	/**
	 * Set parameters to start game. <br>
	 * 
	 * @throws BlockCreateException Can not create blocks
	 */
	public void startGame() throws BlockCreateException {
		// FIXME
		// Test
		next.initializeBlockSet(2);
		operated.setBlocks(next.releaseNextBlocks());
		Thread gameThread = new Thread(this);
		gameThread.start();
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
			
			try {
				updateBlocks();
			} catch (BlockCreateException e) {
				// TODO
				// Handle exception with showing some message and terminate game.
			}
			updateView();
		}
		
		// TODO
		// End of the game
	}
	
	private boolean continueGame() {
		// FIXME
		return true;
	}
	
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
	
	private void deleteBlocks() {
		final Block[][] matrix = board.getMatrixedBlocks();
		
		final String word = blockSetFactory.getWord();
		final int order = 0;
		Set<Integer> deletedIds = DeleteBlockLine.deleteWordLine(matrix, word, order);
		// Delete ids of blocks
		board.deleteBlocks(deletedIds);
		// Release ids of them in block id factory
		BlockIdFactory.getInstance().dissociateIds(deletedIds);
	}
	
	private void updateView() {
		gsv.draw(board, operated, next);
	}
	
	/* Block operation */
	public void moveBlock(final int dx, final int dy) {
		operated.operate(board, dx, dy);
	}
	public void rotateBlock(final boolean clockWise) {
		operated.operate(board, clockWise);
	}
}