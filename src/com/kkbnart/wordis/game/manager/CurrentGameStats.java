package com.kkbnart.wordis.game.manager;

import com.kkbnart.wordis.game.rule.ScoreCalculator;

public class CurrentGameStats {
	private int score;
	private int maxChain;
	private int chain;
	private int deletedBlocks;
	private boolean isLoser;
	
	public CurrentGameStats() {
		clearStats();
	}
	
	public void clearStats() {
		this.score = 0;
		this.maxChain = 0;
		this.chain = 0;
		this.deletedBlocks = 0;
		this.isLoser = false;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(final int score) {
		this.score = score;
	}
	
	public int getMaxChain() {
		return maxChain;
	}
	
	public boolean getIsLoser() {
		return isLoser;
	}
	
	public void setIsLoser(final boolean isLoser) {
		this.isLoser = isLoser;
	}
	
	public void updateMaxChain() {
		if (maxChain < chain) {
			maxChain = chain;
		}
	}
	
	public int incrementChain() {
		chain++;
		updateMaxChain();
		return chain;
	}
	
	public void endChain() {
		this.chain = 0;
		this.deletedBlocks = 0;
	}
	
	public int incrementDeletedBlocks(final int blocks) {
		deletedBlocks += blocks;
		return deletedBlocks;
	}
	
	public void updateScore(final int blocks) {
		incrementChain();
		incrementDeletedBlocks(blocks);
		score += ScoreCalculator.getInstance().calcDeleteScore(blocks, chain, deletedBlocks);
	}
}
