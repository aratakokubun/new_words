package com.kkbnart.wordis.game.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.FontNotExistException;
import com.kkbnart.wordis.game.GameStatus;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.NextBlocks;
import com.kkbnart.wordis.util.WordisFontTypes;
import com.kkbnart.wordis.util.WordisFonts;

/**
 * Manage time and movement while game over. <br>
 * 
 * @author kkbnart
 */
public class GameOverAnimation extends GameAnimation {
	private static final String TAG = GameOverAnimation.class.getSimpleName();
	
	// Block collapse
	private static final float minSpeed = 0.012f;
	private static final float maxSpeed = 0.02f;
	private float[] collapseSpeed = null;
	private AnimationTime collapseTime;
	
	// Game over text position
	private TextPositionRate textPosition = new TextPositionRate(0.5f, 0.4f, 80.f);
	// TODO
	// Declare in xml
	private String gameOverText = "LOSE";
	private AnimationTime textTime;

	public GameOverAnimation(final long animationTime, final GameStatus postAction, final int col) {
		super(animationTime, postAction);
		setAnimationParameters(col);
		collapseTime = new AnimationTime(10/*ms*/, 3000/*ms*/);
		textTime 	 = new AnimationTime(1200/*ms*/, 10000/*ms*/);
	}

	public void setAnimationParameters(final int col) {
		collapseSpeed = new float[col];
		setAnimationSpeed();
	}
	
	/**
	 * Set animation move speed. <br>
	 */
	private void setAnimationSpeed() {
		//  Create row patterns of speed
		final int col = collapseSpeed.length;
		for (int i = 0; i < col; i++) {
			collapseSpeed[i] = minSpeed*((float)i/(col-1)) + maxSpeed*((float)(col-1-i)/(col-1));
		}
		// Shuffle speed
		for (int i = 0; i < 100/*number of shuffle*/; i++) {
			final int swapRow1 = (int)(col*Math.random());
			final int swapRow2 = (int)(col*Math.random());
			float temp = collapseSpeed[swapRow1];
			collapseSpeed[swapRow1] = collapseSpeed[swapRow2];
			collapseSpeed[swapRow2] = temp;
		}
	}
	
	public void setGameOverText(final String gameOverText) {
		this.gameOverText = gameOverText;
	}
	
	@Override
	protected void setAnimationStart(final long now) {
		super.setAnimationStart(now);
		setAnimationSpeed();
	}

	@Override
	public void drawAnimation(final Canvas canvas, final Board board, final NextBlocks nextBlocks, 
			final long elapsedTime, final long diffTime) {
		final Paint paint =  new Paint();
		// Collapse block
		if (collapseTime.start < elapsedTime && elapsedTime < collapseTime.end) {
			drawCollapseBlock(canvas, paint, board, diffTime);
		}
		// Draw next blocks
		nextBlocks.draw(canvas, paint, board);
		// Draw text
		if (textTime.start < elapsedTime && elapsedTime < textTime.end) {
			drawGameOverText(canvas, paint, board, elapsedTime, diffTime);
		}
	}
	
	/**
	 * Draw collapsing block animation. <br>
	 * 
	 * @param canvas		Surface view canvas
	 * @param paint			Paint
	 * @param board			Current board
	 * @param diffTime		Elapsed milli time from previous animation time
	 */
	private void drawCollapseBlock(final Canvas canvas, final Paint paint,
			final Board board, final long diffTime) {
		final int size = board.getBlocks().size();
		for (int i = 0; i < size; i++) {
			final int id = board.getBlocks().keyAt(i);
			final Block b = board.getBlocks().get(id);
			final int col = (int)b.getX();
			final float newY = b.getY() + collapseSpeed[col]*diffTime;
			b.setY(newY);
		}
		
		board.draw(canvas, paint);
	}
	
	/**
	 * Draw game over text animation. <br>
	 * 
	 * @param canvas		Surface view canvas
	 * @param paint			Paint
	 * @param board			Current board
	 * @param elapsedTime	Elapsed milli time from start
	 * @param diffTime		Elapsed milli time from previous animation time
	 */
	private void drawGameOverText(final Canvas canvas, final Paint paint, final Board board,
			final long elapsedTime, final long diffTime) {
		paint.setColor(Color.YELLOW);
		try {
			paint.setTypeface(WordisFonts.getInstance().getFont(WordisFontTypes.OOGIEBOO));
		} catch (FontNotExistException e) {
			if (Constants.D) Log.e(TAG, "Font is not defined.");
		}
		
		// Set text position and size
		final float textX = textPosition.x * board.getWidth() + board.getX();
		final float textY = textPosition.y * board.getHeight() + board.getY();
		final float textSize = textPosition.size;
		
		// Custom text size
		final float popRate = 1.f + 0.05f*(float)(Math.cos((double)elapsedTime / textTime.getLength() * Math.PI * 20.f));
		final float popTextSize = textSize * popRate;
		paint.setTextSize(popTextSize);
		
		// Custom text position
		final float[] widths = new float[gameOverText.length()];
		final int charNum = paint.getTextWidths(gameOverText, widths);
		int textWidth = 0;
		for (int i = 0; i < charNum; i++) {
			textWidth += widths[i];
		}
		final int xPos = (int)(textX - textWidth / 2.f);
		final int yPos = (int)(textY - (paint.descent() + paint.ascent()) / 2.f);
		canvas.drawText(gameOverText, xPos, yPos, paint);
	}
}
