package com.kkbnart.wordis.game;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.kkbnart.wordis.R;
import com.kkbnart.wordis.game.exception.BlockCreateException;
import com.kkbnart.wordis.game.exception.LoadPropertyException;
import com.kkbnart.wordis.game.rule.MoveAmount;

@Fullscreen
@EActivity(R.layout.game)
public class Game extends Activity {
	// Game manager to proceed everything except control with user interface
	private GameManager manager;
	// Specify amount of move for each direction
	private MoveAmount moveAmount;
	
	@ViewById(R.id.mySurfaceView)
	GameSurfaceView gsv;
	
	@ViewById(R.id.leftButton)
	Button left;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@AfterViews
	protected void initGameManager(){
		// Wait for surface view ready
		manager = new GameManager(this, gsv);
		// FIXME
		// Specify the amount of moves for each key
		
		// FIXME
		GameTypeDefinition gtd = new GameTypeDefinition();
		try {
			gtd.loadProperty("default");
		} catch (LoadPropertyException e) {
			// TODO
			// handle exception
		}
		try {
			manager.startGame();
		} catch (BlockCreateException e) {
			// TODO
			// Show message that game is terminated with exception.
		}
	}
	
	public void startGame(final int gameType) {
		// TODO
	}
	
	// View Button Event
	@Click(R.id.leftButton)
	protected void leftButtonClick(final View view) {
		manager.moveBlock(-1, 0);
	}
	@Click(R.id.rightButton)
	protected void rightButtonClick(final View view) {
		manager.moveBlock(1, 0);
	}
	@Click(R.id.upperButton)
	protected void upperButtonClick(final View view) {
		// FIXME
		// Upper move is needed for only special mode
		// gm.moveBlock(0, -1);
	}
	@Click(R.id.downButton)
	protected void downButtonClick(final View view) {
		manager.moveBlock(0, 1);
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
}
