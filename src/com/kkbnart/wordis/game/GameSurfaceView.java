package com.kkbnart.wordis.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kkbnart.wordis.game.animation.AnimationManager;
import com.kkbnart.wordis.game.board.Board;
import com.kkbnart.wordis.game.object.block.NextBlocks;
import com.kkbnart.wordis.game.object.block.OperatedBlocks;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = GameSurfaceView.class.getSimpleName();
	
	// Game Interface
	private IGameActivity game;
	private boolean mDownTouch = false;	
	 
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}
	
	public void setGameActivity(final IGameActivity game) {
		this.game = game;
	}
	
	/**
	 * Draw board and blocks. <br>
	 * 
	 * @param board	Current board
	 * @param ob	Operated blocks
	 * @param nb	Next blocks
	 */
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
	
	/**
	 * Draw animation. <br>
	 * 
	 * @param animationManager	Animation manager and executor
	 * @param board				Current board
	 * @return Game action to be taken after animation
	 */
	public GameStatus drawAnimation(final AnimationManager animationManager,
			final Board board) {
		final SurfaceHolder holder = getHolder();
		Canvas c = null;
		try {
			// Lock Canvas
			c = holder.lockCanvas();
			if (c != null) {
				// Clear canvas
				c.drawColor(0, PorterDuff.Mode.CLEAR);
				return animationManager.executeAnimation(c, board);
			}
		} finally {
			try {
				getHolder().unlockCanvasAndPost(c);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Can not unlock canvas");
			}
		}
		return GameStatus.NONE;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Canvas c = getHolder().lockCanvas();
		game.surfaceSizeChanged(width, height);
		getHolder().unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = getHolder().lockCanvas();
		game.surfaceSizeChanged(c.getWidth(), c.getHeight());
		getHolder().unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

    @Override
    public boolean performClick()
    {
        super.performClick();
        return true;
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// Listening for the down and up touch events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownTouch = true;
			return true;
		case MotionEvent.ACTION_UP:
			if (mDownTouch) {
				mDownTouch = false;
				performClick(); // Call this method to handle the response, and
								// thereby enable accessibility services to
								// perform this action for a user who cannot
								// click the touchscreen.
				game.onSurfaceTouched(event);
				return true;
			}
		}
		return true;
	}
}
