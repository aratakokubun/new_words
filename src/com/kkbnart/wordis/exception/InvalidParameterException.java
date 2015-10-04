package com.kkbnart.wordis.exception;

public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = 7637854732310798213L;
	
	public static final int ID_OVERFLOW = 1;

	public InvalidParameterException() {
		String msg = "Parameters are invalid.";
		System.err.println(msg);
	}
	
	public InvalidParameterException(final String name, final Object value) {
		String msg = "Parameter " + name + " is invalid for value [" + value.toString() + "].";
		System.err.printf(msg);
	}
}
