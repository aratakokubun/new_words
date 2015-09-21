package com.kkbnart.wordis.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kkbnart.wordis.R;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu);
		setMenuItemList();
	}
	
	private void setMenuItemList() {
		ListView menuList = (ListView) findViewById(R.id.menuList);
		MenuAdapter adapter = new MenuAdapter(this,
				android.R.layout.simple_list_item_1, MenuItem.values());
		menuList.setAdapter(adapter);
		
		final Context context = this;
		// Process on list item tapped or clicked
		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view
					, int position, long id) {
				MenuItem item = (MenuItem) ((ListView)parent).getItemAtPosition(position);
				Intent intent = new Intent(context, item.getClazz());
				startActivity(intent);
			}
		});
	}
	
	
}
