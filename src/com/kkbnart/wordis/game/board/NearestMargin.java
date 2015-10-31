package com.kkbnart.wordis.game.board;

import android.graphics.RectF;

import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.BlockSet;

/**
 * Find the nearest block or board boarder for the specified direction. <br>
 * 
 * @author kkbnart
 */
public class NearestMargin {
	
	/**
	 * Find the nearest boarder of block or board for the {@code dx} direction. <br>
	 * If {@code dx} is smaller than the nearest boarder, return dx. <br>
	 * In other cases, return the distance to the nearest boarder. <br>
	 * 
	 * @param board		Current board
	 * @param blockSet	Block set to move
	 * @param dx		Amount of move
	 * @return	Distance to nearest board : abs(dx) > abs(distance)
	 * 			dx : else
	 */
	public static float findNearestMarginX(final Board board, final BlockSet blockSet, final float dx) {
		final Block[][] matrix = board.getMatrixedBlocks();
		final RectF collisionRect = board.getCollisionRect();
		final boolean isPositive = dx > 0.f;
		float nearest = (isPositive ? 1.0f : -1.0f) * board.getCollisionRect().width();
		
		for (Block b : blockSet.getBlocks()) {
			// floaredCol : which column the left line of the block belongs to
			// ceiledCol  : which column the right line of the block belongs to
			final float blockCol = b.getX() - collisionRect.left;
			final int floaredCol = (int)(Math.floor(blockCol));
			final int ceiledCol  = (int)(Math.ceil(blockCol));
			
			// floaredRow : which row the upper line of the block belongs to
			// ceiledRow  : which row the bottom line of the block belongs to
			final float blockRow = (collisionRect.height()-1) - (b.getY() - collisionRect.top);
			final int floaredRow = (int)(Math.floor(blockRow));
			final int ceiledRow  = (int)(Math.ceil(blockRow));

			if (isPositive) {
				// nearest > 0.f
				float distance = collisionRect.width() - (blockCol+1);
				for (int i = ceiledCol+1; i < collisionRect.width(); i++) {
					if (matrix[floaredRow][i] != null || matrix[ceiledRow][i] != null) {
						distance = i - (blockCol+1);
						break;
					}
				}
				if (nearest > distance) {
					nearest = distance;
				}
			} else {
				// nearest < 0.f
				float distance = -blockCol;
				for (int i = floaredCol-1; i >= 0; i--) {
					if (matrix[floaredRow][i] != null || matrix[ceiledRow][i] != null) {
						distance = -((blockCol-1) - i);
						break;
					}
				}
				if (nearest < distance) {
					nearest = distance;
				}
			}
		}
		return Math.abs(dx) > Math.abs(nearest) ? nearest : dx;
	}
	
	/**
	 * Find the nearest boarder of block or board for the {@code dy} direction. <br>
	 * If {@code dy} is smaller than the nearest boarder, return dy. <br>
	 * In other cases, return the distance to the nearest boarder. <br>
	 * 
	 * @param board		Current board
	 * @param blockSet	Block set to move
	 * @param dy		Amount of move
	 * @return	Distance to nearest board : abs(dy) > abs(distance)
	 * 			dy : else
	 */
	public static float findNearestMarginY(final Board board, final BlockSet blockSet, final float dy) {
		final Block[][] matrix = board.getMatrixedBlocks();
		final RectF collisionRect = board.getCollisionRect();
		final boolean isPositive = dy > 0.f;
		float nearest = (isPositive ? 1.0f : -1.0f) * board.getCollisionRect().height();
		for (Block b : blockSet.getBlocks()) {
			// floaredRow : which row the upper line of the block belongs to
			// ceiledRow  : which row the bottom line of the block belongs to
			final float blockRow = (collisionRect.height()-1) - (b.getY() - collisionRect.top);
			final int floaredRow = (int)(Math.floor(blockRow));
			final int ceiledRow  = (int)(Math.ceil(blockRow));
			
			// floaredCol : which column the left line of the block belongs to
			// ceiledCol  : which column the right line of the block belongs to
			final float blockCol = b.getX() - collisionRect.left;
			final int floaredCol = (int)(Math.floor(blockCol));
			final int ceiledCol  = (int)(Math.ceil(blockCol));
			
			if (isPositive) {
				// nearest > 0.f
				float distance = blockRow;
				for (int i = floaredRow-1; i >= 0; i--) {
					if (matrix[i][ceiledCol] != null || matrix[i][floaredCol] != null) {
						distance = (blockRow-1) - i;
						System.out.println("sysout" + (distance - blockRow+1));
						break;
					}
				}
				if (nearest > distance) {
					nearest = distance;
				}
			} else {
				// nearest < 0.f
				float distance = -(collisionRect.height() - (b.getY()+1));
				for (int i = ceiledRow+1; i < collisionRect.height(); i++) {
					if (matrix[i][ceiledCol] != null || matrix[i][floaredCol] != null) {
						distance = -(i - (blockRow+1));
						System.out.println("sysout" + (distance - blockRow-1));
						break;
					}
				}
				if (nearest < distance) {
					nearest = distance;
				}
			}
		}
		return Math.abs(dy) > Math.abs(nearest) ? nearest : dy;
	}
}
