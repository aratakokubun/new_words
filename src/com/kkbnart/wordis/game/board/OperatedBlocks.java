package com.kkbnart.wordis.game.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockSet;
import com.kkbnart.wordis.game.object.Collision;
import com.kkbnart.wordis.game.rule.FreeFall;

/**
 * Blocks which are on operation. <br>
 * All update methods {@link #autoUpdate()}, {@link #operate(Board, boolean)}, {@link #operate(Board, float, float)} are synchronized <br>
 * to avoid fetching value while other method is changing it. <br> 
 * 
 * @author kkbnart
 */
public class OperatedBlocks extends BlockSet {
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
		setRoot(rootX, rootY);
	}
	
	public void setRoot(final float rootX, final float rootY) {
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
	 * Update block position at each frame automatically. <br>
	 */
	public synchronized void autoUpdate() {
		update(FreeFall.getInstance().getXPerFrame(), FreeFall.getInstance().getYPerFrame());
	}
	
	/**
	 * Move blocks with dx and dy. <br>
	 * If dx or dy is larger than the nearest boarder, fix them with {@link NearestMargin} in this method. <br>
	 * 
	 * @param board	Current board
	 * @param dx 	Amount of move x	
	 * @param dy	Amount of move y
	 */
	public synchronized void operate(final Board board, float dx, float dy) {
		dx = NearestMargin.findNearestMarginX(board, this, dx);
		dy = NearestMargin.findNearestMarginY(board, this, dy);
		
		// Try for (dx, dy) move
		BlockSet test = testUpdate(dx, dy);
		if (!Collision.isCollided(board, test)) {
			update(dx, dy);
			return;
		}
		// Try for (dx, 0) move
		test = testUpdate(dx, 0);
		if (!Collision.isCollided(board, test)) {
			update(dx, 0);
			return;
		}
		
		// Try for (0, dy)
		test = testUpdate(0, dy);
		if (!Collision.isCollided(board, test)) {
			update(0, dy);
		}
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
