package com.kkbnart.wordis.game.board;

import android.graphics.RectF;

import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockSet;

/**
 * Find the nearest block or board boarder for the specified direction. <br>
 * 
 * @author kkbnart
 */
public class NearestMargin {

	// TODO
	// Refactor these methods!
	// Too difficult to understand
	
	public static float findNearestMarginX(final Board board, final BlockSet blockSet, final float dx) {
		final Block[][] matrix = board.getMatrixedBlocks();
		final RectF collisionRect = board.getCollisionRect();
		float nearest = (dx > 0.f ? 1.0f : -1.0f) * board.getCollisionRect().width();
		for (Block b : blockSet.getBlocks()) {
			// floaredCol : which column the left line of the block belongs to
			// ceiledCol  : which column the right line of the block belongs to
			final float blockCol = b.getX() - collisionRect.left;
			final int floaredCol = (int)(Math.floor(blockCol));
			final int ceiledCol  = (int)(Math.ceil(blockCol));
			System.out.println(":"+b.getX() + ", ceiled:"+ceiledCol + ", floared:"+floaredCol);
			
			// floaredRow : which row the upper line of the block belongs to
			// ceiledRow  : which row the bottom line of the block belongs to
			final float blockRow = (collisionRect.height()-1) - (b.getY() - collisionRect.top);
			final int floaredRow = (int)(Math.floor(blockRow));
			final int ceiledRow  = (int)(Math.ceil(blockRow));
			System.out.println("y:"+b.getY() + ", ceiled:"+ceiledRow + ", floared:"+floaredRow);

			if (dx > 0.f) {
				float distance = collisionRect.width() - (blockCol+1);
				for (int i = ceiledCol+1; i < collisionRect.width(); i++) {
					if (matrix[floaredRow][i] != null || matrix[ceiledRow][i] != null) {
						distance = i - (blockCol+1);
						break;
					}
				}
				// nearest > 0.f
				if (nearest > distance) {
					nearest = distance;
				}
			} else {
				float distance = -blockCol;
				for (int i = floaredCol-1; i >= 0; i--) {
					if (matrix[floaredRow][i] != null || matrix[ceiledRow][i] != null) {
						distance = -((blockCol-1) - i);
						break;
					}
				}
				// nearest < 0.f
				if (nearest < distance) {
					nearest = distance;
				}
				System.out.println(nearest);
			}
		}
		return Math.abs(dx) > Math.abs(nearest) ? nearest : dx;
	}
	
	public static float findNearestMarginY(final Board board, final BlockSet blockSet, final float dy) {
		final Block[][] matrix = board.getMatrixedBlocks();
		final RectF collisionRect = board.getCollisionRect();
		float nearest = (dy > 0.f ? 1.0f : -1.0f) * board.getCollisionRect().height();
		for (Block b : blockSet.getBlocks()) {
			// floaredRow : which row the upper line of the block belongs to
			// ceiledRow  : which row the bottom line of the block belongs to
			final float blockRow = (collisionRect.height()-1) - (b.getY() - collisionRect.top);
			final int floaredRow = (int)(Math.floor(blockRow));
			final int ceiledRow  = (int)(Math.ceil(blockRow));
			System.out.println("y:"+b.getY() + ", ceiled:"+ceiledRow + ", floared:"+floaredRow);
			
			// floaredCol : which column the left line of the block belongs to
			// ceiledCol  : which column the right line of the block belongs to
			final float blockCol = b.getX() - collisionRect.left;
			final int floaredCol = (int)(Math.floor(blockCol));
			final int ceiledCol  = (int)(Math.ceil(blockCol));
			System.out.println(":"+b.getX() + ", ceiled:"+ceiledCol + ", floared:"+floaredCol);
			
			if (dy > 0.f) {
				float distance = blockRow;
				for (int i = floaredRow-1; i >= 0; i--) {
					if (matrix[i][ceiledCol] != null || matrix[i][floaredCol] != null) {
						distance = (blockRow-1) - i;
						break;
					}
				}
				// nearest > 0.f
				if (nearest > distance) {
					nearest = distance;
				}
			} else {
				float distance = -(collisionRect.height() - (b.getY()+1));
				for (int i = ceiledRow+1; i < collisionRect.height(); i++) {
					if (matrix[i][ceiledCol] != null || matrix[i][floaredCol] != null) {
						distance = -(i - (blockRow+1));
						break;
					}
				}
				// nearest < 0.f
				if (nearest < distance) {
					nearest = distance;
				}
			}
		}
		return Math.abs(dy) > Math.abs(nearest) ? nearest : dy;
	}
}
