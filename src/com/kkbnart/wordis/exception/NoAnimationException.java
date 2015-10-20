package com.kkbnart.wordis.exception;

import com.kkbnart.wordis.game.GameStatus;

public class NoAnimationException extends Exception {
	private static final long serialVersionUID = 7637854732310798213L;

	public NoAnimationException(final GameStatus action) {
		String msg = "Game action for the type [" + action.toString() + "] declared.";
		System.err.println(msg);
	}
}
