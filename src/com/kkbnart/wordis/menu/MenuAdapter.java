package com.kkbnart.wordis.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
	
	public MenuAdapter(Context context, int resource, MenuItem[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
}
