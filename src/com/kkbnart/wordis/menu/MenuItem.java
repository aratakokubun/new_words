package com.kkbnart.wordis.menu;

import com.kkbnart.wordis.game.Game_;

/**
 * Menu items to display in menu list
 * @author kkbnart
 */
public enum MenuItem {
	// TODO add items
	GAME(0, "Start Game", Game_.class, 0/*TODO change image of item list*/),
	;
	
	private int menuId;
	private String menuName;
	private Class<?> clazz;
	private int drawableId;
	
	/**
	 * Private constructor with specifying id, name, class and image
	 * @param menuId
	 * @param menuName
	 * @param clazz
	 * @param drawableId
	 */
	private MenuItem(final int menuId, final String menuName
			, final Class<?> clazz, final int drawableId) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.clazz = clazz;
		this.drawableId = drawableId;
	}
	
	public int getMenuId() {
		return menuId;
	}
	
	public String getMenuName() {
		return menuName;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public int getDrawableId() {
		return drawableId;
	}
	
	@Override
	public String toString() {
		return menuName;
	}
}
