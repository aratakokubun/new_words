package com.kkbnart.wordis.game;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.R;
import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.exception.InvalidParameterException;
import com.kkbnart.wordis.exception.LoadPropertyException;
import com.kkbnart.wordis.exception.NoAnimationException;
import com.kkbnart.wordis.game.dialog.DialogDismissableOnClickListener;
import com.kkbnart.wordis.game.dialog.GameMenuDialog;
import com.kkbnart.wordis.game.dialog.SingleGameFinishDialog;
import com.kkbnart.wordis.game.object.block.BlockSetFactory;
import com.kkbnart.wordis.game.player.PlayerStatus;
import com.kkbnart.wordis.game.player.WordisPlayer;
import com.kkbnart.wordis.game.rule.MoveAmount;
import com.kkbnart.wordis.game.rule.ScoreCalculator;
import com.kkbnart.wordis.game.util.Direction;
import com.kkbnart.wordis.menu.Menu;

/**
 * Wordis game activity 
 * 
 * @author kkbnart
 */
@Fullscreen
@EActivity(R.layout.game)
public class Game extends Activity implements IGameActivity, IGameTerminate {
	private static final String TAG = Game.class.getSimpleName();

	@ViewById(R.id.mySurfaceView)
	GameSurfaceView gsv;
	
	// Game manager to proceed everything except control with user interface
	private GameManager manager;
	// Game type of this game
	private GameType type;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// FIXME
		// These variables are passed from menu with static class or Intent
		// FIXME
		// Prepare multiple game managers for multiple players
		// And use common game thread 
		manager = new GameManager(this, WordisPlayer.MY_PLAYER);
		
		// FIXME
		// These variables are passed from menu with static class or Intent
		// Load property files
		try {
			loadGameProperties("test", "downFall", "normal");
			// manager.setBlockSetFactory(new BlockSetFactory(this, "normal", "TEST"));
			manager.setBlockSetFactory(new BlockSetFactory(this, "short", "TEST"));
		} catch (BlockCreateException | LoadPropertyException e) {
			forceFinishGame(e);
			return;
		}
		this.type = GameType.TEST;
		
		// Get display size and update depending on the size
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		surfaceSizeChanged(width, height);
	}
	
	/**
	 * Finish game forcefully.
	 * 
	 * @param e Exception occurred in methods which call this
	 */
	private void forceFinishGame(Exception e) {
		manager.interruptGame();
		// TODO
		// finish activity
		System.exit(RESULT_OK);
	}
	
	/**
	 * Load game properties from files. <br>
	 * 
	 * @throws LoadPropertyException Can not load property files
	 */
	private void loadGameProperties(final String gameTypeName,
			final String moveAmountName, final String scorePatternName) throws LoadPropertyException {
		GameTypeDefinition.getInstance().readJson(gameTypeName, this);
		MoveAmount.getInstance().readJson(moveAmountName, this);
		ScoreCalculator.getInstance().readJson(scorePatternName, this);
 	}
	
	/**
	 * @see {@link IGameActivity#surfaceSizeChanged(int, int)}
	 */
	@Override
	public void surfaceSizeChanged(final int width, final int height) {
		try {
			manager.surfaceSizeChanged(width, height);
		} catch (BlockCreateException | InvalidParameterException | NoAnimationException e) {
			if (Constants.D) Log.e(TAG, "[width:" + width + ", height:" + height + "] are invalid.");
			forceFinishGame(e);
		}
	}
	
	@AfterViews
	protected void setupView(){
		gsv.setGameActivity(this);
		manager.setGameSurfaceView(gsv);
		startGame();
	}
	
	private void startGame() {
		try {
			manager.startGame(type);
		} catch (BlockCreateException | NoAnimationException e) {
			forceFinishGame(e);
		}
	}
	
	private void finishGame() {
		// TODO
		// Pass values to menu
		Intent intent = new Intent(getApplicationContext(), Menu.class);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_blackout_open, R.anim.activity_blackout_close);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			// TODO
			// Change according to game status
			showGameMenu();
			return false;
		}
	}
	
	private void showGameMenu() {
		switch (type) {
		case TEST:
		case PRACTICE:
		case SINGLE:
		case VS_CPU:
			Dialog dialog = new GameMenuDialog(this, retryClickListener,
					exitClickListener, dialogCancelListener, true);
			dialog.show();
			manager.suspendGame();
			break;
		default:
			break;
		}
	}

	/**
	 * @see {@link IGameActivity#onSurfaceTouched(MotionEvent)}
	 */
	@Override
	public void onSurfaceTouched(MotionEvent event) {
		manager.onSurfaceTouched(event);
	}
	
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
	
	private void moveBlock(final int dirId) {
		PointF p = MoveAmount.getInstance().getMove(dirId);
		manager.moveBlock(p.x, p.y);
	}
	
	@Click(R.id.counterClockwiseButton)
	protected void counterClockwiseButtonClick(final View view) {
		manager.rotateBlock(false);
	}
	@Click(R.id.clockwiseButton)
	protected void clockwiseButtonClick(final View view) {
		manager.rotateBlock(true);
	}
	
	@Click(R.id.menuButton)
	protected void menuButtonCClick(final View view) {		
		Dialog dialog = new GameMenuDialog(this, retryClickListener,
				exitClickListener, dialogCancelListener, /*cancel button enabled = */ true);
		dialog.show();
		manager.suspendGame();
	}
	
	// On Click Listener for game menu dialog
	private DialogDismissableOnClickListener retryClickListener = new DialogDismissableOnClickListener() {
		@Override
		public void onClick(View v) {
			startGame();
			dismiss();
		}
	};
	
	private DialogDismissableOnClickListener exitClickListener = new DialogDismissableOnClickListener() {
		@Override
		public void onClick(View v) {
			finishGame();
			dismiss();
		}
	};
	
	private OnCancelListener dialogCancelListener = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			manager.resumeGame();
		}
	};

	@Override
	public void terminateSingle(CurrentGameStats currentGameStats) {
		Dialog dialog = new SingleGameFinishDialog(this, retryClickListener,
				exitClickListener, currentGameStats.getScore(), currentGameStats.getMaxChain());
		dialog.show();
	}

	@Override
	public void terminateVsCpu(PlayerStatus myStatus, PlayerStatus cpuStatus) {
		// TODO Auto-generated method stub
	}

	@Override
	public void terminateVersus(PlayerStatus myStatus, PlayerStatus oppStatus) {
		// TODO Auto-generated method stub
	}
}
