package com.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.PrimaryKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.DetailImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.FormImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.SelectorImpl;
import com.engine.domain.wrapper.Path;
import com.engine.inspector.DataModelUtil;

@Service
public class NoAmServiceImpl implements NoAmService {

	private DataModelUtil dataModelUtil;
	private BlockService blockService;
	private EntryService entryService;
	private CollectionService collectionService;

	@Autowired
	public NoAmServiceImpl(BlockService blockService, EntryService entryService, CollectionService collectionService) {
		this.collectionService = collectionService;
		this.blockService = blockService;
		this.entryService = entryService;
	}

	@Override
	public List<Collection> computeAbstractModels(Path path) {

		blockService.setDataModelUtil(dataModelUtil);
		
		List<Collection> collections = new ArrayList<Collection>();
		System.out.println("--------- CREAZIONE DEL NO AM ---------");

		List<Entry> entries = new ArrayList<Entry>();
		List<InteractionFlowElement> interactionFlowElements = new ArrayList<InteractionFlowElement>();

		// create a collection for each VC of the path
		for (InteractionFlowElement interactionFlowElement : path.getInteractionFlowElements()) {
			interactionFlowElements.add(interactionFlowElement);

			entries = createEntries(interactionFlowElements);
			System.out.println(entries.size() + " entries");
			Block block = createBlock(interactionFlowElements, entries);
			System.out.println(block.getKey().getPartitionKeys().size() + " partition keys");
			System.out.println(block.getKey().getSortKeys().size() + " sort keys");

			Collection collection = createCollection(interactionFlowElements, path, block, entries);

			collections.add(collection);
		}
		System.out.println("--------- FINE NO AM ---------");

		return collections;
	}

	@Override
	public Collection createCollection(List<InteractionFlowElement> interactionFlowElements, Path path, Block block,
			List<Entry> entries) {
		Collection collection = new Collection(path.getIdPath() + "." + interactionFlowElements.size());
		collection.setPath(path);
		// get leaf node as name of the collection
		for (InteractionFlowElement interactionFlowElement : interactionFlowElements) {
			if (interactionFlowElement.getOutInteractionFlows() == null
					|| interactionFlowElement.getOutInteractionFlows().isEmpty())
				collection.setName(interactionFlowElement.getName());
		}
		// means there is a leaf pointing to an action
		if (collection.getName() == null)
			collection.setName(interactionFlowElements.get(interactionFlowElements.size() - 1).getName());
		;
		collection.setBlock(block);

		return collection;
	}

	@Override
	public Block createBlock(List<InteractionFlowElement> interactionFlowElements, List<Entry> entries) {

		Block block = new Block();
		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		PrimaryKey primaryKey = new PrimaryKey(partitionKeys, sortKeys);

		// collects all the entities that are navigated in the path
		List<com.engine.mapper.datamodel.DataModel.Entity> entities = new ArrayList<com.engine.mapper.datamodel.DataModel.Entity>();
		com.engine.mapper.datamodel.DataModel.Entity entity = null;

		// get all partition keys from conditions on links
		for (int i = 0; i < interactionFlowElements.size(); i++) {
			InteractionFlowElement current = interactionFlowElements.get(i);
			InteractionFlowElement next = null;

			if ((i + 1) < interactionFlowElements.size())
				next = interactionFlowElements.get(i + 1);

			if (current instanceof ListImpl && ((ListImpl) current).getEntity() != null) {
				entity = ((ListImpl) current).getEntity();
				entities.add(entity);
			}
			if (current instanceof DetailImpl && ((DetailImpl) current).getEntity() != null) {
				entity = ((DetailImpl) current).getEntity();
				entities.add(entity);
			}
			if (current instanceof FormImpl && ((FormImpl) current).getEntity() != null) {
				entity = ((FormImpl) current).getEntity();
				entities.add(entity);
			}
			if (current instanceof SelectorImpl && ((SelectorImpl) current).getEntity() != null) {
				entity = ((SelectorImpl) current).getEntity();
				entities.add(entity);
			}

			if (blockService.retrievePartitionKeysFromLink(current, next) != null) {
				partitionKeys.addAll(blockService.retrievePartitionKeysFromLink(current, next));
			}
			
			if (blockService.retrievePartitionKeysFromViewComponent(current) != null) {
				partitionKeys.addAll(blockService.retrievePartitionKeysFromViewComponent(current));
			}
			

		}

		// path length = 1 with no partition keys
		if (interactionFlowElements.size() == 1 && partitionKeys.isEmpty() && entities.get(0) != null) {
			partitionKeys.addAll(dataModelUtil.extractKeyAttributesIntoPartitionKeys(entities.get(0)));
		}

		// extracted attributes keys as sort key needed to guarantee uniqueness to the
		// record
		List<SortKey> extractedEntitiesKeys = new ArrayList<SortKey>();
		entities.stream().forEach(e -> extractedEntitiesKeys.addAll(dataModelUtil.extractKeyAttributesIntoSortKeys(e)));
		List<SortKey> distinctExtractedEntitiesKeys = extractedEntitiesKeys.stream().distinct()
				.collect(Collectors.toList());

		// removed duplicates on partition list
		partitionKeys = partitionKeys.stream().distinct().collect(Collectors.toList());

		// get all sort keys
		for (int s = 0; s < interactionFlowElements.size(); s++)
			sortKeys.addAll(blockService.retrieveSortKey(interactionFlowElements.get(s), partitionKeys));

		// removed duplicates on sort list
		sortKeys = sortKeys.stream().distinct().collect(Collectors.toList());
		// update sort keys: removed keys with ordering = null
		sortKeys = blockService.removeSortKeysWithOrderIsNull(sortKeys);

		// update sort keys: removed same keys with different order
		sortKeys = blockService.removeSortKeysWithDifferentOrder(sortKeys);

		// update partition key list by removing duplicates from sort keys
		partitionKeys = blockService.removeDuplicatesFromPartitionKeys(partitionKeys, sortKeys);

		// update sort list to guarantee uniqueness
		sortKeys = blockService.guaranteeRecordUniqueness(sortKeys, partitionKeys, distinctExtractedEntitiesKeys);

		// update partition key list by removing partition keys related to "weak"
		// entities (handled 1:N case)
		// partitionKeys = removeWeakPartitionKeys(partitionKeys);

		primaryKey.setPartitionKeys(partitionKeys);
		primaryKey.setSortKeys(sortKeys);

		block.setKey(primaryKey);

		// add partition keys in entries if does not exist
		entries = blockService.addEntriesFromKeys(partitionKeys, sortKeys, entries);

		block.setEntries(entries);

		return block;
	}

