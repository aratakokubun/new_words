package com.kkbnart.wordis.game;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkbnart.utils.FileIOUtils;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.LoadPropertyException;

/**
 * <p>
 * Specify definition of game type in this application. <br>
 * See definition of game type in {@link com.kkbnart.wordis.game.GameType}
 * </p>
 * <p>
 * To access this class, use {@link #getInstance()}. <br>
 * </p>
 * 
 * @author kkbnart
 */
public class GameTypeDefinition {
	// FIXME
	// adapt to multiple players
	
	// Singleton instance of this class
	private static GameTypeDefinition instance = null;
	
	// Board parameters
	private static String BOARD = "board";
	public int boardRow, boardCol;
	public int boardCollisionX, boardCollisionY;
	public int boardCollisionRow, boardCollisionCol;
	public float boardXRate, boardYRate;
	public float boardWRate, boardHRate;
	public float boardStackCellX, boardStackCellY;

	// Operated block parameters
	private static String OPERATED = "operated";
	public int operatedX, operatedY;
	
	// Next block parameters
	private static String NEXT = "next";
	public float nextX, nextY;
	public float nextMarginX, nextMarginY;
	public int nextSize;
	
	// Score Area
	private static String SCORE = "score";
	public float scoreLeftXRate, scoreLeftYRate;
	public int scoreLeftTextSize;
	public float scoreRightXRate, scoreRightYRate;
	public int scoreRightTextSize;
	
	/**
	 * Get instance of this class. <br>
	 * This method is the only way to access. <br>
	 * 
	 * @return Instance of this class
	 */
	public static synchronized GameTypeDefinition getInstance() {
		if (instance == null) {
			instance = new GameTypeDefinition();
		}
		return instance;
	}
	
	/**
	 * <p>
	 * To get instance of this class, you have to use {@link #getInstance()}. <br>
	 * You are not allowed to call private constructor {@link #MoveAmount()}.
	 * </p>
	 */
	private GameTypeDefinition() {}

	/**
	 * Load game type property from asset file. <br>
	 * 
	 * @param propertyName	Game type property name
	 * @param context		Android activity context
	 * @throws LoadPropertyException Can not read specified property
	 */
	public void readJson(final String propertyName, final Context context) throws LoadPropertyException {
		try {
			// Read from json objects
			final String jsonStr = FileIOUtils.loadTextAsset(Constants.GAME_TYPE_JSON, context);
			final JSONObject property = (new JSONObject(jsonStr)).getJSONObject(propertyName);
			// Board parameters
			final JSONObject boardJson = property.getJSONObject(BOARD);
			boardRow = boardJson.getInt("row");
			boardCol = boardJson.getInt("col");
			boardCollisionX = boardJson.getInt("collisionX");
			boardCollisionY = boardJson.getInt("collisionY");
			boardCollisionRow = boardJson.getInt("collisionRow");
			boardCollisionCol = boardJson.getInt("collisionCol");
			boardXRate = (float)boardJson.getDouble("xRate");
			boardYRate = (float)boardJson.getDouble("yRate");
			boardWRate = (float)boardJson.getDouble("wRate");
			boardHRate = (float)boardJson.getDouble("hRate");
			boardStackCellX = (float)boardJson.getDouble("stackCellX");
			boardStackCellY = (float)boardJson.getDouble("stackCellY");
			// Operated block parameters
			final JSONObject operatedJson = property.getJSONObject(OPERATED);
			operatedX = operatedJson.getInt("x");
			operatedY = operatedJson.getInt("y");
			// Next block parameters
			final JSONObject nextJson = property.getJSONObject(NEXT);
			nextX = (float)nextJson.getDouble("x");
			nextY = (float)nextJson.getDouble("y");
			nextMarginX = (float)nextJson.getDouble("marginX");
			nextMarginY = (float)nextJson.getDouble("marginY");
			nextSize = nextJson.getInt("size");
			// Score text parameters
			final JSONObject scoreJson = property.getJSONObject(SCORE);
			final JSONObject scoreLeftJson = scoreJson.getJSONObject("left");
			scoreLeftXRate = (float)scoreLeftJson.getDouble("xRate");
			scoreLeftYRate = (float)scoreLeftJson.getDouble("xRate");
			final JSONObject scoreRightJson = scoreJson.getJSONObject("right");
			scoreLeftXRate = (float)scoreRightJson.getDouble("xRate");
			scoreLeftYRate = (float)scoreRightJson.getDouble("xRate");
		} catch (IOException | JSONException e) {
			System.out.println(e);
			throw new LoadPropertyException();
		}
	}
	
	/**
	 * Load property with jackson json. <br>
	 * This method is deprecated because jackson json raise error with android. <br>
	 * Maybe this cause particular version of android OS or devices. <br>
	 * 
	 * @param propertyName
	 * @throws LoadPropertyException
	 */
	@Deprecated
	public void loadJsonJackson(final String propertyName) throws LoadPropertyException {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(new File(Constants.GAME_TYPE_JSON));
			root = root.get(propertyName);
			// Board parameters
			final JsonNode boardRoot = root.get(BOARD);
			boardRow = boardRoot.get("row").asInt();
			boardCol = boardRoot.get("col").asInt();
			boardCollisionX = boardRoot.get("collisionX").asInt();
			boardCollisionY = boardRoot.get("collisionY").asInt();
			boardCollisionRow = boardRoot.get("collisionRow").asInt();
			boardCollisionCol = boardRoot.get("collisionCol").asInt();
			boardXRate = (float)boardRoot.get("xRate").asDouble();
			boardYRate = (float)boardRoot.get("yRate").asDouble();
			boardWRate = (float)boardRoot.get("wRate").asDouble();
			boardHRate = (float)boardRoot.get("hRate").asDouble();
			boardStackCellX = (float)boardRoot.get("stackCellX").asDouble();
			boardStackCellY = (float)boardRoot.get("stackCellY").asDouble();
			// Operated block parameters
			final JsonNode operatedRoot = root.get(OPERATED);
			operatedX = operatedRoot.get("x").asInt();
			operatedY = operatedRoot.get("y").asInt();
			// Next block parameters
			final JsonNode nextRoot = root.get(NEXT);
			nextX = (float)nextRoot.get("x").asDouble();
			nextY = (float)nextRoot.get("y").asDouble();
			nextMarginX = (float)nextRoot.get("marginX").asDouble();
			nextMarginY = (float)nextRoot.get("marginY").asDouble();
			nextSize = nextRoot.get("size").asInt();
		} catch (Exception e) {
			System.out.println(e);
			throw new LoadPropertyException();
		}
	}
}
