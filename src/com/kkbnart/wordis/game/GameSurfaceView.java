package com.kkbnart.wordis.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.board.NextBlocks;
import com.kkbnart.wordis.game.board.OperatedBlocks;
import com.kkbnart.wordis.game.exception.InvalidParameterException;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameSurfaceView";
	
	// Game activity
	private Game gameActivity;
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}
	
	public void setGameActivity(final Game gameActivity) {
		this.gameActivity = gameActivity;
	}
	
	public void draw(final Board board, final OperatedBlocks ob, final NextBlocks nb) {
		final SurfaceHolder holder = getHolder();
		Canvas c = null;
		try {
			// Lock Canvas
			c = holder.lockCanvas();
			if (c != null) {
				// Clear canvas
				c.drawColor(0, PorterDuff.Mode.CLEAR);
				
				final Paint p = new Paint();
				board.draw(c, p);
				ob.draw(c, p, board);
				nb.draw(c, p, board);
			}
		} finally {
			try {
				getHolder().unlockCanvasAndPost(c);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Can not unlock canvas");
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Canvas c = getHolder().lockCanvas();
		try {
			gameActivity.surfaceSizeChanged(width, height);
		} catch (InvalidParameterException e) {
			System.err.println("[width:" + width + ", height:" + height + "] are invalid");
		}
		getHolder().unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = getHolder().lockCanvas();
		try {
			gameActivity.surfaceSizeChanged(c.getWidth(), c.getHeight());
		} catch (InvalidParameterException e) {
			System.err.println("[width:" + c.getWidth() + ", height:" + c.getHeight() + "] are invalid");
		}
		getHolder().unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}
