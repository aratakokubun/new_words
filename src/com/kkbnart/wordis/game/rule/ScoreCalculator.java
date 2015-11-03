package com.kkbnart.wordis.game.rule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkbnart.utils.FileIOUtils;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.LoadPropertyException;

public class ScoreCalculator {
	// Singleton instance for this class
	private static ScoreCalculator instance = null;
	
	// Bonus given by the sum of number of blocks deleted in this delete chain
	private static final String SUM_BONUS = "sumBonus";
	private int sumBonus;
	// Bonus given by the number of blocks deleted in this delete action
	private static final String NUMBER_BONUS = "numberBonus";
	private int numberMin;
	private int numberMax;
	private Map<Integer, Integer> numberBonus = new HashMap<Integer, Integer>();
	// Bonus given by the number of chain
	private static final String CHAIN_BONUS = "chainBonus";
	private int chainMin;
	private int chainMax;
	private Map<Integer, Integer> chainBonus = new HashMap<Integer, Integer>();
	
	/**
	 * Get instance of this class. <br>
	 * This method is the only way to access. <br>
	 * 
	 * @return Instance of this class
	 */
	public static ScoreCalculator getInstance() {
		if (instance == null) {
			instance = new ScoreCalculator();
		}
		return instance;
	}
	
	/**
	 * <p>
	 * To get instance of this class, you have to use {@link #getInstance()}. <br>
	 * You are not allowed to call private constructor {@link #ScoreCalculator()}.
	 * </p>
	 */
	private ScoreCalculator() {}
	
	/**
	 * Calculate score based on sum, number, and chain of delete. <br>
	 * 
	 * @param deleteIds Deleted block ids
	 * @param word		Target word
	 * @param chain		Number of chain
	 * @param sumBlocks	Sum of deleted block in this delete action
	 * @return Calculated score
	 */
	public int calcDeleteScore(final Set<Integer> deleteIds, final String word, final int chain, final int sumBlocks) {
		// A = sum * sumBonus
		// B = numberBonus.get(number) + chainBonus.get(chain)
		// score(chain) = A * Math.max(B, 1)
		final int number = deleteIds.size() - word.length();
		return sumBlocks * sumBonus * Math.min(getNumberBonus(number) + getChainBonus(chain), 1);
	}
	
	private int getNumberBonus(final int number) {
		if (number < numberMin) {
			return numberBonus.get(numberMin);
		} else if (number > numberMax) {
			return numberBonus.get(numberMax);
		} else {
			return numberBonus.get(number);
		}
	}
	
	private int getChainBonus(final int chain) {
		if (chain < chainMin) {
			return chainBonus.get(chainMin);
		} else if (chain > chainMax) {
			return chainBonus.get(chainMax);
		} else {
			return chainBonus.get(chain);
		}
	}
	
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
			final String jsonStr = FileIOUtils.loadTextAsset(Constants.SCORE_PATTERN_JSON, context);
			final JSONObject property = (new JSONObject(jsonStr)).getJSONObject(propertyName);
			// Sum bonus
			sumBonus = property.getInt(SUM_BONUS);
			// Number bonus
			final JSONObject numberJson = property.getJSONObject(NUMBER_BONUS);
			numberMin = numberJson.getInt("min");
			numberMax = numberJson.getInt("max");
			for (int i = numberMin; i <= numberMax; i++) {
				numberBonus.put(i, numberJson.getInt(String.valueOf(i)));
			}
			// Chain bonus
			final JSONObject chainJson = property.getJSONObject(CHAIN_BONUS);
			chainMin = chainJson.getInt("min");
			chainMax = chainJson.getInt("max");
			for (int i = chainMin; i <= chainMax; i++) {
				chainBonus.put(i, chainJson.getInt(String.valueOf(i)));
			}
		} catch (IOException | JSONException e) {
			throw new LoadPropertyException();
		}
	}
}
