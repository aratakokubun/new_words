package com.kkbnart.wordis.game.rule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.PointF;

import com.kkbnart.utils.FileIOUtils;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.LoadPropertyException;
import com.kkbnart.wordis.game.util.Direction;


/**
 * <p>
 * Specify amount of movement on player operations. <br>
 * See definition of direction in {@link com.kkbnart.wordis.game.util.Direction}. <br>
 * </p>
 * <p>
 * To access this class, use {@link #getInstance()}. <br>
 * </p>
 * 
 * @author kkbnart
 */
public class MoveAmount {
	// Singleton instance of this class
	private static MoveAmount instance = null;
	// Amount of movement corresponds to each Direction
	private Map<Integer, PointF> moves = new HashMap<Integer, PointF>();

	/**
	 * Get instance of this class. <br>
	 * This method is the only way to access. <br>
	 * 
	 * @return Instance of this class
	 */
	public static synchronized MoveAmount getInstance() {
		if (instance == null) {
			instance = new MoveAmount();
		}
		return instance;
	}
	
	/**
	 * <p>
	 * Constructor to add default amount of move. <br>
	 * In this constructor, specify Directions in {@link com.kkbnart.wordis.game.util.Direction}. <br>
	 * </p>
	 * <p>
	 * You can specify value for each direction after calling this. <br>
	 * </p>
	 * <p>
	 * To get instance of this class, you have to use {@link #getInstance()}. <br>
	 * You are not allowed to call private constructor {@link #MoveAmount()}.
	 * </p>
	 */
	private MoveAmount() {
		for (Direction d : Direction.values()) {
			moves.put(d.getId(), new PointF(d.getSampleX(), d.getSampleY()));
		}
	}
	
	public void setDirectionMove(final int id, final PointF p) {
		moves.put(id, p);
	}
	
	public PointF getMove(final int id) {
		return moves.get(id);
	}
	
	/**
	 * Load move amount property from asset file. <br>
	 * 
	 * @param propertyName	Move amount property name
	 * @param context		Android activity context
	 * @throws LoadPropertyException Can not read specified property
	 */
	public void readJson(final String propertyName, final Context context) throws LoadPropertyException {
		try {
			// Read from JSON objects
			final String jsonStr = FileIOUtils.loadTextAsset(Constants.MOVE_AMOUNT_JSON, context);
			final JSONObject property = (new JSONObject(jsonStr)).getJSONObject(propertyName);

			// Update free fall
			final JSONObject freeFallObject = property.getJSONObject("freeFall");
			final float xPerMSec = (float)freeFallObject.getDouble("x");
			final float yPerMSec = (float)freeFallObject.getDouble("y");
			Fall.getInstance().setFallPerMSec(xPerMSec, yPerMSec);
			final float axPerMSec = (float)freeFallObject.getDouble("ax");
			final float ayPerMSec = (float)freeFallObject.getDouble("ay");
			Fall.getInstance().setAccellPerMSec(axPerMSec, ayPerMSec);
			
			// Add move amount of each direction
			final JSONObject operateObject = property.getJSONObject("operate");
			for (Direction d : Direction.values()) {
				final JSONObject dirJson = operateObject.getJSONObject(d.getName());
				final float x = (float)dirJson.getDouble("x");
				final float y = (float)dirJson.getDouble("y");
				moves.put(d.getId(), new PointF(x, y));
			}
		} catch (IOException | JSONException e) {
			throw new LoadPropertyException();
		}
	}
}
