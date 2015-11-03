package com.kkbnart.wordis.game.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import com.kkbnart.wordis.game.GameState;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.NextBlocks;
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
	private HashMap<Integer, BlockFall> fallingBlocks = new LinkedHashMap<Integer, BlockFall>();
	
	public FreeFallAnimation(long animationTime, GameState priorAction) {
		super(animationTime, priorAction);
	}

	/**
	 * Set blocks falling below of which no block exists. <br>
	 * 
	 * @param board Current board
	 * @return 	true	: more than a block has been added to falling block list <br>
	 * 			false	: no blocks have been added
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
		if (ay != 0.f) {
			final int stepRow = ay > 0.f ? 1 : -1;
			final int startRow = ay > 0.f ? 0 : matrixedBlock.length-1;
			for (int col = 0; col < matrixedBlock[0].length; col++) {
				int row = startRow;
				int emptyCells = 0;
				for (int i = 0; i < matrixedBlock.length; i++) {
					final Block target = matrixedBlock[row][col];
					if (target == null) {
						// Count up empty cells in the row
						emptyCells++;
					} else if (emptyCells > 0) {
						final int id = target.getId();
						final BlockFall fall = new BlockFall(id);
						fall.setYFall(0.f, target.getY() + emptyCells*(ay > 0.f ? 1.f : -1.f));
						fallingBlocks.put(id, fall);
					}
					row += stepRow;
				}
			}
		}

		// Search for column
		if (ax != 0.f) {
			final int stepCol = ax > 0.f ? 1 : -1;
			final int startCol = ax > 0.f ? 0 : matrixedBlock[0].length-1;
			for (int row = 0; row < matrixedBlock.length; row++) {
				int col = startCol;
				int emptyCells = 0;
				for (int j = 0; j < matrixedBlock[row].length; j++) {
					final Block target = matrixedBlock[row][col];
					if (target == null) {
						// Count up empty cells in the row
						emptyCells++;
					} else {
						final int id = target.getId();
						// Duplication may occur while first search for row
						if (fallingBlocks.containsKey(id)) {
							fallingBlocks.get(id).setXFall(0.f, target.getX() + emptyCells*ax);
						} else if (emptyCells > 0) {
							final BlockFall fall = new BlockFall(id);
							fall.setXFall(0.f, target.getY() + emptyCells*(ax < 0.f ? 1.f : -1.f));
							fallingBlocks.put(id, fall);
						}
					}
					col += stepCol;
				}
			}
		}
	}

	@Override
	protected void drawAnimation(final Canvas canvas, final Board board, final NextBlocks nextBlocks, 
			final long elapsedTime, final long diffTime) {
		updateBlocks(board, diffTime);
		
		final Paint paint =  new Paint();
		board.draw(canvas, paint);
		nextBlocks.draw(canvas, paint, board);
	}
	
	/**
	 * Update falling speed of blocks. <br>
	 * 
	 * @param diffTime	Elapsed time from previous frame
	 */
	private void updateBlocks(final Board board, final long diffTime) {
		SparseArray<Block> blocks = board.getBlocks();
		
		ArrayList<Integer> removeIdList = new ArrayList<Integer>();
		for (Entry<Integer, BlockFall> entry : fallingBlocks.entrySet()) {
			final int id = entry.getKey();
			final BlockFall blockFall = entry.getValue();
			
			// Update fall speed
			blockFall.updateFallSpeed(
					blockFall.getXSpeed() + Fall.getInstance().getAXPerMSec() * diffTime,
					blockFall.getYSpeed() + Fall.getInstance().getAYPerMSec() * diffTime);

			final float vx = blockFall.getXSpeed();
			final float targetx = blockFall.getXTarget();
			final float vy = blockFall.getYSpeed();
			final float targety = blockFall.getYTarget();
			
			final Block block = blocks.get(id);
			
			if (blockFall.getXEnabled()) {
				float newX = FloatRound.round(block.getX() + vx);
				// newX is coordinated not to be over target x
				if (vx > 0.f) {
					newX = Math.min(newX, targetx);
				} else {
					newX = Math.max(newX, targetx);
				}
				block.setX(newX);
			}
			
			if (blockFall.getYEnabled()) {
				float newY = FloatRound.round(block.getY() + vy);
				// newX is coordinated not to be over target x
				if (vy > 0.f) {
					newY = Math.min(newY, targety);
				} else {
					newY = Math.max(newY, targety);
				}
				block.setY(newY);
			}
			
			clearFallFlag(block, blockFall);
			// If both x and y flag are cleared, remove the block from falling block list.
			if (!blockFall.getXEnabled() && !blockFall.getYEnabled()) {
				removeIdList.add(id);
			}
		}
		
		// Remove block fall id from list
		for (int i : removeIdList) {
			fallingBlocks.remove(i);
		}
	}
	
	private void clearFallFlag(final Block block, final BlockFall blockFall) {
		// Clear flag if reached to target
		if (FloatRound.equals(block.getX(), blockFall.getXTarget())) {
			blockFall.setXEnabled(false);
		}
		if (FloatRound.equals(block.getY(), blockFall.getYTarget())) {
			blockFall.setYEnabled(false);
		}
	}
	
	@Override
	protected boolean isAnimationContinue() {
		return !fallingBlocks.isEmpty();
	}

}
