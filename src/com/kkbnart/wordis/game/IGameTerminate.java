package com.kkbnart.wordis.game;

import com.kkbnart.wordis.game.player.PlayerStatus;

/**
 * <p>
 * Interface for terminating game. <br>
 * </p>
 * 
 * @author kkbnart
 */
public interface IGameTerminate {
	/**
	 * Terminate game for single player game. <br>
	 * 
	 * @param currentGameStats Statics of the current game
	 */
	public abstract void terminateSingle(final CurrentGameStats currentGameStats);

	/**
	 * Terminate game for versus CPU game. <br>
	 * 
	 * @param myStatus	Status of this player
	 * @param cpuStatus	Status of CPU
	 */
	public abstract void terminateVsCpu(final PlayerStatus myStatus, final PlayerStatus cpuStatus);
	
	/**
	 * Terminate game for multiple player game. <br>
	 * 
	 * @param myStatus	Status of this player
	 * @param oppStatus	Status of opponent player
	 */
	public abstract void terminateVersus(final PlayerStatus myStatus, final PlayerStatus oppStatus);
}
