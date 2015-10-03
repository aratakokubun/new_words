package com.kkbnart.wordis.exception;

public class BlockCreateException extends Exception {
	private static final long serialVersionUID = 7637854732310798213L;
	
	public static final int ID_OVERFLOW = 1;

	public BlockCreateException() {
		String msg = "Can not create block";
		System.err.println(msg);
	}
	
	public BlockCreateException(final int cause) {
		String msg = "Can not create block";
		switch (cause) {
		case ID_OVERFLOW:
			msg += " because new number can not be assigned.";
			break;
		default:
			break;
		}
		System.err.println(msg);
	}
}
