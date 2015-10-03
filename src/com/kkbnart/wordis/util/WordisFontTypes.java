package com.kkbnart.wordis.util;


public enum WordisFontTypes {
	OOGIEBOO(0, "fonts/OOGIEBOO.ttf"),
	;
	
	private int id;
	private String filepath;
	private WordisFontTypes(final int id, final String filepath) {
		this.id = id;
		this.filepath = filepath;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFilepath() {
		return filepath;
	}
}
