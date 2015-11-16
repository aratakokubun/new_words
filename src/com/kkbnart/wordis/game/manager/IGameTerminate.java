package com.kkbnart.wordis.game.manager;


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
	 * @param exp
	 * @param point
	 */
	public abstract void terminateVsCpu(final CurrentGameStats myStatus, final CurrentGameStats cpuStatus,
			final int exp, final int point);
	
	/**
	 * Terminate game for multiple player game. <br>
	 * 
	 * @param myStatus	Status of this player
	 * @param oppStatus	Status of opponent player
	 */
	public abstract void terminateVsPlayer(final CurrentGameStats myStatus, final CurrentGameStats oppStatus);
}
