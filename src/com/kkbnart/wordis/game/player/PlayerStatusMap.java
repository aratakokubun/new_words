package com.kkbnart.wordis.game.player;

import java.util.HashMap;

import com.kkbnart.wordis.exception.NoSuchPlayerException;

public class PlayerStatusMap extends HashMap<WordisPlayer, PlayerStatus> {
	private static final long serialVersionUID = -6758837623464097777L;

	public void setResult(final HashMap<WordisPlayer, Integer> diffRates) throws NoSuchPlayerException {
		for (Entry<WordisPlayer, Integer> entry : diffRates.entrySet()) {
			final WordisPlayer player = entry.getKey();
			final int diffRate = entry.getValue();
			final PlayerStatus status = get(entry.getKey());
			if (status == null) {
				throw new NoSuchPlayerException(player);
			} else {
				status.setDiffRate(diffRate);
			}
		}
	}
	
	public void applyResult() {
		for (Entry<WordisPlayer, PlayerStatus> entry : entrySet()) {
			entry.getValue().applyResult();
		}
	}
}
