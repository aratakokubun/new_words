package com.kkbnart.wordis.game.layout;

import org.json.JSONException;
import org.json.JSONObject;

public class LayoutDefinition {
	// Board parameters
	private static String BOARD = "board";
	public float boardXRate, boardYRate;
	
	// Next block parameters
	private static String NEXT = "next";
	public float nextX, nextY;
	
	// Score Area
	private static String SCORE = "score";
	public float scoreXRate, scoreYRate;
	public int scoreTextSize;
	
	public LayoutDefinition (final JSONObject layout) throws JSONException {
		// Board parameters
		final JSONObject boardJson = layout.getJSONObject(BOARD);
		boardXRate = (float)boardJson.getDouble("xRate");
		boardYRate = (float)boardJson.getDouble("yRate");
		// Next block parameters
		final JSONObject nextJson = layout.getJSONObject(NEXT);
		nextX = (float)nextJson.getDouble("x");
		nextY = (float)nextJson.getDouble("y");
		// Score text parameters
		final JSONObject scoreJson = layout.getJSONObject(SCORE);
		scoreXRate = (float)scoreJson.getDouble("xRate");
		scoreYRate = (float)scoreJson.getDouble("yRate");
		scoreTextSize = scoreJson.getInt("size");
	}
}
