package com.kkbnart.wordis.exception;

import com.kkbnart.wordis.game.player.WordisPlayer;


public class NoSuchPlayerException extends Exception {
	private static final long serialVersionUID = 7637854732310798213L;

	public NoSuchPlayerException(final WordisPlayer player) {
		final String msg = "Player type : " + player.toString() + " does not exist in this game.";
		System.err.println(msg);
	}
}
