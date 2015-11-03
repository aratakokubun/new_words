package com.kkbnart.wordis.game.object.block;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.character.Character;

/**
 * State of blocks
 * @author kkbnart
 */
public class Block {
	// Id of the block
	private int id;
	// Color of the block
	private BlockColor color;
	// Character (a, b, c, ...) of the block
	private Character character;
	// Column and row position of the block
	private float x, y;
	
	/**
	 * Constructor with specifying color and character of the block. <br>
	 * 
	 * @param color 	Color of the block selected from BlockColorSet
	 * @param character	Character of the block selected from CharacterSet
	 */
	public Block(final BlockColor color, final Character character) {
		this.color = color;
		this.character = character;
	}
	
	public Block(final BlockColor color, final Character character, final int id, final float x, final float y) {
		this.color = color;
		this.character = character;
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void setId(final int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setX(final float x) {
		this.x = x;
	}
	
	public void setY(final float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public RectF getRect() {
		return new RectF(x, y, x+1.f, y+1.f);
	}
	
	public String getCharacterString() {
		return character.getLetter();
	}
	
	/**
	 * Draw bitmap image of the block.<br>
	 * 
	 * @param canvas	Canvas of surface view
	 * @param paint		Paint of surface view
	 * @param board		Game board
	 */
	public void drawImage(final Canvas canvas, final Paint paint, final Board board) {
		// Draw block image
		drawImage(canvas, paint, color.getImage(), board);
		// Draw character image
		drawImage(canvas, paint, character.getImage(), board);
	}
	
	private void drawImage(final Canvas canvas, final Paint paint, final Bitmap bitmap, final Board board) {
		final int viewX = (int) (x * board.getXStep()) + board.getX();
		final int viewY = (int) (y * board.getYStep()) + board.getY();
		canvas.drawBitmap(
				bitmap,
				new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				new Rect(viewX, viewY, (int) viewX + board.getXStep(),
						(int) viewY + board.getYStep()), paint);
	}
	
	/**
	 * Draw bitmap image of the block with specified transparency and magnificient rate.<br>
	 * 
	 * @param canvas	Canvas of surface view
	 * @param paint		Paint of surface view
	 * @param board		Game board
	 * @param mag
	 */
	public void drawImage(final Canvas canvas, final Paint paint,
			final Board board, final float mag) {
		// Draw block image
		drawImage(canvas, paint, color.getImage(), board, mag);
		// Draw character image
		drawImage(canvas, paint, character.getImage(), board, mag);
	}
	
	private void drawImage(final Canvas canvas, final Paint paint, final Bitmap bitmap,
			final Board board, final float mag) {
		final int width  = (int)(board.getXStep() * mag);
		final int height = (int)(board.getYStep() * mag);
		final int viewX  = (int) (x * board.getXStep()) + board.getX() - (width - board.getXStep())/2;
		final int viewY  = (int) (y * board.getYStep()) + board.getY() - (height - board.getYStep())/2;
		
		canvas.drawBitmap(
				bitmap,
				new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				new Rect(viewX, viewY, (int) viewX + width, (int) viewY + height), paint);
	}
	
	public Block clone() {
		Block cloned = new Block(color, character, id, x, y);
		return cloned;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Block) {
			return id == ((Block)other).id;
		} else {
			return false;
		}
	}
}
