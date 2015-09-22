package com.kkbnart.wordis.game.board;

import java.util.ArrayDeque;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.game.exception.BlockCreateException;
import com.kkbnart.wordis.game.object.Block;
import com.kkbnart.wordis.game.object.BlockSet;
import com.kkbnart.wordis.game.object.BlockSetFactory;


/**
 * Block sets waiting for operation <br>
 * 
 * @author kkbnart
 */
public class NextBlocks extends ArrayDeque<BlockSet> {
	private static final long serialVersionUID = 1L;
	// Root position of the next candidates
	private float rootX, rootY;
	// Margin between next blocks
	private float marginX, marginY;
	// Factory to create new block sets
	private BlockSetFactory factory = null;
	
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
	
	public void setFactory(final BlockSetFactory factory) {
		this.factory = factory;
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
	public void initializeBlockSet(final int num) throws BlockCreateException {
		if (factory == null) {
			throw new BlockCreateException();
		} else {
			this.clear();
			for (int i = 0; i < num; i++) {
				addLast(factory.create());
			}
		}
	}
	
	/**
	 * Push new block set, update positions of them and pop first block set. <br>
	 * 
	 * @return First stuck element of next block set
	 * @throws BlockCreateException Can not create block
	 */
	public BlockSet releaseNextBlocks() throws BlockCreateException {
		if (factory == null) {
			throw new BlockCreateException();
		} else {
			addLast(factory.create());
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
