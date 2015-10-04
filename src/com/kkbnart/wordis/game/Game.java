package com.kkbnart.wordis.game;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.R;
import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.LoadPropertyException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.rule.MoveAmount;
import com.kkbnart.wordis.game.util.Direction;

@Fullscreen
@EActivity(R.layout.game)
public class Game extends Activity implements IGame {
	private static final String TAG = Game.class.getSimpleName();
	
	// Game manager to proceed everything except control with user interface
	private GameManager manager;

	@ViewById(R.id.mySurfaceView)
	GameSurfaceView gsv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		manager = new GameManager();
		
		// Load property files
		try {
			// FIXME
			// These variables are passed from menu with static class or Intent
			loadGameProperties("test", "downFall");
			manager.createBlockSetFactory(this, "normal", "TEST");
		} catch (BlockCreateException | LoadPropertyException e) {
			// TODO
			// Handle exception
			// finish activity
		}
		
		// Get display size and update depending on the size
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		surfaceSizeChanged(width, height);
	}
	
	/**
	 * Load game properties from files. <br>
	 * 
	 * @throws LoadPropertyException Can not load property files
	 */
	private void loadGameProperties(final String gameTypeName,
			final String moveAmountName) throws LoadPropertyException {
		GameTypeDefinition.getInstance().readJson(gameTypeName, this);
		MoveAmount.getInstance().readJson(moveAmountName, this);
	}
	
	/**
	 * @see {@link IGame#surfaceSizeChanged(int, int)}
	 */
	@Override
	public void surfaceSizeChanged(final int width, final int height) {
		try {
			manager.surfaceSizeChanged(this, width, height);
		} catch (BlockCreateException | InvalidParameterException | NoAnimationException e) {
			// TODO
			// handle exception
			if (Constants.D) Log.e(TAG, "[width:" + width + ", height:" + height + "] are invalid");
		}
	}
	
	@AfterViews
	protected void initGameManager(){
		gsv.setGameActivity(this);
		manager.setGameSurfaceView(gsv);
		
		try {
			manager.startGame();
		} catch (BlockCreateException e) {
			// TODO
			// Show message that game is terminated with exception.
			e.printStackTrace();
		}
	}
	
	// View Button Event
	@Click(R.id.leftButton)
	protected void leftButtonClick(final View view) {
		moveBlock(Direction.LEFT.getId());
	}
	@Click(R.id.rightButton)
	protected void rightButtonClick(final View view) {
		moveBlock(Direction.RIGHT.getId());
	}
	@Click(R.id.upperButton)
	protected void upperButtonClick(final View view) {
		moveBlock(Direction.UP.getId());
	}
	@Click(R.id.downButton)
	protected void downButtonClick(final View view) {
		moveBlock(Direction.DOWN.getId());
	}
	@Click(R.id.counterClockwiseButton)
	protected void counterClockwiseButtonClick(final View view) {
		manager.rotateBlock(false);
	}
	@Click(R.id.clockwiseButton)
	protected void clockwiseButtonClick(final View view) {
		manager.rotateBlock(true);
	}
	@CheckedChange(R.id.menuButton)
	protected void menuButtonCheckedChange(final CompoundButton button,
			final Boolean isChecked) {
		// TODO
		// Show menu dialog
	}
	
	private void moveBlock(final int dirId) {
		PointF p = MoveAmount.getInstance().getMove(dirId);
		manager.moveBlock(p.x, p.y);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			// FIXME
			// Show game option
			// finish();
			System.exit(RESULT_OK);
			return false;
		}
	}

	/**
	 * @see {@link IGame#onSurfaceTouched(MotionEvent)}
	 */
	@Override
	public void onSurfaceTouched(MotionEvent event) {
		manager.onSurfaceTouched(event);
	}
}
