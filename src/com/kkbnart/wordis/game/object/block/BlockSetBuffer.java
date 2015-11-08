package com.kkbnart.wordis.game.object.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kkbnart.wordis.exception.BlockCreateException;
import com.kkbnart.wordis.game.player.WordisPlayer;

/**
 * <p>
 * Stock created block set by factory. <br>
 * Supply block set for each {@link BlockSetClient}. <br>
 * </p>
 * <p>
 * Register {@link BlockSetClient} to receive block set from this class. <br>
 * </p>
 * 
 * @author kkbnart
 */
public class BlockSetBuffer {
	// Factory to create new set of block
	private BlockSetFactory blockSetFactory = null;
	// Id attached to created next block set
	private int id = 0;
	// Buffer of blockset with id
	private Map<Integer, BlockSet> buffer = new HashMap<Integer, BlockSet>();
	// next id taken by clients
	private Map<WordisPlayer, Integer> clientTakenIds = new HashMap<WordisPlayer, Integer>();
	// Released id by clients
	private Map<Integer, Set<WordisPlayer>> releasedIds = new HashMap<Integer, Set<WordisPlayer>>();
	
	public BlockSetBuffer(final BlockSetFactory factory) {
		this.blockSetFactory = factory;
	}
	
	public void initBuffer() {
		id = 0;
		for (BlockSet bs : buffer.values()) {
			for (Block b : bs.getBlocks()) {
				BlockIdFactory.getInstance().dissociateId(b.getId());
			}
		}
		buffer.clear();
		for (Entry<WordisPlayer, Integer> entry : clientTakenIds.entrySet()) {
			entry.setValue(id);
		}
	}
	
	public void registerClient(final WordisPlayer client) {
		clientTakenIds.put(client, id);
	}
	
	public synchronized BlockSet requestBlockSet(final WordisPlayer client) throws BlockCreateException {
		final int takenId = clientTakenIds.get(client);
		if (takenId < id) {
			// Get specified block set from buffer.
			final BlockSet bs = buffer.get(takenId);
			// Update taken id.
			clientTakenIds.put(client, takenId+1);
			// If block set of takenId is taken by all client, remove it.
			if (isIdTakenByOthers(takenId)) {
				buffer.remove(takenId);
			}
			return bs.clone();
		} else {
			// Release next block set
			final BlockSet bs = blockSetFactory.create();
			// Register block set to buffer
			buffer.put(id, bs);
			// Update taken id.
			id += 1;
			clientTakenIds.put(client, id);
			return bs.clone();
		}
	}
	
	private boolean isIdTakenByOthers(final int takenId) {
		for (Integer clientId : clientTakenIds.values()) {
			if (takenId >= clientId) {
				return false;
			}
		}
		return true;
	}
	
	public void releaseIds(final WordisPlayer client, final Set<Integer> deletedIds) {
		final Set<Integer> releaseIdFromFactory = new HashSet<Integer>();
		for (int deletedId : deletedIds) {
			if (releasedIds.containsKey(deletedId)) {
				releasedIds.get(deletedIds).add(client);
			} else {
				final Set<WordisPlayer> clientSet = new HashSet<WordisPlayer>();
				clientSet.add(client);
				releasedIds.put(deletedId, clientSet);
			}
			// Check is released from all clients
			if (containsAllClient(releasedIds.get(deletedId))) {
				releaseIdFromFactory.add(deletedId);
			}
		}
		// Release from buffer and factory
		for (int releaseId : releaseIdFromFactory) {
			BlockIdFactory.getInstance().dissociateId(releaseId);
			releasedIds.remove(releaseId);
		}
	}
	
	public boolean containsAllClient(final Set<WordisPlayer> clients) {
		final Set<WordisPlayer> allClients = clientTakenIds.keySet();
		clients.removeAll(allClients);
		return clients.isEmpty();
	}
}
