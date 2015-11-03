package com.kkbnart.wordis.game.animation;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kkbnart.wordis.game.GameState;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.Block;
import com.kkbnart.wordis.game.object.block.NextBlocks;

public class BlockDeleteAnimation extends GameAnimation {

	private static final long FLASH_INTERVAL = 50L;
	private AnimationTime flashTime;
	// Magnification rate finally reached on disappear
	private static final float DISAPPEAR_MAG = 0.2f;
	private AnimationTime disappearTime;
	private Set<Block> deletedBlocks = new HashSet<Block>();

	public BlockDeleteAnimation(final long animationTime, final GameState priorAction) {
		super(animationTime, priorAction);
		flashTime = new AnimationTime(100/*ms*/, 400/*ms*/);
		disappearTime = new AnimationTime(400/*ms*/, 600/*ms*/);
	}
	
	public void setDeleteBlockIds(final Set<Block> deletedBlocks) {
		this.deletedBlocks = deletedBlocks;
	}

	@Override
	protected void drawAnimation(Canvas canvas, Board board,
			NextBlocks nextBlocks, long elapsedTime, long diffTime) {
		final Paint paint = new Paint();
		
		// Collapse block
		if (flashTime.start < elapsedTime && elapsedTime < flashTime.end) {
			drawFlashBlock(canvas, paint, board, elapsedTime);
		}
		// Draw next blocks
		nextBlocks.draw(canvas, paint, board);
		// Draw text
		if (disappearTime.start < elapsedTime && elapsedTime < disappearTime.end) {
			drawDisappearBlock(canvas, paint, board, elapsedTime);
		}
		
		
		nextBlocks.draw(canvas, paint, board);
	}
	
	private void drawFlashBlock(final Canvas canvas, final Paint paint,
			final Board board, long elapsedTime) {
		board.draw(canvas, paint);
		
		elapsedTime -= flashTime.start;
		final int flashAlpha = (int)((.6f + .4f * (float)(Math.sin((double)elapsedTime / FLASH_INTERVAL * Math.PI))) * 255);
		paint.setAlpha(flashAlpha);
		for (Block b : deletedBlocks) {
			b.drawImage(canvas, paint, board);
		}
	}

	private void drawDisappearBlock(final Canvas canvas, final Paint paint,
			final Board board, long elapsedTime) {
		board.draw(canvas, paint);
		
		elapsedTime -= disappearTime.start;
		final int disappearAlpha = (int)((float)(1.f - elapsedTime/ disappearTime.getLength()) * 255);
		final float mag = 1.f + DISAPPEAR_MAG * elapsedTime/ disappearTime.getLength();
		paint.setAlpha(disappearAlpha);
		for (Block b : deletedBlocks) {
			b.drawImage(canvas, paint, board, mag);
		}
	}
}
