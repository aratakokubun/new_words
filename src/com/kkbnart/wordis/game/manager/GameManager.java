package com.kkbnart.wordis.game.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.view.MotionEvent;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.GameSurfaceView;
import com.kkbnart.wordis.game.GameType;
import com.kkbnart.wordis.game.object.block.BlockSetBuffer;
import com.kkbnart.wordis.game.object.block.BlockSetFactory;
import com.kkbnart.wordis.game.player.WordisPlayer;
import com.kkbnart.wordis.game.thread.GameThread;
import com.kkbnart.wordis.game.thread.ThreadListenerCallback;

/**
 * Manage game process <br>
 * 
 * @author kkbnart
 */
public class GameManager implements ThreadListenerCallback {
	// Game manager to proceed everything except control with user interface
	private Map<WordisPlayer, GameBoardManager> boardManagers = new HashMap<WordisPlayer, GameBoardManager>();
	// GameSurfaceView
	private GameSurfaceView gsv = null;
	// Game thread
	private GameThread gameThread = null;
	// Interface called on terminating game
	private IGameTerminate gameTerminate;
	
	// Game type
	private GameType gameType;
	// FIXME
	// Use common class to preserve objective word 
	// Objective word
	private String word;
	
	private WordisPlayer loser = WordisPlayer.NONE;
	
	public GameManager(final IGameTerminate gameTerminate, final GameType gameType, final String word, 
			final Set<WordisPlayer> wordisPlayers) throws InvalidParameterException {
		this.gameTerminate = gameTerminate;
		this.gameType = gameType;
		this.word = word;
		// Add game board manager for each player
		for (WordisPlayer wordisPlayer : wordisPlayers) {
			boardManagers.put(wordisPlayer, new GameBoardManager(wordisPlayer, word));
		}
		// Register thread callback listener
		gameThread = new GameThread();
		gameThread.addCallbackListener(this);
	}
	
	public void setGameSurfaceView(final GameSurfaceView gsv) {
		this.gsv = gsv;
	}
	
	public void setBlockSetFactory(final BlockSetFactory factory) {
		final BlockSetBuffer buffer = new BlockSetBuffer(factory);
		for (GameBoardManager manager : boardManagers.values()) {
			manager.setBlockSetBuffer(buffer);
		}
	}
	
	/**
	 * Set parameters to start game. <br>
	 * 
	 * @throws BlockCreateException Can not create blocks
	 * @throws NoAnimationException Can not add animation 
	 */
	public void startGame(final GameType type) throws BlockCreateException, NoAnimationException {
		gameType = type;
		loser = WordisPlayer.NONE;
		for (GameBoardManager boardManager : boardManagers.values()) {
			boardManager.startGame();
		}
		gameThread.startThread();
	}
	
	/**
	 * Change view size. <br>
	 * 
	 * @param width		View width
	 * @param height	View height
	 * @throws BlockCreateException			Can not create new set of blocks
	 * @throws InvalidParameterException 	Invalid parameters are specified
	 * @throws NoAnimationException 		Animation is not defined
	 */
	public void surfaceSizeChanged(final int width, final int height) throws BlockCreateException, InvalidParameterException, NoAnimationException {
		for (GameBoardManager boardManager : boardManagers.values()) {
			boardManager.surfaceSizeChanged(width, height);
		}
	}
	
	/**
	 * @see ThreadListenerCallback#continueGame()
	 */
	@Override
	public boolean continueGame() {
		boolean isContinue = true;
		for (GameBoardManager manager : boardManagers.values()) {
			if (!manager.continueGame()) {
				isContinue = false;
			}
		}
		return isContinue;
	}
	
	/**
	 * @see ThreadListenerCallback#invokeMainProcess()
	 */
	@Override
	public synchronized void invokeMainProcess(final long elapsedMSec) {
		// 1. Check game is over
		if (loser == WordisPlayer.NONE) {
			for (Entry<WordisPlayer, GameBoardManager> entry : boardManagers.entrySet()) {
				if (entry.getValue().isGameOver()) {
					// First member of hash map is judged if loser firstly
					loser = entry.getKey();
					entry.getValue().setLoser();
					break;
				}
			}
		}
		// 2. set game over or update board positions including animation
		for (GameBoardManager boardManager : boardManagers.values()) {
			if (loser == WordisPlayer.NONE) {
				boardManager.updateBoardObjects(elapsedMSec);
			} else {
				boardManager.setGameOver();
			}
		}
		// 3. draw view images
		gsv.draw(boardManagers.values());
	}
	
	/**
	 * @see ThreadListenerCallback#finishGame()
	 */
	@Override
	public void finishGame() {
		switch (gameType) {
		case TEST:
		case PRACTICE:
		case SINGLE:
		{
			final CurrentGameStats stats = boardManagers.get(WordisPlayer.MY_PLAYER).getGameStats();
			gameTerminate.terminateSingle(stats);
			break;
		}
		case VS_CPU:
		{
			final CurrentGameStats myStats = boardManagers.get(WordisPlayer.MY_PLAYER).getGameStats();
			final CurrentGameStats cpuStats = boardManagers.get(WordisPlayer.COM).getGameStats();
			// FIXME
			gameTerminate.terminateVsCpu(myStats, cpuStats, /*exp*/0, /*point*/0);
			break;
		}
		case MULTI_NET:
			// TODO
			break;
		}
	}
	
	public void forceFinishGame() {
		if (gameThread != null) {
			gameThread.initThread();
		}
	}
	
	public void suspendGame() {
		for (GameBoardManager boardManager : boardManagers.values()) {
			boardManager.suspendGame();
		}
	}
	
	public void resumeGame() {
		for (GameBoardManager boardManager : boardManagers.values()) {
			boardManager.resumeGame();
		}
	}
	
	public GameType getGameType() {
		return gameType;
	}

	/**
	 * Move blocks. <br>
	 * Block operations are valid while not animation. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param player	Wordis player
	 * @param dx		x direction move
	 * @param dy		y direction move
	 */
	public synchronized void moveBlock(final WordisPlayer player, final float dx, final float dy) {
		boardManagers.get(player).moveBlock(dx, dy);
	}
	
	/**
	 * Rotate blocks. <br>
	 * Block operations are valid while not animation. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param player	Wordis player
	 * @param clockWise	Is clockwise rotation or not
	 */
	public synchronized void rotateBlock(final WordisPlayer player, final boolean clockWise) {
		boardManagers.get(player).rotateBlock(clockWise);
	}
	
	/**
	 * On surface touched. <br>
	 * Synchronized because update is conflicted with user operation.
	 * 
	 * @param event Touch event
	 */
	public synchronized void onSurfaceTouched(MotionEvent event) {
		// TODO
	}
}