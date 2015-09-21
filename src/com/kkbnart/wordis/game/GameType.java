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
	MULTI_NET(3, "multi_net");
	
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
