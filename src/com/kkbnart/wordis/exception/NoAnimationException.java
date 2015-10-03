package com.kkbnart.wordis.exception;

import com.kkbnart.wordis.game.GameAction;

public class NoAnimationException extends Exception {
	private static final long serialVersionUID = 7637854732310798213L;

	public NoAnimationException(final GameAction action) {
		String msg = "Game action for the type [" + action.toString() + "] declared.";
		System.err.println(msg);
	}
}
