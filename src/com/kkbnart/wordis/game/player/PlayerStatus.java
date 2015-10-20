package com.kkbnart.wordis.game.player;

public class PlayerStatus {
	private int id;
	private String name;
	private int score;
	private int maxChain;
	private int drawCount;
	private int disconnectionCount;
	private int rate;
	private int diffRate;
	
	public PlayerStatus(final int id, final String name, final int score,
			final int maxChain, final int drawCount,
			final int disconnectionCount, final int rate, final int diffRate) {
		this.id = id;
		this.name = name;
		this.score = score;
		this.maxChain = maxChain;
		this.drawCount = drawCount;
		this.disconnectionCount = disconnectionCount;
		this.rate = rate;
		this.diffRate = diffRate;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(final int id)	 {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
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
	
	/**
	 * Set new max chain value. <br>
	 * @param maxChain	New max chain value
	 * @param compare	Compare with current max chain
	 * @return
	 */
	public void setMaxChain(final int maxChain, final boolean compare) {
		if (maxChain > this.maxChain || !compare) {
			this.maxChain = maxChain;
		}
	}
	
	public int getDrawCount() {
		return drawCount;
	}
	
	public void setDrawCount(final int drawCount) {
		this.drawCount = drawCount;
	}
	
	public int getDisconnectionCount() {
		return disconnectionCount;
	}
	
	public void setDiscconectionCount(final int disconnectionCount) {
		this.disconnectionCount = disconnectionCount;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(final int rate) {
		this.rate = rate;
	}
	
	public int getDiffRate() {
		return diffRate;
	}
	
	public void setDiffRate(final int diffRate) {
		this.diffRate = diffRate;
	}
	
	public void applyResult() {
		rate += diffRate;
		diffRate = 0;
	}
}
