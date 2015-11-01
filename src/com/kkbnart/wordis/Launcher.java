package com.kkbnart.wordis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kkbnart.wordis.exception.FontNotExistException;
import com.kkbnart.wordis.menu.Menu;
import com.kkbnart.wordis.util.WordisFonts;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO
		// Load files and connect DB here
		try {
			readCommonFiles();
		} catch (FontNotExistException e) {
			// TODO Auto-generated catch block
			// Finish activity
		}
		
		// Instantly move to menu activity
		Intent intent = new Intent(this, Menu.class);
		startActivity(intent);
		
		// Finish this activity
		finish();
	}
	
	private void readCommonFiles() throws FontNotExistException {
		// Read font files
		WordisFonts.getInstance().readFontFiles(this);
	}
}
