package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Collection;

public interface CollectionService {

	/**
	 * @param collections
	 * @return list of collection without duplicates (comparison based on the keys
	 *         and entries)
	 */
	List<Collection> removeDuplicatesCollections(List<Collection> collections);

	/**
	 * @param aggregateEntry
	 * @param entries
	 * @return true if collection has been already inserted in the new collection
	 *         list,
	 */
	boolean collectionHasAlreadyBeenAdded(Collection collection, List<Collection> alreadyAddedCollections);

}