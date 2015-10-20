package com.kkbnart.wordis.game;

/**
 * Interface between Game Thread and it's manager. <br>
 * @author kkbnart
 */
public interface GameThreadManager {
	/**
	 * Judge if continue game. <br>
	 * 
	 * @return true  : continue game <br>
	 * 		   false : terminate game <br>
	 */
	public boolean continueGame();

	/**
	 * Periodical process while game. <br>
	 * Synchronized because update is conflicted with user operation. <br>
	 */
	public void invokeMainProcess();

	/**
	 * Process called on finish game
	 */
	public void finishGame();
}
