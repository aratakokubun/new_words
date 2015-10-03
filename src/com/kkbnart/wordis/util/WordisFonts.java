package com.kkbnart.wordis.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.exception.FontNotExistException;

/**
 * Manage fonts in Wordis. <br>
 * To access this class instance, use {@link #getInstance()}. <br>
 * 
 * @author kkbnart
 */
public class WordisFonts {
	private static final String TAG = WordisFonts.class.getSimpleName();
	
	// Singleton instance
	private static WordisFonts instance = null;
	// TypeFace map
	private Map<WordisFontTypes, Typeface> typefaces = new HashMap<WordisFontTypes, Typeface>();
	
	/**
	 * Get singleton instance of this class. <br>
	 * 
	 * @return WordisFonts instance
	 */
	public static synchronized WordisFonts getInstance() {
		if (instance == null) {
			instance = new WordisFonts();
		}
		return instance;
	}
	
	private WordisFonts() {}
	
	/**
	 * Read font from files. <br>
	 * 
	 * @param context Activity context
	 * @throws FontNotExistException	If specified font file does not exist
	 */
	public void readFontFiles(Context context) throws FontNotExistException {
		HashSet<String> invalidPath = new HashSet<String>();
 		for (WordisFontTypes f : WordisFontTypes.values()) {
			try {
				Typeface typeface = Typeface.createFromAsset(context.getAssets(), f.getFilepath());
				typefaces.put(f, typeface);
			} catch (NullPointerException e) {
				invalidPath.add(f.getFilepath());
			}
		}
 		
 		if (invalidPath.size() > 0) {
 			String pathString = new String();
 			for (String fp : invalidPath) {
 				pathString += fp + ",";
 			}
 			throw new FontNotExistException(pathString);
 		} else {
 			if (Constants.D) Log.d(TAG, "Font files have been read correctly.");
 		}
	}
	
	/**
	 * Get specified font. <br>
	 * 
	 * @param f	Worids font type
	 * @return Font typeface
	 * @throws FontNotExistException	If specified font is not defined
	 */
	public Typeface getFont(WordisFontTypes f) throws FontNotExistException {
		Typeface typeface = typefaces.get(f);
		if (typeface == null) {
			throw new FontNotExistException(f);
		}
		return typeface;
	}
}
