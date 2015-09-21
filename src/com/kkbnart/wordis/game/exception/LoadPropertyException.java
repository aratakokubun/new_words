package com.kkbnart.wordis.game.exception;

public class LoadPropertyException extends Exception {
	private static final long serialVersionUID = 3720051922032354799L;

	public LoadPropertyException() {
		final String msg = "Can not load property";
		System.err.println(msg);
	}
}
