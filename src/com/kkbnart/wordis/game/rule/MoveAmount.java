package com.kkbnart.wordis.game.rule;

import java.util.Map;

import android.graphics.PointF;

import com.kkbnart.wordis.game.util.Direction;


/**
 * Specify amount of movement on player operations. <br>
 * See definition of direction in {@link com.kkbnart.wordis.game.util.Direction}. <br>
 * 
 * @author kkbnart
 */
public class MoveAmount {
	// Amount of movement corresponds to each Direction
	private Map<Integer, PointF> moves;

	/**
	 * Constructor to add default amount of move. <br>
	 * In this constructor, specify Directions in {@link com.kkbnart.wordis.game.util.Direction}. <br>
	 * 
	 * You may want to specify value for each direction after calling this. <br>
	 */
	public MoveAmount() {
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
}
