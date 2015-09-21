package com.kkbnart.wordis.game.object;

import java.util.ArrayList;
import java.util.HashMap;

import com.kkbnart.wordis.game.exception.BlockCreateException;


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
	 */
	public BlockSetFactory(final BlockColorSet colorSet, final CharacterSet characterSet) {
		this.colorSet = colorSet;
		this.characterSet = characterSet;
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
		for (int[] p : pd.getPositions()) {
			final int randomIndex = (int)(Math.random()*charaPatterns.size());
			final Character c = (Character)charaPatterns.keySet().toArray()[randomIndex];
			final int id = BlockIdFactory.getInstance().assignIds(1)[0]; // Get first element
			final Block b = new Block(/*color=*/charaPatterns.get(c), /*character=*/c, /*id=*/id, /*x=*/p[0], /*y=*/p[1]);
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
}
