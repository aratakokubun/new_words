package com.kkbnart.wordis.game.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kkbnart.wordis.R;

public class GameMenuDialog extends Dialog {

	public GameMenuDialog(final Context context,
			final DialogDismissableOnClickListener retryClickListener,
			final DialogDismissableOnClickListener exitClickListener,
			final OnCancelListener dialogCancelListener, 
			final boolean cancelButtonEnabled) {
		
		super(context, R.style.Theme_PopupDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_menu_dialog);
		setTitle(R.string.game_menu_dialog);
		setOnCancelListener(dialogCancelListener);
		
		// Retry
		final Button retry = (Button) findViewById(R.id.retryButton);
		retryClickListener.setDialog(this);
		retry.setOnClickListener(retryClickListener);
		
		// Exit
		final Button exit = (Button) findViewById(R.id.exitButton);
		exitClickListener.setDialog(this);
		exit.setOnClickListener(exitClickListener);
		
		// Cancel
		final Button cancel = (Button) findViewById(R.id.cancelButton);
		if (cancelButtonEnabled) {
			cancel.setEnabled(cancelButtonEnabled);
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cancel();
				}
			});
		}
	}
}
