package com.kkbnart.wordis.exception;

import com.kkbnart.wordis.util.WordisFontTypes;

public class FontNotExistException extends Exception {
	private static final long serialVersionUID = 8090361395293972768L;

	public FontNotExistException(final String filepath) {
		System.err.println(filepath + " does not exist.");
	}
	
	public FontNotExistException(final WordisFontTypes wordisFontType) {
		System.err.println("Font type [" + wordisFontType.toString() + "] is not defined.");
	}
}
