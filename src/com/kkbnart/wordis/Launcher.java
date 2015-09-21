package com.kkbnart.wordis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kkbnart.wordis.menu.Menu;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO
		// Load files and connect DB here
		// setContentView(R.layout.activity_main);
		
		// Instantly move to menu activity
		Intent intent = new Intent(getApplicationContext(), Menu.class);
		startActivity(intent);
		
		// FIXME
		// The activity is not finished
		// Finish this activity
		finish();
	}
}
