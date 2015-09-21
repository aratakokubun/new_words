package com.kkbnart.wordis.game.object;

import java.util.ArrayList;

public enum PatternDefinition {
	BAR(new ArrayList<int[]>(){
		private static final long serialVersionUID = -3816705301771640900L;
		{	add(new int[]{0, 0});
			add(new int[]{1, 0});
			add(new int[]{2, 0});
			add(new int[]{3, 0}); }}, 1),
	L(new ArrayList<int[]>(){
		private static final long serialVersionUID = -5588467579491847994L;
		{	add(new int[]{0, 1});
			add(new int[]{1, 1});
			add(new int[]{2, 1});
			add(new int[]{2, 0}); }}, 1),
	INV_L(new ArrayList<int[]>(){
		private static final long serialVersionUID = 3877879944501221288L;
		{	add(new int[]{0, 0});
			add(new int[]{1, 0});
			add(new int[]{2, 0});
			add(new int[]{2, 1}); }}, 1),
	Z(new ArrayList<int[]>(){
		private static final long serialVersionUID = 5251963045219314670L;
		{	add(new int[]{0, 0});
			add(new int[]{1, 0});
			add(new int[]{1, 1});
			add(new int[]{2, 1}); }}, 2),
	INV_Z(new ArrayList<int[]>(){
		private static final long serialVersionUID = 3846824737444870329L;
		{	add(new int[]{0, 1});
			add(new int[]{1, 1});
			add(new int[]{1, 0});
			add(new int[]{2, 0}); }}, 1),
	BUMP(new ArrayList<int[]>(){
		private static final long serialVersionUID = 3257631799389326558L;
		{	add(new int[]{0, 0});
			add(new int[]{1, 0});
			add(new int[]{2, 0});
			add(new int[]{1, 1}); }}, 1);
	
	private ArrayList<int[]> positions;
	private int center;
	private PatternDefinition(final ArrayList<int[]> positions, final int center) {
		this.positions = positions;
		this.center = center;
	}
	public ArrayList<int[]> getPositions() {
		return positions;
	}
	public int getCenter() {
		return center;
	}
}
