package com.kkbnart.wordis.game.manager;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.GameState;
import com.kkbnart.wordis.game.animation.AnimationManager;
import com.kkbnart.wordis.game.animation.BlockDeleteAnimation;
import com.kkbnart.wordis.game.animation.FreeFallAnimation;
import com.kkbnart.wordis.game.animation.GameAnimationFactory;
import com.kkbnart.wordis.game.animation.GameAnimationType;
import com.kkbnart.wordis.game.layout.LayoutDefinition;
import com.kkbnart.wordis.game.layout.ViewLayout;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.BlockSetBuffer;
import com.kkbnart.wordis.game.object.block.NextBlocks;
import com.kkbnart.wordis.game.object.block.OperatedBlocks;
import com.kkbnart.wordis.game.object.board.Board;
import com.kkbnart.wordis.game.player.WordisPlayer;
import com.kkbnart.wordis.game.rule.Collision;
import com.kkbnart.wordis.game.rule.DeleteBlockLine;

/**
 * Manage game block action (move, delete, etc..) <br>
 * 
 * @author kkbnart
 */
public class GameBoardManager {
	// Game board
	private Board board = null;
	// Operated block
	private OperatedBlocks operated = null;
	// Next block
	private NextBlocks next = null;
	// Factory to create new set of block
	private BlockSetBuffer blockSetBuffer = null;
	// Manage and execute animations
	private AnimationManager animationManager = null;
	// Wordis player type
	private WordisPlayer player;

	private String word;
	
	// Game status
	private GameState gameState;
	// Temporary game status while pause
	private GameState temporaryStateWhilePause;
	// Preserve game statics while current game
	private CurrentGameStats stats = new CurrentGameStats();
	
	public GameBoardManager(final WordisPlayer player, final String word) {
		this.player = player;
		this.word = word;
	}
	
	public void setBlockSetBuffer(final BlockSetBuffer buffer) {
		buffer.registerClient(player);
		this.blockSetBuffer = buffer;
	}
	
	/**
	 * Set parameters to start game. <br>
	 * 
	 * @throws BlockCreateException Can not create blocks
	 * @throws NoAnimationException Can not add animation 
	 */
	public void startGame() throws BlockCreateException, NoAnimationException {
		// Set next blocks prepared to start
		next.setBlockSetBuffer(blockSetBuffer);
		next.initializeBlockSet(player, ViewLayout.getInstance().nextSize);
		operated.setBlocks(next.releaseNextBlocks(player));
		board.clearBlocks();
		stats.clearStats();

		// Set start animation
		animationManager.addAnimation(GameAnimationType.GAME_START);
		gameState = GameState.ANIMATION;
	}
	
	/**
	 * Change view size. <br>
	 * 
	 * @param width		View width
	 * @param height	View height
	 * @throws BlockCreateException			Can not create new set of blocks
	 * @throws InvalidParameterException 	Invalid parameters are specified
	 * @throws NoAnimationException 
	 */
	public void surfaceSizeChanged(final int width, final int height) throws BlockCreateException, InvalidParameterException, NoAnimationException {
		final ViewLayout vl = ViewLayout.getInstance();
		final LayoutDefinition ld = vl.getLayoutDefinition(player.getSide());
		
		// Change board size depends on view size
		final int x = (int)(ld.boardXRate * width);
		final int y = (int)(ld.boardYRate * height);
		final int w = (int)(vl.boardWRate * width);
		final int h = (int)(vl.boardHRate * height);
		
		if (board == null) {
			board = new Board(x, y, w, h, vl.boardCol, vl.boardRow, vl.boardCollisionX, vl.boardCollisionY,
					vl.boardCollisionCol, vl.boardCollisionRow, vl.boardStackCellX, vl.boardStackCellY);
		} else {
			board.updateBoardArea(x, y, w, h);
			board.updateBoardSize(vl.boardCol, vl.boardRow, vl.boardCollisionX, vl.boardCollisionY,
					vl.boardCollisionCol, vl.boardCollisionRow, vl.boardStackCellX, vl.boardStackCellY);
		}
		
		// Create operated block set
		if (operated == null) {
			operated = new OperatedBlocks(vl.operatedX, vl.operatedY);
		} else {
			operated.setRoot(vl.operatedX, vl.operatedY);
		}
		
		// Create next block set
		if (next == null) {
			next = new NextBlocks(ld.nextX, ld.nextY, vl.nextMarginX, vl.nextMarginY);
		} else {
			next.setCoordinate(ld.nextX, ld.nextY, vl.nextMarginX, vl.nextMarginY);
		}
		
		// Create animation manager
		if (animationManager == null) {
			animationManager = new AnimationManager(player, vl.boardCol, vl.boardRow, w, h);
		} else {
			animationManager.onSurfaceChange(player, vl.boardCol, vl.boardRow, w, h);
		}
	}
	
