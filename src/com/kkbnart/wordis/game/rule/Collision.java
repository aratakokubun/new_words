package com.kkbnart.wordis.game.rule;

import android.graphics.RectF;
import android.util.SparseArray;

import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.BlockSet;
import com.kkbnart.wordis.game.object.board.Board;

/**
 * Static class to judge collision
 * @author kkbnart
 */
public class Collision {
	/**
	 * Judge if the blocks in the board and the blocks in block set are intersected each other
	 * @param board 	Board of the game
	 * @param blockSet	Operated blocks
	 * @return Is collided or not
	 */
	public static boolean isCollided(final Board board, final BlockSet blockSet) {
		// collision with wall of the board
		for (Block b1 : blockSet.getBlocks()) {
			// Collided with board blocks
			SparseArray<Block> boardBlocks = board.getBlocks();
			for (int i = 0 ; i < boardBlocks.size(); i++) {
				Block b2 = boardBlocks.get(boardBlocks.keyAt(i));
				if (RectF.intersects(b1.getRect(), b2.getRect())) {
					return true;
				}
			}
			// Collided with board walls
			if (!board.getCollisionRect().contains(b1.getRect())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Judge if the blocks in the board and the blocks in block set are contacted each other
	 * @param board 	Board of the game
	 * @param blockSet	Operated blocks
	 * @return Is contacted or not
	 */
	public static boolean isContacted(final Board board, final BlockSet blockSet) {
		for (Block b1 : blockSet.getBlocks()) {
			// Contacted with blocks
			SparseArray<Block> boardBlocks = board.getBlocks();
			for (int i = 0 ; i < boardBlocks.size(); i++) {
				Block b2 = boardBlocks.get(boardBlocks.keyAt(i));
				if (isContacted(board, b1, b2)) {
					return true;
				}
			}
			// Contacted with walls
			if (isContacted(board, b1)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Judge if two blocks are contacted each other
	 * @param board Board of the game
	 * @param b1	Moving block
	 * @param b2	Stable block
	 * @return Is contacted or not
	 */
	private static boolean isContacted(final Board board, final Block b1, final Block b2) {
		final float moveX = Fall.getInstance().getXPerMSec(), moveY = Fall.getInstance().getYPerMSec();
		// Contacted with x side
		if (Math.abs(moveX) > 0.f) {
			if (   Math.abs(b1.getX()-b2.getX()) < Math.abs(moveX) + 1.f
				&& Math.abs(b1.getY()-b2.getY()) <= Math.abs(moveY)
				&& (b1.getX()-b2.getX())*moveX < 0.f) {
				// continue
			} else {
				return false;
			}
		}
		// Contacted with y side
		if (Math.abs(moveY) > 0.f) {
			if (   Math.abs(b1.getX()-b2.getX()) <= Math.abs(moveX)
				&& Math.abs(b1.getY()-b2.getY()) < Math.abs(moveY) + 1.f
				&& (b1.getY()-b2.getY())*moveY < 0.f) {
				// continue
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Judge if board and block are contacted each other
	 * @param board Board of the game
	 * @param b		Moving block
	 * @return Is contacted or not
	 */
	private static boolean isContacted(final Board board, final Block b) {
		final float moveX = Fall.getInstance().getXPerMSec(), moveY = Fall.getInstance().getYPerMSec();
		final RectF rect = board.getCollisionRect();
		return (moveX < 0.f && Math.abs(rect.left-b.getX()) 	< Math.abs(moveX))
			|| (moveY < 0.f && Math.abs(rect.top-b.getY()) 		< Math.abs(moveY))
			|| (moveX > 0.f && Math.abs(rect.right-b.getX()) 	< Math.abs(moveX) + 1.f )
			|| (moveY > 0.f && Math.abs(rect.bottom-b.getY()) 	< Math.abs(moveY) + 1.f );
	}
}
