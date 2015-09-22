package com.kkbnart.wordis.game.rule;

import java.util.HashSet;
import java.util.Set;

import com.kkbnart.wordis.game.object.Block;

public class DeleteBlockLine {
	// Substitute string for a blank cell
	private static final String NULL_OPTION = ",";

	/**
	 * Delete line of blocks which construct the specified {@code word} in the same order. <br>
	 * Lines of vertical, horizontal, left diagonal (from left top to right bottom) and right diagonal (from right top to left bottom) <br>
	 * are searched. <br>
	 * Ids of deleted blocks are dissociated from id factory. <br>
	 * 
	 * @param matrix 	Matrix form of the board blocks
	 * @param word		Word to search
	 * @return Set of deleted block id
	 */
	public static Set<Integer> deleteWordLine(final Block[][] matrix, final String word, final int order) {
		Set<Integer> deletedIds = new HashSet<Integer>();

		// TODO
		// count how many times each id is selected with different direction. (count 1 for multiple times in same direction)
		
		deletedIds.addAll(deleteVerticalWordLine(matrix, word, order));
		deletedIds.addAll(deleteHorizontalWordLine(matrix, word, order));
		deletedIds.addAll(deleteLeftDiagonalWordLine(matrix, word, order));
		deletedIds.addAll(deleteRightDiagonalWordLine(matrix, word, order));
		
		return deletedIds;
	}
	
