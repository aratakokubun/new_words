package com.kkbnart.wordis.game.animation;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import com.kkbnart.wordis.game.GameStatus;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.rule.Fall;
import com.kkbnart.wordis.game.util.FloatRound;

/**
 * Manage animation of falling blocks. <br>
 * 
 * @author kkbnart
 *
 */
public class FreeFallAnimation extends GameAnimation {
	// Map for block id and current falling speed
	// Because the order of the list is very import, use linked hash map.
	private HashMap<Integer, BlockFallSpeed> fallingBlocks = new LinkedHashMap<Integer, BlockFallSpeed>();
	
	public FreeFallAnimation(long animationTime, GameStatus priorAction) {
		super(animationTime, priorAction);
	}

	/**
	 * Set blocks falling below of which no block exists. <br>
	 * 
	 * @param board Current board
	 */
	public void setFallingBlocks(final Board board) {
		fallingBlocks.clear();
		
		// Search for row
		// 1. If ay > 0, search from bottom, else from top
		// 2. Search for null block in the column
		// 3. If null block is found, add blocks to falling block list above(ay>0) or below(ay<0) of the null
		// Search for column
		// 1. If ax > 0, search from right, else from left
		// 2. Search for null block in the row
		// 3. If null block is found, add blocks to falling block list left(ax>0) or right(ax<0) side of the null
		
		final Block[][] matrixedBlock = board.getMatrixedBlocks();
		final float ax = Fall.getInstance().getAXPerMSec(), ay = Fall.getInstance().getAYPerMSec();
		
		// Search for row
		final int stepRow = ay < 0.f ? 1 : -1;
		final int startRow = ay < 0.f ? 0 : matrixedBlock.length-1;
		for (int j = 0; j < matrixedBlock[0].length; j++) {
			int row = startRow;
			for (int i = 0; i < matrixedBlock.length; i++) {
				if (matrixedBlock[row][j] == null) {
					// Add blocks to falling block list from i+1 to matrixedBlock.length
					for (int k = i+1; k < matrixedBlock.length; k++) {
						row += stepRow;
						final Block target = matrixedBlock[row][j];
						if (target != null) {
							final int id = target.getId();
							final BlockFallSpeed speed = new BlockFallSpeed(id, false, true);
							// No duplication occurs while first search for row
							fallingBlocks.put(id, speed);
						}
					}
					break;
				}
				row += stepRow;
			}
		}

		// Search for column
		final int stepCol = ax > 0.f ? 1 : -1;
		final int startCol = ax > 0.f ? 0 : matrixedBlock[0].length-1;
		for (int i = 0; i < matrixedBlock.length; i++) {
			int col = startCol;
			for (int j = 0; j < matrixedBlock[i].length; j++) {
				if (matrixedBlock[i][col] == null) {
					// Add blocks to falling block list from j+1 to matrixedBlock[i].length
					for (int k = j+1; k < matrixedBlock[i].length; k++) {
						col += stepCol;
						final Block target = matrixedBlock[i][col];
						if (target != null) {
							final int id = target.getId();
							// Duplication may occur while first search for row
							if (fallingBlocks.containsKey(id)) {
								fallingBlocks.get(id).setFallXEnabled(true);
							} else {
								final BlockFallSpeed speed = new BlockFallSpeed(id, true, false);
								fallingBlocks.put(id, speed);
							}
						}
					}
					break;
				}
				col += stepCol;
			}
		}
	}

	@Override
	protected void drawAnimation(final Canvas canvas, final Board board, final long elapsedTime,
			final long diffTime) {
		updateBlocks(board, diffTime);
		final Paint paint =  new Paint();
		board.draw(canvas, paint);
	}
	
	/**
	 * Update falling speed of blocks. <br>
	 * 
	 * @param diffTime	Elapsed time from previous frame
	 */
	private void updateBlocks(final Board board, final long diffTime) {
		SparseArray<Block> blocks = board.getBlocks();
		for (BlockFallSpeed speed : fallingBlocks.values()) {
			final int id = speed.getId();
			
			// Update fall speed
			speed.updateFallSpeed(Fall.getInstance().getAXPerMSec()*diffTime, Fall.getInstance().getAYPerMSec()*diffTime);

			final Block test = blocks.get(id).clone();
			
			// Test update x
			final float newX = FloatRound.round(test.getX() + speed.getFallXSpeed());
			test.setX(newX);
			// Check intersect with other blocks
			for (int i = 0; i < blocks.size(); i++) {
				final Block other = blocks.get(i);
				// If test block intersects with other blocks, finish falling.
				if (other.getId() != id && other.getRect().intersect(test.getRect())) {
					clearFallFlag(id, true);
					break;
				}
			}
			// Not intersected, update block position
			blocks.get(id).setX(newX);			
			
			// Test update y
			final float newY = FloatRound.round(test.getY() + speed.getFallYSpeed());
			test.setY(newY);
			// Check intersect with other blocks
			for (int i = 0; i < blocks.size(); i++) {
				final Block other = blocks.get(i);
				// If test block intersects with other blocks, finish falling.
				if (other.getId() != id && other.getRect().intersect(test.getRect())) {
					clearFallFlag(id, false);
					break;
				}
			}
			// Not intersected, update block position
			blocks.get(id).setY(newY);
		}
	}
	
	private void clearFallFlag(final int id, final boolean isClearX) {
		final BlockFallSpeed speed = fallingBlocks.get(id); 
		// Clear flag
		if (isClearX) {
			speed.setFallXEnabled(false);
		} else {
			speed.setFallYEnabled(false);
		}
		// If both x and y flag are cleared, remove the block from falling block list.
		if (!speed.getFallXEnabled() && !speed.getFallYEnabled()) {
			fallingBlocks.remove(id);
		}
	}
	
	@Override
	protected boolean isAnimationContinue() {
		System.out.println("hogehogehoge");
		return !fallingBlocks.isEmpty();
	}

}
