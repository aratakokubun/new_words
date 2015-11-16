package com.kkbnart.wordis.game.object.block;

import java.util.ArrayDeque;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.game.object.board.Board;
import com.kkbnart.wordis.game.player.WordisPlayer;


/**
 * Block sets waiting for operation <br>
 * 
 * @author kkbnart
 */
public class NextBlocks extends ArrayDeque<BlockSet> {
	private static final long serialVersionUID = 1L;
	
	// Specify maximum number of next block sets
	private static final int MAX_NUM = 2;
	
	// Root position of the next candidates
	private float rootX, rootY;
	// Margin between next blocks
	private float marginX, marginY;
	// Factory to create new block sets
	private BlockSetBuffer buffer = null;
	
	/**
	 * Constructor with specifying factory
	 * 
	 * @param rootX		Original point for next block x
	 * @param rootY		Original point for next block y
	 * @param marginX	X margin between n next and n+1 next blocks
	 * @param marginY	Y margin between n next and n+1 next blocks
	 */
	public NextBlocks(final float rootX, final float rootY,
			final float marginX, final float marginY) {
		this.rootX = rootX;
		this.rootY = rootY;
		this.marginX = marginX;
		this.marginY = marginY;
	}
	
	public void setBlockSetBuffer(final BlockSetBuffer buffer) {
		this.buffer = buffer;
	}
	
	public void setCoordinate(final float rootX, final float rootY, final float marginX, final float marginY) {
		this.rootX = rootX;
		this.rootY = rootY;
		this.marginX = marginX;
		this.marginY = marginY;
	}
	
	/**
	 * Initialize blocks with stuck size {@code num} <br>
	 * 
	 * @param num Stuck size
	 * @throws BlockCreateException Can not create block
	 */
	public void initializeBlockSet(final WordisPlayer player, int num) throws BlockCreateException {
		num = Math.max(num, MAX_NUM);
		if (buffer == null) {
			throw new BlockCreateException();
		} else {
			this.clear();
			for (int i = 0; i < num; i++) {
				addLast(buffer.requestBlockSet(player));
			}
		}
	}
	
	/**
	 * Push new block set, update positions of them and pop first block set. <br>
	 * 
	 * @return First stuck element of next block set
	 * @throws BlockCreateException Can not create block
	 */
	public BlockSet releaseNextBlocks(final WordisPlayer player) throws BlockCreateException {
		if (buffer == null) {
			throw new BlockCreateException();
		} else {
			addLast(buffer.requestBlockSet(player));
			updatePosition();
			return pop();
		}
	}
	
	/**
	 * Update position when push and pop block set. <br>
	 */
	private void updatePosition() {
		Iterator<BlockSet> it = iterator();
		int i = 0;
		while (it.hasNext()) {
			BlockSet bs = it.next();
			if (i > 0) {
				final float x = rootX + (i-1)*marginX;
				final float y = rootY + (i-1)*marginY;
				bs.updateAbsolute(x, y);
			}
			i++;
		}
	}
		
	/**
	 * Draw block images. <br>
	 * 
	 * @param canvas	Canvas of surface view
	 * @param paint		Paint of surface view
	 */
	public void draw(final Canvas canvas, final Paint paint, final Board board) {
		for (BlockSet bs : this) {
			for (Block b : bs.getBlocks()) {
				b.drawImage(canvas, paint, board);
			}
		}
	}
}
