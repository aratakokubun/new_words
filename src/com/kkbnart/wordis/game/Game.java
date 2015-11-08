package com.kkbnart.wordis.game;

import java.util.HashSet;
import java.util.Set;

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
import com.kkbnart.wordis.game.layout.ViewLayout;
import com.kkbnart.wordis.game.manager.CurrentGameStats;
import com.kkbnart.wordis.game.manager.GameManager;
import com.kkbnart.wordis.game.manager.IGameTerminate;
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
	private  GameManager manager = null;
	// Game type of this game
	private GameType type;
	// FIXME
	// Use common class to preserve objective word 
	// Objective word
	private String word;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// FIXME
		// These variables are passed from menu with static class or Intent
		try {
			// TODO
			// Create game type from players type
			type = GameType.VS_CPU;
			word = "TEST";
			Set<WordisPlayer> wordisPlayers = new HashSet<WordisPlayer>(){
				private static final long serialVersionUID = 969408424928353339L;
				{
				    add(WordisPlayer.MY_PLAYER);
				    add(WordisPlayer.COM);
				}
			};
			manager = new GameManager(this, type, word, wordisPlayers);
			final BlockSetFactory factory = new BlockSetFactory(this, "short", word);
			manager.setBlockSetFactory(factory);
		
			loadGameProperties("test", "downFall", "normal");
		} catch (BlockCreateException | LoadPropertyException | InvalidParameterException e) {
			forceFinishGame(e);
			return;
		}
		
		// Get display size and update depending on the size
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		final int width = displaymetrics.widthPixels;
		final int height = displaymetrics.heightPixels;
		surfaceSizeChanged(width, height);
	}
	
	/**
	 * Finish game forcefully.
	 * 
	 * @param e Exception occurred in methods which call this
	 */
	private void forceFinishGame(Exception e) {
		manager.forceFinishGame();
		finish();
	}
	
	/**
	 * Load game properties from files. <br>
	 * 
	 * @throws LoadPropertyException	Can not load property files
	 * @throws BlockCreateException		Can not create new block set
	 */
	private void loadGameProperties(final String gameTypeName,
			final String moveAmountName, final String scorePatternName) throws LoadPropertyException, BlockCreateException {
		ViewLayout.getInstance().readJson(gameTypeName, this);
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
			// TODO
			// Use xml template
			if (Constants.D) Log.e(TAG, "[width:" + width + ", height:" + height + "] are invalid.");
			forceFinishGame(e);
		}
	}
	
	/**
	 * Called on view item has got ready. <br>
	 */
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
			// Can not pause while versus multiple players
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
		manager.moveBlock(WordisPlayer.MY_PLAYER, p.x, p.y);
	}
	
	@Click(R.id.counterClockwiseButton)
	protected void counterClockwiseButtonClick(final View view) {
		manager.rotateBlock(WordisPlayer.MY_PLAYER, false);
	}
	@Click(R.id.clockwiseButton)
	protected void clockwiseButtonClick(final View view) {
		manager.rotateBlock(WordisPlayer.MY_PLAYER, true);
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
	public void terminateVsPlayer(PlayerStatus myStatus, PlayerStatus oppStatus) {
		// TODO Auto-generated method stub
	}
}
