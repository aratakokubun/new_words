package com.kkbnart.wordis.game.dialog;

import com.kkbnart.wordis.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class VsCpuFinishDialog extends Dialog {
	
	public VsCpuFinishDialog(final Context context,
			final DialogDismissableOnClickListener retryClickListener,
			final DialogDismissableOnClickListener exitClickListener,
			final DialogDismissableOnClickListener nextClickListener,
			final int score, final int maxChain, final int exp, final int point,
			final boolean isLoser) {
		super(context, R.style.Theme_NoCancelDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vs_cpu_finish_dialog);
		setTitle(R.string.game_finish_dialog);
		setCancelable(false);
		
		// Score
		final TextView scoreValue = (TextView) findViewById(R.id.scoreValue);
		scoreValue.setText(String.valueOf(score));
		
		// Max chain
		final TextView chainValue = (TextView) findViewById(R.id.chainValue);
		chainValue.setText(String.valueOf(maxChain));
		
		// Exp
		final TextView expValue = (TextView) findViewById(R.id.expValue);
		expValue.setText(String.valueOf(exp));
		
		// Point
		final TextView pointValue = (TextView) findViewById(R.id.pointValue);
		pointValue.setText(String.valueOf(point));
		
		final Button retry = (Button) findViewById(R.id.retryButton);
		final Button next = (Button) findViewById(R.id.nextButton);
		if (isLoser) {
			nextClickListener.setDialog(this);
			next.setOnClickListener(retryClickListener);
			retry.setVisibility(View.GONE);
		} else {
			retryClickListener.setDialog(this);
			retry.setOnClickListener(retryClickListener);
			next.setVisibility(View.GONE);
		}
		
		// Exit
		final Button exit = (Button) findViewById(R.id.exitButton);
		exitClickListener.setDialog(this);
		exit.setOnClickListener(exitClickListener);
	}

}
