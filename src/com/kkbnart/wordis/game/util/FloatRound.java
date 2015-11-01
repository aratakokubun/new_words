package com.kkbnart.wordis.game.util;

import java.math.BigDecimal;
import java.math.MathContext;

public class FloatRound {
	private static final int DIGIT = 4;
	private static final float EQAULS_DIFF = 0.01f;
	
	public static float round(final float f) {
		BigDecimal bd = new BigDecimal(f);
		MathContext m = new MathContext(DIGIT);
		return bd.round(m).floatValue();
	}
	
	public static boolean equals(final float f1, final float f2) {
		return Math.abs(f1-f2) < EQAULS_DIFF;
 	}
}
