package com.kkbnart.wordis.game.object;

import java.util.HashSet;
import java.util.Set;

import com.kkbnart.wordis.game.exception.BlockCreateException;

/**
 * Retain ids set of all blocks and assign ids to new blocks <br>
 * 
 * @author kkbnart
 */
public class BlockIdFactory {
	// To reduce calculation cost, restrict max number of id
	// Change here to expand board size
	private static final int MAX_ID = 10000;
	
	// Singleton instance for this class
	private static BlockIdFactory instance = null;
	
	// Next block id to assign
	private int nextId;
	// Ids already assigned
	private Set<Integer> assignedIds = new HashSet<Integer>();
	
	public static synchronized BlockIdFactory getInstance() {
		if (instance == null) {
			instance = new BlockIdFactory();
		}
		return instance;
	}
	
	private BlockIdFactory() {
		nextId = 0;
	}
	
	/**
	 * Assign {@code num} ids which are not assigned. <br>
	 * <b> Do not forget to dissociate assigned ids when deleted,
	 * to call {@link #dissociateId} or {@link #dissociateIds} </b>
	 * 
	 * @param num Number of ids to assign
	 * @return Array of Ids
	 * @throws BlockCreateException If sufficient ids are not available
	 */
	public int[] assignIds(final int num) throws BlockCreateException {
		final int end = nextId == 0 ? MAX_ID : nextId-1;
		int[] ids = new int[num];
		int assigned = 0;
		// Loop until num of ids are assigned
		while (assigned < num) {
			if (assignedIds.contains(nextId)) {
				// All possible candidates for ids are duplicated, throw cannot create error
				if (nextId == end) {
					throw new BlockCreateException(BlockCreateException.ID_OVERFLOW);
				}
			} else {
				ids[assigned++] = nextId;
				assignedIds.add(nextId);
			}
			// Iterate next id
			nextId = nextId == MAX_ID ? 0 : nextId+1;
		}
		return ids;
	}
	
	/**
	 * Dissociate {@code id} from assigned id set. <br>
	 * <b> 
	 * 
	 * @param id Id to be dissociated
	 */
	public void dissociateId(final int id) {
		assignedIds.remove(Integer.valueOf(id));
		if (nextId > id) nextId = id;
	}
	
	/**
	 * Dissociate {@code ids} from assinged id set. <br>
	 * 
	 * @param ids Id set to be dissociated
	 */
	public void dissociateIds(final Set<Integer> ids) {
		for (Integer id : ids) {
			dissociateId(id);
		}
	}
}
