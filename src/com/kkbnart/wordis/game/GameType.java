package com.kkbnart.wordis.game;

/**
 * Specify type of games. <br>
 * 
 * @author kkbnart
 */
public enum GameType {
	TEST(0, "test"),
	PRACTICE(1, "practice"),
	SINGLE(2, "single"),
	VS_CPU(3, "vs cpu"),
	MULTI_NET(4, "multi net");
	
	private int id;
	private String jsonPropety;
	
	private GameType(final int id, final String jsonPropety) {
		this.id = id;
		this.jsonPropety = jsonPropety;
	}
	public int getId() {
		return id;
	}
	public String getJsonProperty() {
		return jsonPropety;
	}
}
