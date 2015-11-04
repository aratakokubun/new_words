package com.kkbnart.wordis.game.player;

public class PlayerStatus {
	// Profile
	private int id;
	private String name;
	private String comment;
	// Current game status
	private int score;
	private int chain;
	private int maxChain;
	// Result
	private int diffRate;
	// History
	private int maxChainHistory;
	private int winCount;
	private int loseCount;
	private int drawCount;
	private int disconnectionCount;
	private int rate;
	
	public PlayerStatus(final int id, final String name, final String comment,
			final int maxChainHistory, final int winCount, final int loseCount,
			final int drawCount, final int disconnectionCount, final int rate) {
		// Profile
		this.id = id;
		this.name = name;
		this.comment = comment;
		// Current game status
		this.score = 0;
		this.chain = 0;
		this.maxChain = 0;
		// Result
		this.diffRate = 0;
		// History
		this.maxChainHistory = maxChainHistory;
		this.winCount = winCount;
		this.loseCount = loseCount;
		this.drawCount = drawCount;
		this.disconnectionCount = disconnectionCount;
		this.rate = rate;
	}
	
	
	// History
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
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(final String comment) {
		this.comment = comment;
	}
	
	// Current game store
	public int getScore() {
		return score;
	}
	
	public void setScore(final int score) {
		this.score = score;
	}
	
	public int getChain() {
		return chain;
	}
	
	public void setChain(final int chain) {
		this.chain = chain;
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
	
	public int getMaxChainHistory() {
		return maxChainHistory;
	}
	
	/**
	 * Set new history max chain value. <br>
	 * @param maxChainHistory	New max chain history value
	 * @param compare			Compare with current max chain
	 * @return
	 */
	public void setMaxChainHistory(final int maxChainHistory, final boolean compare) {
		if (maxChainHistory > this.maxChainHistory || !compare) {
			this.maxChainHistory = maxChainHistory;
		}
	}
	
	public int getWinCount() {
		return winCount;
	}
	
	public void setWinCount(final int winCount) {
		this.winCount = winCount;
	}
	
	public int getLoseCount() {
		return loseCount;
	}
	
	public void setLoseCount(final int loseCount) {
		this.loseCount = loseCount;
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
	
	public void updateHistory() {
		setMaxChain(maxChain, true);
		// TODO
	}
}
