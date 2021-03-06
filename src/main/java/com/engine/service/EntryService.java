package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.enumeration.Predicate;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.DetailImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.FormImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.SelectorImpl;
import com.engine.domain.wrapper.Path;

public interface EntryService {

	/**
	 * @param formImpl
	 * @return attributes of ONLY fields
	 */
	List<Entry> retrieveFormAttributes(FormImpl formImpl);

	/**
	 * @param listImpl
	 * @return display attributes of the list
	 */
	List<Entry> retrieveListDisplayAttributes(ListImpl listImpl);

	/**
	 * @param detailImpl
	 * @return display attributes of the detail
	 */
	List<Entry> retrieveDetailDisplayAttributes(DetailImpl detailImpl);

	/**
	 * @param selectorImpl
	 * @return attributes selected by the selector
	 */
	List<Entry> retrieveSelectorAttributes(SelectorImpl selectorImpl);
	
	List<Entry> removeDuplicatesEntries(List<Entry> entries);
	
	boolean haveSameEntries(List<Entry> entries, List<Entry> entries2) ;
	
	boolean aggregateHasBeenAlreadyAdded(Entry aggregateEntry, List<Entry> entries);
	
	boolean checkPresence(List<Entry> candidateAggregateEntries, Path path);

	Predicate findPredicate(InteractionFlowElement interactionFlowElement, String idAttribute);

	String findValueCondition(InteractionFlowElement interactionFlowElement, String idAttribute);

	/**
	 * @param partitionKeys
	 * @param entries
	 * @return entries enriched with partition keys and sort keys, needed for the
	 *         script of the physical model
	 */
	List<Entry> addEntriesFromKeys(List<PartitionKey> partitionKeys, List<SortKey> sortKeys, List<Entry> entries);

	List<Entry> findEntriesConditions(InteractionFlowElement interactionFlowElement);

}