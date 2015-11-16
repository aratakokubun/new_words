package com.kkbnart.wordis.game.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkbnart.utils.FileIOUtils;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.LoadPropertyException;

/**
 * <p>
 * Specify layout(board, next blocks, ...) in the GAME mode. <br>
 * See definition of game type in {@link com.kkbnart.wordis.game.GameType}
 * </p>
 * <p>
 * To access this class, use {@link #getInstance()}. <br>
 * </p>
 * 
 * @author kkbnart
 */
public class ViewLayout {
	// Singleton instance for this class
	private static ViewLayout instance = null;

	// Common parameters
	// Board parameters
	private static String BOARD = "board";
	public int boardRow, boardCol;
	public int boardCollisionX, boardCollisionY;
	public int boardCollisionRow, boardCollisionCol;
	public float boardWRate, boardHRate;
	public float boardStackCellX, boardStackCellY;

	// Operated block parameters
	private static String OPERATED = "operated";
	public int operatedX, operatedY;
	
	// Next block parameters
	private static String NEXT = "next";
	public float nextMarginX, nextMarginY;
	public int nextSize;
	
	// Parameters characteristic for position
	private Map<Integer, LayoutDefinition> layoutsDefs = new HashMap<Integer, LayoutDefinition>();
	
	/**
	 * Get instance of this class. <br>
	 * This method is the only way to access. <br>
	 * 
	 * @return Instance of this class
	 */
	public static ViewLayout getInstance() {
		if (instance == null) {
			instance = new ViewLayout();
		}
		return instance;
	}
	
	/**
	 * <p>
	 * To get instance of this class, you have to use {@link #getInstance()}. <br>
	 * You are not allowed to call private constructor {@link #MoveAmount()}.
	 * </p>
	 */
	private ViewLayout() {}
	
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
			
			// Common parameters
			final JSONObject common = property.getJSONObject("common");
			// Board parameters
			final JSONObject boardJson = common.getJSONObject(BOARD);
			boardRow = boardJson.getInt("row");
			boardCol = boardJson.getInt("col");
			boardCollisionX = boardJson.getInt("collisionX");
			boardCollisionY = boardJson.getInt("collisionY");
			boardCollisionRow = boardJson.getInt("collisionRow");
			boardCollisionCol = boardJson.getInt("collisionCol");
			boardWRate = (float)boardJson.getDouble("wRate");
			boardHRate = (float)boardJson.getDouble("hRate");
			boardStackCellX = (float)boardJson.getDouble("stackCellX");
			boardStackCellY = (float)boardJson.getDouble("stackCellY");
			// Operated block parameters
			final JSONObject operatedJson = common.getJSONObject(OPERATED);
			operatedX = operatedJson.getInt("x");
			operatedY = operatedJson.getInt("y");
			// Next block parameters
			final JSONObject nextJson = common.getJSONObject(NEXT);
			nextMarginX = (float)nextJson.getDouble("marginX");
			nextMarginY = (float)nextJson.getDouble("marginY");
			nextSize = nextJson.getInt("size");
			
			// Position based layout parameters
			final JSONArray layouts = property.getJSONArray("layouts");
			for (int i = 0; i < layouts.length(); i++) {
				final JSONObject layout = layouts.getJSONObject(i);
				final int side = layout.getInt("side");
				layoutsDefs.put(side, new LayoutDefinition(layout));
			}
		} catch (IOException | JSONException e) {
			System.out.println(e);
			throw new LoadPropertyException();
		}
	}
	
	public LayoutDefinition getLayoutDefinition(final int side) {
		return layoutsDefs.get(side);
	}
}
