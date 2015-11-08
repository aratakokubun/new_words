package com.kkbnart.wordis.game.object.board;

import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;

import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.BlockSet;

/**
 * Board to contain all settled blocks. <br>
 * 
 * @author kkbnart
 */
public class Board {
	// Pixel x and y coordinates of the board
	private int x, y;
	// Pixel width and height of the board
	private int width, height;
	// Matrix size of the board, with row(y) and column(x)
	private int column, row;
	// Matrix size of collision of the board
	private int collisionX, collisionY;
	private int collisionColumn, collisionRow;
	// Blocks which the board contains
	private SparseArray<Block> blocks = new SparseArray<Block>();
	
	// Stack position for over flow
	private int stackCol, stackRow;
	
	/**
	 * Constructor with specifying area, size and blocks. <br>
	 * 
	 * @param x					Left line of the board
	 * @param y					Upper line of the board
	 * @param width 			Width of the board
	 * @param height			Height of the board
	 * @param column			Column number of the board matrix
	 * @param row				Row number of the board matrix
	 * @param collisionX		Left line of the collision of the board
	 * @param collisionY		Upper line of the collision of the board
	 * @param collisionColumn	Column number of the collision of the board
	 * @param collisionRow		Row number of the collision of the board
	 * @param stackCellX		X position of stacking cell
	 * @param stackCellY		Y position of stacking cell
	 * @throws InvalidParameterException	Invalid parameters are specified
	 */
	public Board(final int x, final int y, final int width, final int height,
			final int column, final int row, final int collisionX, final int collisionY,
			final int collisionColumn, final int collisionRow, final float stackCellX, final float stackCellY)
			throws InvalidParameterException {
		this.updateBoardArea(x, y, width, height);
		this.updateBoardSize(column, row, collisionX, collisionY, collisionColumn, collisionRow, stackCellX, stackCellY);
	}
	
	/**
	 * Update board area in the display layout. <br>
	 * 
	 * @param x			Left line of the board
	 * @param y			Upper line of the board
	 * @param width 	Width of the board
	 * @param height	Height of the board
	 */
	public void updateBoardArea(final int x, final int y, final int width, final int height) throws InvalidParameterException {
		if (x<0 || y<0 || width<=0 || height<=0) {
			throw new InvalidParameterException();
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 
	 * Update board matrix size. <br>
	 * This method is not allowed while playing. <br>
	 * 
	 * @param column			Column number of the board matrix
	 * @param row				Row number of the board matrix
	 * @param collisionX		Left line of the collision of the board
	 * @param collisionY		Upper line of the collision of the board
	 * @param collisionColumn	Column number of the collision of the board
	 * @param collisionRow		Row number of the collision of the board
	 * @param stackCellX		X position of stacking cell
	 * @param stackCellY		Y position of stacking cell
	 * @throws InvalidParameterException	Invalid parameters are specified
	 */
	public void updateBoardSize(final int column, final int row, final int collisionX, final int collisionY,
			final int collisionColumn, final int collisionRow, final float stackCellX, final float stackCellY)
			throws InvalidParameterException {
		if (row<=0 || column<=0 || collisionColumn<=collisionX || collisionRow<=collisionY) {
			throw new InvalidParameterException();
		}
		this.column = column;
		this.row =  row;
		this.collisionX = collisionX;
		this.collisionY = collisionY;
		this.collisionColumn = collisionColumn;
		this.collisionRow = collisionRow;
		this.stackCol = (int)stackCellX;
		this.stackRow = (int)((collisionRow-1) - ((int)stackCellY - collisionY));
	}
	
	/**
	 * Get all blocks which the board contains. <br>
	 * 
	 * @return All blocks in the board
	 */
	public SparseArray<Block> getBlocks() {
		return blocks;
	}
	
	/**
	 * Get size of the board to judge collision. <br>
	 * 
	 * @return Collision rectangle
	 */
	public RectF getCollisionRect() {
		return new RectF(collisionX, collisionY, collisionX+collisionColumn, collisionY+collisionRow);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Get area of the board in display layout. <br>
	 * 
	 * @return Pixel area
	 */
	public RectF getPixelRect() {
		return new RectF(x, y, x+width, y+height);
	}
	
	/**
	 * Get width of each cell. <br>
	 * 
	 * @return Width of each cell
	 */
	public int getXStep() {
		return width / column;
	}
	
	/**
	 * Get height of each cell. <br>
	 * 
	 * @return Height of each cell
	 */
	public int getYStep() {
		return height / row;
	}
	
	/**
	 * Get column for specified x position. <br>
	 * 
	 * @param x Position from upper line of the board
	 * @return Column of the position x
	 */
	public int getColPos(final float x) {
		return (int)((x - this.x) / getXStep());
	}
	
	/**
	 * Get row for specified y position. <br>
	 * 
	 * @param y Position from left line of the board
	 * @return Row of the position y
	 */
	public int getRowPos(final float y) {
		return (int)((y - this.y) / getYStep());
	}
	
	/**
	 * Add blocks from blockSet. <br>
	 * 
	 * The added block set is to be stabled
	 * @param blockSet Block set to add
	 */
	public void addBlockSet(final BlockSet blockSet) {
		for (Block b :  blockSet.getBlocks()) {
			// Change block position to each cell of the board exactly
			b.setX(Math.round(b.getX()));
			b.setY(Math.round(b.getY()));
			blocks.put(b.getId(), b);
		}
	}
	
	/**
	 * Delete blocks from {@code blocks} which has an id in {@code deletedBlocksIds}
	 * 
	 * @param deletedBlockIds Ids of blocks to delete
	 */
	public void deleteBlocks(final Set<Integer> deletedBlockIds) {
		for (int id : deletedBlockIds) {
			blocks.remove(id);
		}
	}
	
	/**
	 * Clear all blocks. <br>
	 */
	public void clearBlocks() {
		blocks.clear();
	}
		
	/**
	 * Draw block images. <br>
	 * 
	 * @param canvas	Canvas of surface view
	 * @param paint		Paint of surface view
	 */
	public void draw(final Canvas canvas, final Paint paint) {
		for (int i = 0; i < blocks.size(); i++) {
			final int key = blocks.keyAt(i);
			blocks.get(key).drawImage(canvas, paint, this);
		}
	}
	
	/**
	 * Get blocks as a form of matrix n * m. <br>
	 * All blocks are allocated to their point of cell coordinate. <br>
	 * 
	 * @return Matrix form of the board blocks
	 */
	public Block[][] getMatrixedBlocks() {
		// Create matrix of board row*column size
		Block[][] matrix = new Block[collisionRow][collisionColumn];
		
		for (int i = 0; i < blocks.size(); i++) {
			final Block b = blocks.get(blocks.keyAt(i));
			final int row = (collisionRow-1) - ((int)b.getY() - collisionY);
			final int column = (int)b.getX() - collisionX;
			matrix[row][column] = b.clone();
		}
		return matrix;
	}
	
	/**
	 * Get is board stacked and can not accommodate more blocks. <br>
	 * 
	 * @return Is board stacked or not.
	 */
	public boolean getIsBoardStacked() {
		final Block[][] matrix = getMatrixedBlocks();
		return matrix[stackRow][stackCol] != null;
	}
}