	/* ---------------------------------- */
	public void updateBoardObjects(final long elapsedMSec) {
		// If game is not ready, skip process
		if (checkIsNull()) {
			return;
		}
				
		// Process according to game status
		try {
			switch (gameState) {
			case CONTROL:
				updateOperatedBlocks(elapsedMSec);
				break;
			case FALL:
				updateFallingBlocks();
				break;
			case DELETE:
				updateDeleteBlocks();
				break;
			case ANIMATION:
				updateAnimation();
				return;
			default:
				// Do nothing
				return;
			}
		} catch (BlockCreateException | NoAnimationException e) {
			// TODO
			// Handle exception with showing some message and terminate game.
		}
	}
	
	public boolean checkIsNull() {
		// TODO
		return false;
	}
	
	/**
	 * Update blocks while control by the player. <br>
	 * 
	 * @param elapsedTime	Elapsed time from previous frame
	 */
	private void updateOperatedBlocks(final long elapsedMSec) {
		if (Collision.isContacted(board, operated)) {
			// If contacted to walls or other blocks, stable the operated blocks and set next
			board.addBlockSet(operated);
			gameState = GameState.FALL;
		} else {
			// Automatically update block
			operated.autoUpdate(elapsedMSec);
		}
	}
	
	/**
	 * Update block falling. <br>
	 * 
	 * @throws BlockCreateException
	 * @throws NoAnimationException
	 */
	private void updateFallingBlocks() throws BlockCreateException, NoAnimationException {
		// Set fall animation
		GameAnimationFactory factory = animationManager.getAnimationFactory();
		FreeFallAnimation animation = (FreeFallAnimation)factory.create(GameAnimationType.BLOCK_FALL);
		factory = null;
		animation.setFallingBlocks(board);
		animationManager.addAnimation(animation);
		gameState = GameState.ANIMATION;
	}
	
	/**
	 * Update block delete. <br>
	 * 
	 * @throws BlockCreateException
	 * @throws NoAnimationException
	 */
	private void updateDeleteBlocks() throws BlockCreateException, NoAnimationException {
		// Delete blocks
		final Set<Block> deletedBlocks = deleteBlocks();
		
		// If delete or fall occur, update animation
		// Else, shift to "game over" or "release next"
		if (deletedBlocks.isEmpty()) {
			if (isGameOver()) {
				// TODO
				// If versus remote player, send game over message to server
				// If operated block is collided to walls or other blocks, game over!!
				animationManager.addAnimation(GameAnimationType.GAME_OVER);
				gameState = GameState.ANIMATION;
			} else {
				operated.setBlocks(next.releaseNextBlocks(player));
				stats.endChain();
				gameState = GameState.CONTROL;
			}
		} else {
			// Set delete animation
			GameAnimationFactory factory = animationManager.getAnimationFactory();
			BlockDeleteAnimation animation = (BlockDeleteAnimation)factory.create(GameAnimationType.BLOCK_DELETE);
			animation.setDeleteBlockIds(deletedBlocks);
			animationManager.addAnimation(animation);
			stats.updateScore(deletedBlocks.size());
			gameState = GameState.ANIMATION;
		}
	}
	
	/**
	 * Delete blocks in board. <br>
	 * 
	 * @return Deleted block ids
	 */
	private Set<Block> deleteBlocks() {
		final Block[][] matrix = board.getMatrixedBlocks();
		
		final int order = 0;
		final Set<Block> deletedBlocks = DeleteBlockLine.deleteWordLine(matrix, word, order);
		
		// Extract ids of them
		final Set<Integer> deletedBlockIds = new HashSet<Integer>();
		for (Block b : deletedBlocks) {
			deletedBlockIds.add(b.getId());
		}

		// Delete specified blocks from board
		board.deleteBlocks(deletedBlockIds);
		// Release ids of deleted blocks from id buffer
		blockSetBuffer.releaseIds(player, deletedBlockIds);
		
		return deletedBlocks;
	}
	
	/**
	 * Judge if game is over. <br>
	 * @return true:	game is over <br>
	 * 		   false:	game is not over <br>	
	 */
	private boolean isGameOver() {
		return board.getIsBoardStacked();
	}
	
	private void updateAnimation() {
		// TODO
	}

	/**
	 * Draw objects and animations. <br>
	 * 
	 * @param canvas
	 * @param paint
	 */
	public void draw(final Canvas canvas, final Paint paint) {
		if (animationManager.hasAnimation()) {
			gameState = animationManager.executeAnimation(canvas, board, next);
		} else {
			board.draw(canvas, paint);
			operated.draw(canvas, paint, board);
			next.draw(canvas, paint, board);
		}
	}
	
	/* ---------------------------------- */
	public boolean continueGame() {
		return gameState != GameState.FINISH;
	}
	
	// TODO
	// Check deep copy
	public void suspendGame() {
		temporaryStateWhilePause = gameState;
		gameState = GameState.PAUSE;
	}
	
	public void resumeGame() {
		gameState = temporaryStateWhilePause;
	}
	
	public CurrentGameStats getGameStats() {
		return stats;
	}
	
	/* ---------------------------------- */
	public synchronized void moveBlock(final float dx, final float dy) {
		if (!animationManager.hasAnimation()) {
			operated.operate(board, dx, dy);
		}
	}
	
	public synchronized void rotateBlock(final boolean clockWise) {
		if (!animationManager.hasAnimation()) {
			operated.operate(board, clockWise);
		}
	}
}
