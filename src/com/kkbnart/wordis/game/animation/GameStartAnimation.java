package com.kkbnart.wordis.game.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.FontNotExistException;
import com.kkbnart.wordis.game.GameState;
import com.kkbnart.wordis.game.object.block.NextBlocks;
import com.kkbnart.wordis.game.object.board.Board;
import com.kkbnart.wordis.util.WordisFontTypes;
import com.kkbnart.wordis.util.WordisFonts;

public class GameStartAnimation extends GameAnimation {
	private static final String TAG = GameAnimation.class.getSimpleName();
	
	// Game over text position
	private TextPositionRate textPosition = new TextPositionRate(0.5f, 0.4f, 80.f);
	
	public GameStartAnimation(long animationTime, GameState priorAction) {
		super(animationTime, priorAction);
	}

	@Override
	protected void drawAnimation(Canvas canvas, Board board, final NextBlocks nextBlocks, 
			long elapsedTime, long diffTime) {
		final Paint paint = new Paint();
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
		paint.setTextSize(textSize);

		// Custom alpha
		final int alpha = (int)((((getAnimationTime() - elapsedTime) % 1000) / 1250.f + 0.2f) * 255);
		paint.setAlpha(alpha);
		
		// Custom text position
		final int count = (int) ((getAnimationTime() - elapsedTime) / 1000L) + 1;
		final String countText = String.valueOf(count);
		final float[] widths = new float[countText.length()];
		final int charNum = paint.getTextWidths(countText, widths);
		int textWidth = 0;
		for (int i = 0; i < charNum; i++) {
			textWidth += widths[i];
		}
		final int xPos = (int)(textX - textWidth / 2.f);
		final int yPos = (int)(textY - (paint.descent() + paint.ascent()) / 2.f);
		canvas.drawText(countText, xPos, yPos, paint);
	}

}
