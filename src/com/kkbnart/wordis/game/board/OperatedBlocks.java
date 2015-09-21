package com.kkbnart.wordis.game.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockSet;
import com.kkbnart.wordis.game.object.Collision;
import com.kkbnart.wordis.game.rule.FreeFall;

/**
 * Blocks which are on operation
 * @author kkbnart
 */
public class OperatedBlocks extends BlockSet {
	// private static final String TAG = OperatedBlocks.class.getSimpleName();
	
	// Root position of the operated blocks
	private float rootX, rootY;
	
	/**
	 * Constructor with specifying root position of operated block. <br>
	 * 
	 * @param rootX Original x position of the block set
	 * @param rootY Original y position of the block set
	 */
	public OperatedBlocks(final float rootX, final float rootY) {
		super();
		this.rootX = rootX;
		this.rootY = rootY;
	}
	
	/**
	 * Set new block set and move to root position. <br>
	 * 
	 * @param blocks Blocks to set operable
	 */
	public void setBlocks(final BlockSet blockSet) {
		copy(blockSet);
		updateAbsolute(rootX, rootY);
	}
	
	/**
	 * Automatical update of the position <br>
	 */
	public synchronized void autoUpdate() {
		update(FreeFall.getInstance().getXPerFrame(), FreeFall.getInstance().getYPerFrame());
	}
	
	/**
	 * Move blocks with dx and dy. <br>
	 * 
	 * @param board	Current board
	 * @param dx 	Amount of move x	
	 * @param dy	Amount of move y
	 */
	public synchronized void operate(final Board board, final int dx, final int dy) {
		BlockSet test = testUpdate(dx, 0);
		boolean checkDx = Collision.isCollided(board, test);
		test = testUpdate(0, dy);
		boolean checkDy = Collision.isCollided(board, test);
		test = null;
		
		update(checkDx ? 0 : dx, checkDy ? 0 : dy);
	}

	/**
	 * Rotate blocks clockwise or counter. <br>
	 * <p>
	 * Blocks can be moved before rotation if the blocks can not be rotated in their original position. <br>
	 * If that case, the cells which blocks can be moved is defined with their around cells.
	 * </p>
	 * 
	 * @param board		Current board
	 * @param clockWise	Is clockwise direction or not
	 * @see com.kkbnart.wordis.game.object.BlockSet#getDiffAroundCells
	 */
	public synchronized void operate(final Board board, final boolean clockWise) {
		// FIXME
		// 凸型で左から1マスでその場回転ができないときがある
		BlockSet test;
		for (float[] diff : getDiffAroundCells()) {
			test = testUpdate(clockWise);
			test.update(diff[0], diff[1]);
			// If no collision detected, test state can be adopted to new state
			if (!Collision.isCollided(board, test)) {
				copy(test);
				return;
			}
		}
	}
		
	/**
	 * Draw block images <br>
	 * 
	 * @param canvas	Canvas of surface view
	 * @param paint		Paint of surface view
	 */
	public void draw(final Canvas canvas, final Paint paint, final Board board) {
		for (Block b : getBlocks()) {
			b.drawImage(canvas, paint, board);
		}
	}
}