	@Override
	public List<Entry> createEntries(List<InteractionFlowElement> interactionFlowElements) {

		List<Entry> entries = new ArrayList<Entry>();

		for (InteractionFlowElement interactionFlowElement : interactionFlowElements) {

			if (interactionFlowElement instanceof ListImpl)
				entries.addAll(entryService.retrieveListDisplayAttributes((ListImpl) interactionFlowElement));

			if (interactionFlowElement instanceof DetailImpl)
				entries.addAll(entryService.retrieveDetailDisplayAttributes((DetailImpl) interactionFlowElement));

			if (interactionFlowElement instanceof SelectorImpl)
				entries.addAll(entryService.retrieveSelectorAttributes((SelectorImpl) interactionFlowElement));

			if (interactionFlowElement instanceof FormImpl)
				entries.addAll(entryService.retrieveFormAttributes((FormImpl) interactionFlowElement));

		}

		return entries;
	}

	/*
	 * Optimization on path scope
	 */
	@Override
	public List<Collection> pathOptimization(List<Collection> collections) {

		// entries aggregation
		List<Collection> optimizedCollections = optimizeEntries(collections);

		// collection aggregation
		optimizedCollections = optimizeCollections(collections);

		return collectionService.removeDuplicatesCollections(optimizedCollections);
	}

