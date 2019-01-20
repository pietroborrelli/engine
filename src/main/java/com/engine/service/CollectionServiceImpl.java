package com.engine.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Collection;

@Service
public class CollectionServiceImpl implements CollectionService {

	private BlockService blockService;
	private EntryService entryService;
	
	@Autowired
	public CollectionServiceImpl(BlockService blockService, EntryService entryService) {
		this.blockService = blockService;
		this.entryService = entryService;
	}

	/* (non-Javadoc)
	 * @see com.engine.service.CollectionService#removeDuplicatesCollections(java.util.List)
	 */
	@Override
	public List<Collection> removeDuplicatesCollections(List<Collection> collections) {

		ArrayList<Collection> collectionsTemp = new ArrayList<Collection>(collections);
		ArrayList<Collection> collectionsTemp2 = new ArrayList<Collection>(collections);
		ArrayList<Collection> newCollection = new ArrayList<Collection>();

		for (Collection collectionTemp : collectionsTemp) {

			for (Collection collectionTemp2 : collectionsTemp2) {
				// comparing different collections
				if (	!collectionTemp.getName().equals(collectionTemp2.getName()) && !collectionTemp.getId().equals(collectionTemp2.getId())
						&& blockService.haveSamePartitionKeys(collectionTemp.getBlock().getKey().getPartitionKeys(),
								collectionTemp2.getBlock().getKey().getPartitionKeys())
						&& blockService.haveSameSortKeys(collectionTemp.getBlock().getKey().getSortKeys(),
								collectionTemp2.getBlock().getKey().getSortKeys())
						&& entryService.haveSameEntries(collectionTemp.getBlock().getEntries(),
								collectionTemp2.getBlock().getEntries())
						&& !collectionHasAlreadyBeenAdded(collectionTemp2, newCollection)) {

					newCollection.add(collectionTemp2);

				}

			}
		}
		collections.removeAll(newCollection);
		return collections;
	}
	
	/* (non-Javadoc)
	 * @see com.engine.service.CollectionService#collectionHasAlreadyBeenAdded(com.engine.domain.abstractmodel.Collection, java.util.List)
	 */
	@Override
	public boolean collectionHasAlreadyBeenAdded(Collection collection, List<Collection> alreadyAddedCollections) {

		Boolean found = false;

		for (Collection alreadyAddedCollection : alreadyAddedCollections) {

			if (blockService.haveSamePartitionKeys(collection.getBlock().getKey().getPartitionKeys(),
					alreadyAddedCollection.getBlock().getKey().getPartitionKeys())
					&& blockService.haveSameSortKeys(collection.getBlock().getKey().getSortKeys(),
							alreadyAddedCollection.getBlock().getKey().getSortKeys())
					&& entryService.haveSameEntries(collection.getBlock().getEntries(),
							alreadyAddedCollection.getBlock().getEntries()))
				found = true;
		}

		return found;

	}
	
	
	
}
