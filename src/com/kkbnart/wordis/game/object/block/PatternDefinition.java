package com.kkbnart.wordis.game.object.block;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;

import com.kkbnart.wordis.exception.LoadPropertyException;

public class PatternDefinition {
	private ArrayList<Point> positions;
	private int center;
	
	public PatternDefinition(final ArrayList<Point> positions, final int center) {
		this.positions = positions;
		this.center = center;
	}
	
	public ArrayList<Point> getPositions() {
		return positions;
	}
	
	public int getCenter() {
		return center;
	}
	
	public static PatternDefinition readFromJsonObject(JSONObject pattern) throws LoadPropertyException, JSONException {
		final int centerIndex = pattern.getInt("center");
		final JSONArray blocks = pattern.getJSONArray("blocks");
		ArrayList<Point> blockPos = new ArrayList<Point>();
		for (int j = 0; j < blocks.length(); j++) {
			final JSONObject pos = blocks.getJSONObject(j);
			final int x = pos.getInt("x");
			final int y = pos.getInt("y");
			blockPos.add(new Point(x, y));
		}
		// Validate
		if (centerIndex >= blockPos.size()) {
			throw new LoadPropertyException();
		}
		return new PatternDefinition(blockPos, centerIndex);
	}
}
