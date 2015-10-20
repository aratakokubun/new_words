package com.kkbnart.wordis.game.dialog;

import android.app.Dialog;
import android.view.View.OnClickListener;

public abstract class DialogDismissableOnClickListener implements OnClickListener {
	private Dialog dialog = null;

	public void setDialog (final Dialog dialog) {
		this.dialog = dialog;
	}
	
	protected void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
