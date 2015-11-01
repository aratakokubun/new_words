package com.kkbnart.wordis.game.object.block;

import java.util.ArrayList;

import com.kkbnart.wordis.game.util.FloatRound;

/**
 * Set of operated blocks by players <br>
 * 
 * @author kkbnart
 */
public class BlockSet {
	// private static final String TAG = BlockSet.class.getSimpleName();
	
	// List of all blocks
	private ArrayList<Block> blocks = new ArrayList<Block>();
	// Rotational center block index
	private int center;
	
	/**
	 * Constructor with specifying all blocks and rotational center index. <br>
	 * 
	 * @param blocks
	 * @param center
	 */
	public BlockSet(final ArrayList<Block> blocks, final int center) {
		this.blocks = blocks;
		this.center = center;
	}
	
	public BlockSet() {
		// Do nothing
	}

	/**
	 * Copy content of another block set to this. <br>
	 * 
	 * @param blockSet Copy source of block set
	 */
	public void copy(final BlockSet blockSet) {
		this.blocks = blockSet.blocks;
		this.center = blockSet.center;
	}
	
	/**
	 * Get all blocks. <br>
	 * 
	 * @return All blocks array list
	 */
	public ArrayList<Block> getBlocks()	{
		return blocks;
	}

	/**
	 * Move blocks to the position at which the center block is (x, y). <br>
	 * 
	 * @param x	Absolute position x
	 * @param y	Absolute position y
	 */
	public void updateAbsolute(final float x, final float y) {
		final float cx = blocks.get(center).getX();
		final float cy = blocks.get(center).getY();
		for (Block b : blocks) {
			final float newX = FloatRound.round(x + b.getX() - cx);
			final float newY = FloatRound.round(y + b.getY() - cy);
			b.setX(newX); // x + (bx -cx)
			b.setY(newY); // y + (by -cy)
		}
	}
	
	/**
	 * Move blocks to x and y. <br>
	 * 
	 * @param dx	Difference from previous position x
	 * @param dy	Difference from previous position y
	 */
	public void update(final float dx, final float dy) {
		for (Block b : blocks) {
			final float newX = FloatRound.round(b.getX() + dx);
			final float newY = FloatRound.round(b.getY() + dy);
			b.setX(newX);
			b.setY(newY);
		}
	}
	
	/**
	 * Rotate blocks clockwise or counter clockwise. <br>
	 * 
	 * @param clockWise cw or ccw
	 */
	public void update(final boolean clockWise) {
		final float cx = blocks.get(center).getX();
		final float cy = blocks.get(center).getY();
		for (Block b : blocks) {
			/*
			 * Rotate around (cx, cy)
			 * |cosa -sina||dx| + |cx|
			 * |sina  cosa||dy| + |cy|
			 */
			final float newX = FloatRound.round((clockWise ? 1.f : -1.f) * (b.getY() - cy) + cx);
			final float newY = FloatRound.round((clockWise ? -1.f : 1.f) * (b.getX() - cx) + cy);
			b.setX(newX);
			b.setY(newY);
		}
	}
	
	/**
	 * Check if the blocks can be moved, move cloned block set. <br>
	 * This method return cloned block set which has been updated with (dx, dy). <br>
	 * 
	 * @param dx	Difference from previous position x
	 * @param dy	Difference from previous position y
	 * @return Copied block set
	 */
	public BlockSet testUpdate(final float dx, final float dy) {
		BlockSet cloned = clone();
		cloned.update(dx, dy);
		return cloned;
	}
	
	/**
	 * Check if the blocks can be rotated, rotate cloned block set. <br>
	 * This method return cloned block set which has been updated with (clockWise). <br>
	 * 
	 * @param clockWise cw or ccw
	 * @return Copied block set
	 */
	public BlockSet testUpdate(final boolean clockWise) {
		BlockSet cloned = clone();
		cloned.update(clockWise);
		return cloned;
	}
	
	/**
	 * Get difference between this and around cells. <br>
	 * Around cells are defined as exact position of cells (do not include number under point(.) <br>
	 * within +-1 dx or +-1 dy. Note that dx = 0 or dy = 0 must be met. <br>
	 * 
	 * @return (dx, dy) pairs of around cells
	 */
	public float[][] getDiffAroundCells() {
		final float x = blocks.get(center).getX(), y = blocks.get(center).getY();
		return new float[][] {
				{ 0.f, 0.f },
				{ FloatRound.round(Math.round(x - 1.f) - x), 0.f },
				{ FloatRound.round(Math.round(x + 1.f) - x), 0.f },
				{ 0.f, FloatRound.round(Math.round(y - 1.f) - y) },
				{ 0.f, FloatRound.round(Math.round(y + 1.f) - y) } };
	}
	
	/**
	 * Clone block set. <br>
	 */
	public BlockSet clone() {
		ArrayList<Block> clonedArray = new ArrayList<Block>();
		for (Block b : blocks) {
			clonedArray.add(b.clone());
		}
		BlockSet cloned = new BlockSet(clonedArray, center);
		return cloned;
	}
}
