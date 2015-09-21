package com.kkbnart.wordis.game.util;

import java.math.BigDecimal;
import java.math.MathContext;

public class FloatRound {
	private static final int DIGIT = 4;
	
	public static float round(final float f) {
		BigDecimal bd = new BigDecimal(f);
		MathContext m = new MathContext(DIGIT);
		return bd.round(m).floatValue();
	}
}
