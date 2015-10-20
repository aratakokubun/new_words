package com.kkbnart.wordis.game.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;

import com.kkbnart.utils.FileIOUtils;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.LoadPropertyException;


/**
 * Factory to create block set randomly
 * @author kkbnart
 */
public class BlockSetFactory {
	// String pattern to create
	private String word = new String();
	
	// Block color set
	private BlockColorSet colorSet;
	// Character set
	private CharacterSet characterSet;
	// Block position pattern
	private HashMap<PatternDefinition, Double> blockPatterns = new HashMap<PatternDefinition, Double>();
	// Character and color pattern
	private HashMap<Character, BlockColor> charaPatterns = new HashMap<Character, BlockColor>();
	
	/**
	 * Constructor with specifying color set. <br>
	 * 
	 * @param activity	Android Activity
	 * @param property	Block set property name
	 * @param word		Answer word
	 * @throws BlockCreateException		Can not create new block set
	 * @throws LoadPropertyException	Can not read property file
	 */
	public BlockSetFactory (final Activity activity, final String property, final String word) throws BlockCreateException, LoadPropertyException {
		BlockColorSet colorSet = new BlockColorSet(activity.getResources());
		CharacterSet characterSet = new CharacterSet(activity.getResources());
		this.colorSet = colorSet;
		this.characterSet = characterSet;
		readJson(property, activity);
		registerCharacterPattern(word);
	}
	
	/**
	 * Add a pattern of block set. <br>
	 * 
	 * @param id		Pattern id
	 * @param pattern	Block set pattern
	 */
	public void registerBlockPatterns(final PatternDefinition pdef, final double rate) {
		blockPatterns.put(pdef, rate);
	}
	
	/**
	 * Add a pattern of character set. <br>
	 * 
	 * @param word String pattern
	 */
	public void registerCharacterPattern(final String word) {
		this.word = word;
		
		final int[] colorIds = shuffleColor(word.length());
		for (int i = 0 ; i < word.length(); i++) {
			charaPatterns.put(characterSet.getCharacter(word.substring(i, i+1)),
					colorSet.getColor(colorIds[i]));
		}
	}
	
	/**
	 * Shuffle pattern for combinations between characters and colors. <br>
	 * 
	 * @param len Number of color pattern to create
	 * @return Created color pattern
	 */
	private int[] shuffleColor(final int len) {
		int[] colorIds = new int[len];
		for (int i = 0 ; i < len; i++) {
			colorIds[i] = i;
		}
		// Swap numbers
		for (int i = 0; i < 100/* number of shuffles */; i++) {
			final int j = (int)(Math.random()*len);
			final int k = (int)(Math.random()*len);
			final int temp = colorIds[j];
			colorIds[j] = colorIds[k];
			colorIds[k] = temp;
		}
		return colorIds;
	}
	
	/**
	 * Create block pattern with specified pattern. <br>
	 * 
	 * @param pattern Specified pattern
	 * @return Block pattern
	 * @throws BlockCreateException Can not create new blocks
	 */
	public BlockSet create(final PatternDefinition pd) throws BlockCreateException {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Point p : pd.getPositions()) {
			final int randomIndex = (int)(Math.random()*charaPatterns.size());
			final Character c = (Character)charaPatterns.keySet().toArray()[randomIndex];
			final int id = BlockIdFactory.getInstance().assignIds(1)[0]; // Get first element
			final Block b = new Block(/*color=*/charaPatterns.get(c), /*character=*/c, /*id=*/id, /*x=*/p.x, /*y=*/p.y);
			blocks.add(b);
		}
		return new BlockSet(blocks, pd.getCenter());
	}
	
	/**
	 * Create block pattern randomly. <br>
	 * This method call {@link #create(PatternDefinition)} in the step of creating an instance. <br>
	 * 
	 * @return Block pattern
	 * @throws BlockCreateException Can not create new blocks
	 */
	public BlockSet create() throws BlockCreateException {
		final double sumRate = sumRate();
		final double rate = Math.random()*sumRate;
		double accumulatedRate = 0.0;
		for (PatternDefinition pd : blockPatterns.keySet()) {
			final double patternRate = blockPatterns.get(pd);
			if (accumulatedRate <= rate && rate < accumulatedRate+patternRate) {
				return create(pd);
			}
			accumulatedRate += patternRate;
		}
		throw new BlockCreateException();
	}
	
	/**
	 * Get sum of the pattern rates. <br>
	 * 
	 * @return Sum of the pattern rates
	 */
	private double sumRate() {
		double sum = 0.0;
		for (PatternDefinition pd : blockPatterns.keySet()) {
			sum += blockPatterns.get(pd);
		}
		return sum;
	}
	
	/**
	 * Clear pattern of character and image of it. <br>
	 */
	public void clearCharacterPatterns() {
		charaPatterns.clear();
	}
	
	public String getWord() {
		return word;
	}

	/**
	 * Load block patterns from asset file. <br>
	 * 
	 * @param propertyName	Block pattern property name
	 * @param context		Android activity context
	 * @throws LoadPropertyException Can not read specified property
	 */
	public void readJson(final String propertyName, final Context context) throws LoadPropertyException {
		try {
			// Read from JSON objects
			final String jsonStr = FileIOUtils.loadTextAsset(Constants.BLOCK_PATTERN_JSON, context);
			final JSONObject property = (new JSONObject(jsonStr)).getJSONObject(propertyName);
			
			// Add move amount of each direction
			final JSONArray patterns = property.getJSONArray("patterns");
			for (int i = 0; i < patterns.length(); i++) {
				final JSONObject pattern = patterns.getJSONObject(i);
				final int centerIndex = pattern.getInt("center");
				final float weight = (float)pattern.getDouble("weight");
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
				registerBlockPatterns(new PatternDefinition(blockPos, centerIndex), weight);
			}
		} catch (IOException | JSONException e) {
			throw new LoadPropertyException();
		}
	}
}