	/**
	 * Delete line of blocks for vertical direction. <br>
	 * 
	 * @param matrix 	Matrix form of the board blocks
	 * @param word		Word to search
	 * @param order		1  : Right order of {@code word} is allowed. <br>
	 * 					-1 : Inverse order of {@code word} is allowed. <br>
	 * 					0  : Both are allowed. <br>
	 * @return Set of deleted block id
	 */
	public static Set<Integer> deleteVerticalWordLine(final Block[][] matrix,
			final String word, final int order) {
		Set<Integer> deletedBlockIds = new HashSet<Integer>();
		
		for (int col = 0; col < matrix[0].length; col++) {
			// Create a sequence of characters of this row
			String colWordSequence = new String();
			for (int row = 0; row < matrix.length; row++) {
				colWordSequence += convertBlockString(matrix[row][col]);
			}

			if (order >= 0) {
				// Search from start to end for the word
				Set<Integer> indices = searchWord(colWordSequence, word);
				for (int index : indices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(matrix[index+i][col].getId());
					}
				}
			}
			if (order <= 0) {
				// Search from end to start for the word (inversely)
				String reversedSequence = new StringBuilder(colWordSequence).reverse().toString();
				Set<Integer> inverseIndices = searchWord(reversedSequence, word);
				for (int index : inverseIndices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(matrix[(matrix.length-1) - (index+i)][col].getId());
					}
				}
			}
		}
		return deletedBlockIds;
	}

	/**
	 * Delete line of blocks for horizontal direction. <br>
	 * 
	 * @param matrix Matrix form of the board blocks
	 * @param word		Word to search
	 * @param order		1  : Right order of {@code word} is allowed. <br>
	 * 					-1 : Inverse order of {@code word} is allowed. <br>
	 * 					0  : Both are allowed. <br>
	 * @return Set of deleted block id
	 */
	public static Set<Integer> deleteHorizontalWordLine(final Block[][] matrix,
			final String word, final int order) {
		Set<Integer> deletedBlockIds = new HashSet<Integer>();
		
		for (Block[] rowBlocks : matrix) {
			// Create a sequence of characters of this row
			String rowWordSequence = new String();
			for (Block b : rowBlocks) {
				rowWordSequence += convertBlockString(b);
			}
			
			if (order >= 0) {
				// Search from start to end for the word
				Set<Integer> indices = searchWord(rowWordSequence, word);
				for (int index : indices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(rowBlocks[index+i].getId());
					}
				}
			}
			if (order <= 0) {
				// Search from end to start for the word (inversely)
				String reversedSequence = new StringBuilder(rowWordSequence).reverse().toString();
				Set<Integer> inverseIndices = searchWord(reversedSequence, word);
				for (int index : inverseIndices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(rowBlocks[(rowBlocks.length-1) - (index+i)].getId());
					}
				}
			}
		}
		return deletedBlockIds;
	}
	
	/**
	 * Delete line of blocks for left diagonal direction. <br>
	 * 
	 * @param matrix Matrix form of the board blocks
	 * @param word		Word to search
	 * @param order		1  : Right order of {@code word} is allowed. <br>
	 * 					-1 : Inverse order of {@code word} is allowed. <br>
	 * 					0  : Both are allowed. <br>
	 * @return Set of deleted block id
	 */
	public static Set<Integer> deleteLeftDiagonalWordLine(
			final Block[][] matrix, final String word, final int order) {
		Set<Integer> deletedBlockIds = new HashSet<Integer>();
		
		for (int row = word.length()-1; row < matrix.length; row++) {
			// Maximum cells for the diagonal row
			final int maxCol = Math.min(matrix[row].length, row+1);
			// Create a sequence of characters of this diagonal row
			String rowWordSequence = new String();
			for (int col = 0; col < maxCol; col++) {
				rowWordSequence += convertBlockString(matrix[row-col][col]);
			}
			
			if (order >= 0) {
				// Search from start to end for the word
				Set<Integer> indices = searchWord(rowWordSequence, word);
				for (int index : indices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(matrix[row-(index+i)][index+i].getId());
					}
				}
			}
			if (order <= 0) {
				// Search from end to start for the word (inversely)
				String reversedSequence = new StringBuilder(rowWordSequence).reverse().toString();
				Set<Integer> inverseIndices = searchWord(reversedSequence, word);
				for (int index : inverseIndices) {
					for (int i = 0; i < word.length(); i++) {
						final int deleteRow = (row-(maxCol-1))+(index+i);
						final int deleteCol = (maxCol-1)-(index+i);
						deletedBlockIds.add(matrix[deleteRow][deleteCol].getId());
					}
				}
			}
		}
		return deletedBlockIds;
	}
	
	/**
	 * Delete line of blocks for left diagonal direction. <br>
	 * 
	 * @param matrix Matrix form of the board blocks
	 * @param word		Word to search
	 * @param order		1  : Right order of {@code word} is allowed. <br>
	 * 					-1 : Inverse order of {@code word} is allowed. <br>
	 * 					0  : Both are allowed. <br>
	 * @return Set of deleted block id
	 */
	public static Set<Integer> deleteRightDiagonalWordLine(
			final Block[][] matrix, final String word, final int order) {
		Set<Integer> deletedBlockIds = new HashSet<Integer>();
		
		for (int row = 0; row < matrix.length+1 - word.length(); row++) {
			// Maximum cells for the diagonal row
			final int maxCol = Math.min(matrix[row].length, matrix.length-row);
			// Create a sequence of characters of this diagonal row
			String rowWordSequence = new String();
			for (int col = 0; col < maxCol; col++) {
				rowWordSequence += convertBlockString(matrix[row+col][col]);
			}
			
			if (order >= 0) {
				// Search from start to end for the word
				Set<Integer> indices = searchWord(rowWordSequence, word);
				for (int index : indices) {
					for (int i = 0; i < word.length(); i++) {
						deletedBlockIds.add(matrix[row+(index+i)][index+i].getId());
					}
				}
			}
			if (order <= 0) {
				// Search from end to start for the word (inversely)
				String reversedSequence = new StringBuilder(rowWordSequence).reverse().toString();
				Set<Integer> inverseIndices = searchWord(reversedSequence, word);
				for (int index : inverseIndices) {
					for (int i = 0; i < word.length(); i++) {
						final int deleteRow = (row+(maxCol-1))-(index+i);
						final int deleteCol = (maxCol-1)-(index+i);
						deletedBlockIds.add(matrix[deleteRow][deleteCol].getId());
					}
				}
			}
		}
		return deletedBlockIds;
	}
	
	private static String convertBlockString(Block b) {
		if (b == null) {
			return NULL_OPTION;
		} else {
			// TODO
			// change here to operate special string
			return b.getCharacterString();
		}
	}
	
	/**
	 * Search {@code src} with {@code word} and return start indices of matched sequences
	 * 
	 * @param src		Source string to be searched
	 * @param target	Target word
	 * @return Start indices of matched sequences
	 */
	private static Set<Integer> searchWord(final String src, final String target) {
		int start = 0;
		Set<Integer> indices = new HashSet<Integer>();
		while (true) {
			start = src.indexOf(target, start);
			if (start == -1) {
				break;
			} else {
				indices.add(Integer.valueOf(start));
				start += target.length();
			}
		}
		return indices;
	}
}