	@Override
	// TODO da finire ottimizzazione sul percorso
	public List<Collection> optimizeCollections(List<Collection> collections) {

		ArrayList<Collection> collectionsTemp = new ArrayList<Collection>(collections);
		ArrayList<Collection> collectionsTemp2 = new ArrayList<Collection>(collections);
		ArrayList<Collection> optimizedCollections = new ArrayList<Collection>();

		for (Collection collectionTemp : collectionsTemp) {

			String name = "(" + collectionTemp.getName() + ")";
			Integer countCollection = 1;
			ArrayList<PartitionKey> partitionKeys = new ArrayList<PartitionKey>(
					collectionTemp.getBlock().getKey().getPartitionKeys());
			ArrayList<SortKey> sortKeys = new ArrayList<SortKey>(collectionTemp.getBlock().getKey().getSortKeys());
			ArrayList<Entry> entries = new ArrayList<Entry>(collectionTemp.getBlock().getEntries());

			for (Collection collectionTemp2 : collectionsTemp2) {

				// comparing different collections
				if (!collectionTemp.getName().equals(collectionTemp2.getName())
						&& !collectionTemp.getId().equals(collectionTemp2.getId())) {

					if (blockService.haveSamePartitionKeys(collectionTemp2.getBlock().getKey().getPartitionKeys(),
							partitionKeys.stream().distinct().collect(Collectors.toList()))) {

						partitionKeys.addAll(collectionTemp2.getBlock().getKey().getPartitionKeys());

						sortKeys.addAll(collectionTemp2.getBlock().getKey().getSortKeys());
						sortKeys.addAll(collectionTemp.getBlock().getKey().getSortKeys());

						entries.addAll(collectionTemp2.getBlock().getEntries());
						entries.addAll(collectionTemp.getBlock().getEntries());

						name = name + "-" + "(" + collectionTemp2.getName() + ")";
						countCollection++;
					}
				}
			}

			if (!partitionKeys.isEmpty() && countCollection > 1) {

				Collection collection = new Collection(collectionTemp.getId());
				collection.setName(name);
				collection.setPath(collectionTemp.getPath());

				PrimaryKey primaryKey = new PrimaryKey(collectionTemp.getBlock().getKey().getPartitionKeys(),
						collectionTemp.getBlock().getKey().getSortKeys());
				primaryKey.getPartitionKeys().addAll(partitionKeys);
				primaryKey.getSortKeys().addAll(sortKeys);
				primaryKey.setPartitionKeys(
						primaryKey.getPartitionKeys().stream().distinct().collect(Collectors.toList()));
				primaryKey.setSortKeys(primaryKey.getSortKeys().stream().distinct().collect(Collectors.toList()));

				List<SortKey> sortKeysToAdd = blockService.removeSortKeysWithDifferentOrder(primaryKey.getSortKeys());
				sortKeysToAdd = blockService.removeDuplicatesFromSortKeys(primaryKey.getPartitionKeys(), sortKeysToAdd);

				primaryKey.setSortKeys(sortKeysToAdd);

				collection.getBlock().setKey(primaryKey);

				collection.getBlock().setEntries(collectionTemp.getBlock().getEntries());
				collection.getBlock().getEntries().addAll(entries);

				collection.getBlock()
						.setEntries(entryService.removeDuplicatesEntries(collection.getBlock().getEntries()));

				optimizedCollections.add(collection);

			}

		}

		return optimizedCollections;
	}

	/*
	 * @param collections to evaluate
	 * 
	 * @return new collection with field aggregated. For each collection are grouped
	 * all the fields by interaction flow element id. Then they are compared one by
	 * one and aggregated all the fields that brings more than one field related to
	 * the same entity
	 */
	@Override
	public List<Collection> optimizeEntries(List<Collection> collections) {

		for (Collection collection : collections) {

			Map<String, List<Entry>> entriesGrouped = collection.getBlock().getEntries().stream()
					.collect(Collectors.groupingBy(w -> w.getInteractionFlowElementName()));

			Map<String, List<Entry>> entriesGroupedTemp = new HashMap<String, List<Entry>>(entriesGrouped);

			for (Map.Entry<String, List<Entry>> e : entriesGrouped.entrySet()) {

				List<Entry> candidateAggregateEntries = new ArrayList<Entry>();

				for (Map.Entry<String, List<Entry>> e2 : entriesGroupedTemp.entrySet()) {

					if (!e.getKey().equals(e2.getKey())) {

						for (Entry entry : e.getValue()) {
							for (Entry entry2 : e2.getValue()) {
								if (entry.getId().equals(entry2.getId())
										&& !entryService.aggregateHasBeenAlreadyAdded(entry, candidateAggregateEntries)
										&& !entry.isAKey(collection.getBlock().getKey()))

									candidateAggregateEntries.add(entry);
							}
						}

					}
				}

				// must be found at least 2 aggregated elements between interaction flow
				// elements and aggregate entries must be present in all view components having
				// same entity
				if (candidateAggregateEntries.size() > 1
						&& entryService.checkPresence(candidateAggregateEntries, collection.getPath())) {
					Entry aggregateEntry = new Entry(candidateAggregateEntries);
					List<Entry> entryTemp = new ArrayList<Entry>(collection.getBlock().getEntries());

					for (Entry entry : entryTemp) {
						for (Entry entry2 : candidateAggregateEntries) {
							if (entry.getId().equals(entry2.getId())) {
								collection.getBlock().getEntries().remove(entry);
							}
						}
					}
					if (!entryService.aggregateHasBeenAlreadyAdded(aggregateEntry, collection.getBlock().getEntries()))
						collection.getBlock().getEntries().add(aggregateEntry);
				}

			}
			collection.getBlock().removeDuplicatesEntries();
		}

		return collections;
	}

	public DataModelUtil getDataModelUtil() {
		return dataModelUtil;
	}

	public void setDataModelUtil(DataModelUtil dataModelUtil) {
		this.dataModelUtil = dataModelUtil;
	}

	/*
	 * Optimization over page scope
	 */
	@Override
	public List<Collection> pageOptimization(List<Collection> collectionsPageScope) {
		// collection aggregation
		return collectionService.removeDuplicatesCollections(
				optimizeCollections(collectionService.removeDuplicatesCollections(collectionsPageScope)));
	}

}
