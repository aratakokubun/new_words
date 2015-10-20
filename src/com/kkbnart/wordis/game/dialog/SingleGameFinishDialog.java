package com.kkbnart.wordis.game.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kkbnart.wordis.R;

public class SingleGameFinishDialog extends Dialog {
	
	public SingleGameFinishDialog(final Context context,
			final DialogDismissableOnClickListener retryClickListener,
			final DialogDismissableOnClickListener exitClickListener,
			final int score, final int maxChain) {
		
		super(context, R.style.Theme_NoCancelDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_finish_dialog);
		setTitle(R.string.game_finish_dialog);
		setCancelable(false);
		
		// Score
		final TextView scoreValue = (TextView) findViewById(R.id.scoreValue);
		scoreValue.setText(String.valueOf(score));
		
		// Max chain
		final TextView chainValue = (TextView) findViewById(R.id.chainValue);
		chainValue.setText(String.valueOf(maxChain));
		
		// Retry
		final Button retry = (Button) findViewById(R.id.retryButton);
		retryClickListener.setDialog(this);
		retry.setOnClickListener(retryClickListener);
		
		// Exit
		final Button exit = (Button) findViewById(R.id.exitButton);
		exitClickListener.setDialog(this);
		exit.setOnClickListener(exitClickListener);
	}
}
