package com.kkbnart.wordis.game.object;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

public class BlockColorSet {
	// Block set map
	private SparseArray<BlockColor> bcmap = new SparseArray<BlockColor>();
	
	public BlockColorSet(final Resources resources) {
		for (ColorDefinition c : ColorDefinition.values()) {
			bcmap.append(c.getId(), new BlockColor(c.getColor(),
				BitmapFactory.decodeResource(resources, c.getImageId())));
		}
	}
	
	public BlockColor getColor(final int id) {
		return bcmap.get(id);
	}
}
