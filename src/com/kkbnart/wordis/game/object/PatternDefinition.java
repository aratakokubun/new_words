package com.kkbnart.wordis.game.object;

import java.util.ArrayList;

import android.graphics.Point;

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
}
