package com.kkbnart.wordis.game.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.kkbnart.wordis.R;

public class CharacterSet {
	// Character map
	private SparseArray<Character> cmap = new SparseArray<Character>();
	
	public CharacterSet(final Context context) {
		final Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.alphabet);
		final int w = 60;
		final int h = 120;
		int i = 0;
		for (CharacterDefinition a : CharacterDefinition.values()) {
			cmap.append(a.getId(), new Character(a.getCharacter(), 
					Bitmap.createBitmap(image, w*(i%10), h*(i/10+1), w, h)));
			i++;
		}
	}
	
	public Character getCharacter(final int id) {
		return cmap.get(id);
	}
	
	public Character getCharacter(final String s) {
		return cmap.get(CharacterDefinition.getDef(s).getId());
	}
}
