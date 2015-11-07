package com.kkbnart.wordis.game;

import java.util.HashSet;
import java.util.Set;

import android.view.MotionEvent;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.animation.AnimationManager;
import com.kkbnart.wordis.game.animation.BlockDeleteAnimation;
import com.kkbnart.wordis.game.animation.FreeFallAnimation;
import com.kkbnart.wordis.game.animation.GameAnimationFactory;
import com.kkbnart.wordis.game.animation.GameAnimationType;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.layout.LayoutDefinition;
import com.kkbnart.wordis.game.layout.ViewLayout;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.BlockIdFactory;
import com.kkbnart.wordis.game.object.block.BlockSetFactory;
import com.kkbnart.wordis.game.object.block.NextBlocks;
import com.kkbnart.wordis.game.object.block.OperatedBlocks;
import com.kkbnart.wordis.game.player.WordisPlayer;
import com.kkbnart.wordis.game.rule.Collision;
import com.kkbnart.wordis.game.rule.DeleteBlockLine;
import com.kkbnart.wordis.game.thread.GameThread;
import com.kkbnart.wordis.game.thread.GameThreadManager;

public class GameManager implements GameThreadManager {
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

	// Interface called on terminating game
	private IGameTerminate gameTerminate;
	
	// Game thread
	private GameThread gameThread = null;
	
	// Game type
	private GameType gameType;
	// Game status
	private GameState gameState;
	// Wordis player type
	private WordisPlayer player;
	
	// Preserve game statics while current game
	private CurrentGameStats stats;
	
	public GameManager(final IGameTerminate gameTerminate, final GameThread gameThread, final WordisPlayer player) {
		this.gameTerminate = gameTerminate;
		this.gameThread = gameThread;
		this.gameThread.addGameThreadManager(this);
		this.player = player;
		this.stats = new CurrentGameStats();
	}
	
	public void setGameSurfaceView(final GameSurfaceView gsv) {
		this.gsv = gsv;
	}
	
	public void setBlockSetFactory(final BlockSetFactory factory) {
		this.blockSetFactory = factory;
	}
	
	/**
	 * Set parameters to start game. <br>
	 * 
	 * @throws BlockCreateException Can not create blocks
	 * @throws NoAnimationException Can not add animation 
	 */
	public void startGame(final GameType type) throws BlockCreateException, NoAnimationException {
		gameType = type;
		
		// Set next blocks prepared to start
		next.setFactory(blockSetFactory);
		final GameTypeDefinition gtd = GameTypeDefinition.getInstance();
		next.initializeBlockSet(gtd.nextSize);
		operated.setBlocks(next.releaseNextBlocks());
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
	
	/**
	 * @see GameThreadManager#continueGame()
	 */
	@Override
	public boolean continueGame() {
		return gameState != GameState.GAMEFINISH;
	}
	
	/**
	 * @see GameThreadManager#invokeMainProcess()
	 */
	@Override
	public synchronized void invokeMainProcess(final long elapsedMSec) {
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
				updateFallBlocks();
				break;
			case DELETE:
				updateDeleteBlocks();
				break;
			case ANIMATION:
				updateAnimation();
				return;
			case RELEASE_NEXT:
				operated.setBlocks(next.releaseNextBlocks());
				gameState = GameState.CONTROL;
				break;
			default:
				// Do nothing
				return;
			}
			updateView();
		} catch (BlockCreateException | NoAnimationException e) {
			// TODO
			// Handle exception with showing some message and terminate game.
		}
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
	 * Update block fall. <br>
	 * 
	 * @throws BlockCreateException
	 * @throws NoAnimationException
	 */
	private void updateFallBlocks() throws BlockCreateException, NoAnimationException {
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
				operated.setBlocks(next.releaseNextBlocks());
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
		
		final String word = blockSetFactory.getWord();
		final int order = 0;
		final Set<Block> deletedBlocks = DeleteBlockLine.deleteWordLine(matrix, word, order);
		
		// Extract ids of them
		final Set<Integer> deletedBlockIds = new HashSet<Integer>();
		for (Block b : deletedBlocks) {
			deletedBlockIds.add(b.getId());
		}

		// Delete specified blocks from board
		board.deleteBlocks(deletedBlockIds);
		// Release ids of deleted blocks from id factory
		BlockIdFactory.getInstance().dissociateIds(deletedBlockIds);
		
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
	
	/**
	 * Update view animation and take game actions after the animation. <br>
	 */
	private void updateAnimation() {
		// FIXME
		// Divide process to update phase and draw phase,
		// and divide drawing board and drawing superimposed text
		if (animationManager.hasAnimation()) {
			gameState = gsv.drawAnimation(animationManager, board, next);
		}
	}
	
	/**
	 * Update view graphics. <br>
	 */
	private void updateView() {
		gsv.draw(board, operated, next, stats);
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
	 * @see GameThreadManager#finishGame()
	 */
	@Override
	public void finishGame() {
		switch (gameType) {
		case TEST:
		case PRACTICE:
		case SINGLE:
			// FIXME
			gameTerminate.terminateSingle(stats);
			break;
		case VS_CPU:
			// TODO
			break;
		case MULTI_NET:
			// TODO
			break;
		}
	}
	
	/**
	 * To forcefully finish game from outside, interrupt thread. <br>
	 */
	public void interruptGame() {
		gameThread.initThread();
	}
	
	public void suspendGame() {
		gameState = GameState.PAUSE;
	}
	
	public void resumeGame() {
		gameState = GameState.NONE;
	}
	
	public GameType getGameType() {
		return gameType;
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