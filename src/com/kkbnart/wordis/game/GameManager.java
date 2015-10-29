package com.kkbnart.wordis.game;

import java.util.Set;

import android.view.MotionEvent;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.animation.AnimationManager;
import com.kkbnart.wordis.game.animation.GameAnimationType;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockIdFactory;
import com.kkbnart.wordis.game.object.BlockSetFactory;
import com.kkbnart.wordis.game.object.Collision;
import com.kkbnart.wordis.game.object.NextBlocks;
import com.kkbnart.wordis.game.object.OperatedBlocks;
import com.kkbnart.wordis.game.player.PlayerStatus;
import com.kkbnart.wordis.game.player.PlayerStatusMap;
import com.kkbnart.wordis.game.player.WordisPlayer;
import com.kkbnart.wordis.game.rule.DeleteBlockLine;

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
	// Players' status
	private PlayerStatusMap playerStatusMap = new PlayerStatusMap();
	
	// Interface called on terminating game
	IGameTerminate gameTerminate;
	
	// Game thread
	GameThread gameThread = null;
	
	// Game type
	private GameType gameType;
	// Game status
	private GameStatus gameStatus;
	// Wordis player type
	private WordisPlayer player;
	
	public GameManager(IGameTerminate gameTerminate, final WordisPlayer player) {
		this.gameTerminate = gameTerminate;
		this.gameThread = new GameThread(this);
		this.player = WordisPlayer.MY_PLAYER;
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
		// Set next blocks prepared to start
		next.setFactory(blockSetFactory);
		final GameTypeDefinition gtd = GameTypeDefinition.getInstance();
		next.initializeBlockSet(gtd.nextSize);
		operated.setBlocks(next.releaseNextBlocks());

		gameType = type;
		gameStatus = GameStatus.NONE;
		
		// TODO
		// Initialize player status
		final PlayerStatus status = new PlayerStatus(0, "temp", 0, 0, 0, 0, 0, 0);
		playerStatusMap.put(player, status);
		
		// Refresh board
		board.clearBlocks();

		// Set start animation
		animationManager.addAnimation(GameAnimationType.GAME_START);
		
		// Start game thread
		gameThread.startThread();
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
		
		// Create animation manager
		if (animationManager == null) {
			animationManager = new AnimationManager(player, gtd.boardCol, gtd.boardRow, w, h);
		} else {
			animationManager.onSurfaceChange(player, gtd.boardCol, gtd.boardRow, w, h);
		}
	}
	
	/**
	 * @see GameThreadManager#continueGame()
	 */
	public boolean continueGame() {
		return gameStatus != GameStatus.GAMEFINISH;
	}
	
	/**
	 * @see GameThreadManager#invokeMainProcess()
	 */
	public synchronized void invokeMainProcess(final long elapsedMSec) {
		if (!checkIsNull() && gameStatus != GameStatus.PAUSE) {
			if (animationManager.hasAnimation()) {
				updateAnimation();
			} else {
				// Do update and draw while not animation
				try {
					updateBlocks(elapsedMSec);
				} catch (BlockCreateException | NoAnimationException e) {
					// TODO
					// Handle exception with showing some message and terminate game.
				}
				updateView();
			}
		}
	}
	
	/**
	 * @see GameThreadManager#finishGame()
	 */
	public void finishGame() {
		switch (gameType) {
		case TEST:
		case PRACTICE:
		case SINGLE:
			final PlayerStatus myStatus = playerStatusMap.get(WordisPlayer.MY_PLAYER);
			gameTerminate.terminateSingle(myStatus);
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
	 * Update view animation and take game actions after the animation. <br>
	 */
	private void updateAnimation() {
		gameStatus = gsv.drawAnimation(animationManager, board);
	}
	
	/**
	 * Update state of blocks in board. <br>
	 * 
	 * @throws BlockCreateException Can not create new block set
	 * @throws NoAnimationException Specified animation is not registered 
	 */
	private void updateBlocks(final long elapsedMSec) throws BlockCreateException, NoAnimationException  {
		if (Collision.isCollided(board, operated)) {
			// TODO
			// If versus remote player, send gameover message to server
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
			operated.autoUpdate(elapsedMSec);
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
	 * To forcefully finish game from outside, interrupt thread. <br>
	 */
	public void interruptGame() {
		gameThread.initThread();
	}
	
	public void suspendGame() {
		gameStatus = GameStatus.PAUSE;
	}
	
	public void resumeGame() {
		gameStatus = GameStatus.NONE;
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