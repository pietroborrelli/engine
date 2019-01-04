package com.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.PrimaryKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.enumeration.Ordering;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.WrapperAttribute;
import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.DetailImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.FormImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.SelectorImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.Field;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.FieldImpl;
import com.engine.domain.wrapper.Path;

@Service
public class NoAmServiceImpl implements NoAmService {

	@Override
	public List<Collection> computeAbstractModelsByPaths(List<Path> paths) {

		List<Collection> collections = new ArrayList<Collection>();
		System.out.println("--------- CREAZIONE DEL NO AM ---------");

		for (Path path : paths) {

			List<Entry> entries = createEntries(path);
			System.out.println(entries.size() + " entries");

			Block block = createBlock(path, entries);
			System.out.println(block.getKey().getPartitionKeys().size() + " partition keys");
			System.out.println(block.getKey().getSortKeys().size() + " sort keys");

			Collection collection = createCollection(path, block, entries);

			collections.add(collection);
		}

		return collections;
	}

	@Override
	public Collection createCollection(Path path, Block block, List<Entry> entries) {
		Collection collection = new Collection(path.getIdPath());
		collection.setPath(path);
		// get leaf node as name of the collection
		for (InteractionFlowElement interactionFlowElement : path.getInteractionFlowElements()) {
			if (interactionFlowElement.getOutInteractionFlows() == null
					|| interactionFlowElement.getOutInteractionFlows().isEmpty())
				collection.setName(interactionFlowElement.getName());
		}
		//means there is a leaf pointing to an action
		if (collection.getName()==null)
			collection.setName(path.getInteractionFlowElements().get(path.getInteractionFlowElements().size()-1).getName());;
		collection.setBlock(block);

		return collection;
	}

	@Override
	public Block createBlock(Path path, List<Entry> entries) {

		Block block = new Block();
		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		PrimaryKey primaryKey = new PrimaryKey(partitionKeys, sortKeys);

		System.out.println("Estrazione delle chiavi su percorso n." + path.getIdPath());

		// get all partition keys
		for (int i = 0; i < path.getInteractionFlowElements().size(); i++) {
			InteractionFlowElement current = path.getInteractionFlowElements().get(i);
			InteractionFlowElement next = null;

			if ((i + 1) < path.getInteractionFlowElements().size())
				next = path.getInteractionFlowElements().get(i + 1);

			if (retrievePartitionKey(current, next) != null)
				partitionKeys.addAll(retrievePartitionKey(current, next));

		}

		// removed duplicates on partition list
		partitionKeys = partitionKeys.stream().distinct().collect(Collectors.toList());

		// get all sort keys
		for (int s = 0; s < path.getInteractionFlowElements().size(); s++)
			sortKeys.addAll(retrieveSortKey(path.getInteractionFlowElements().get(s), partitionKeys));

		// removed duplicates on sort list
		sortKeys = sortKeys.stream().distinct().collect(Collectors.toList());

		// update partition key list by removing duplicates from sort keys
		partitionKeys = removeDuplicatesFromPartitionKeys(partitionKeys, sortKeys);

		// update sort keys by removing duplicates keys with different ordering
		sortKeys = removeDuplicatesFromSortKeys(sortKeys);

		primaryKey.setPartitionKeys(partitionKeys);
		primaryKey.setSortKeys(sortKeys);

		block.setKey(primaryKey);
		block.setEntries(entries);

		return block;
	}

	
	
	
	/**
	 * @param sortKeys
	 * @return new sort keys without duplicate sort keys that differ only for
	 *         ordering. given priority to DESC order
	 */
	private List<SortKey> removeDuplicatesFromSortKeys(List<SortKey> sortKeys) {

		List<SortKey> tempSortKeys = new ArrayList<SortKey>(sortKeys);

		for (int i = 0; i < tempSortKeys.size() - 1; i++) {

			if (tempSortKeys.get(i).getId().equals(tempSortKeys.get(i + 1).getId())) {

				if (tempSortKeys.get(i).getOrdering().equals(Ordering.ASCENDING))
					sortKeys.remove(tempSortKeys.get(i));
				else
					sortKeys.remove(tempSortKeys.get(i + 1));
			}

		}

		return sortKeys;
	}

	/**
	 * @param partitionKeys
	 * @param sortKeys
	 * @return new partition keys list without keys that are already candidates to
	 *         be sort keys
	 */
	private List<PartitionKey> removeDuplicatesFromPartitionKeys(List<PartitionKey> partitionKeys,
			List<SortKey> sortKeys) {

		List<PartitionKey> tempPartitionKeys = new ArrayList<PartitionKey>(partitionKeys);

		for (PartitionKey partitionKey : tempPartitionKeys) {
			for (SortKey sortKey : sortKeys) {

				if (partitionKey.getId().equals(sortKey.getId()))
					partitionKeys.remove(partitionKey);
			}
		}

		return partitionKeys;
	}

	/**
	 * @param interactionFlowElement
	 * @param partitionKeys
	 * @return sort keys according with the type of the view component
	 */
	private List<SortKey> retrieveSortKey(InteractionFlowElement interactionFlowElement,
			List<PartitionKey> partitionKeys) {

		List<SortKey> sortKeys = new ArrayList<SortKey>();

		// 1. In case is LIST
		if (interactionFlowElement instanceof ListImpl) {

			if (((ListImpl) interactionFlowElement).getSortAttributes() != null) {
				// get sort attributes if already in the group of the partition keys
				for (Attribute sortAttribute : ((ListImpl) interactionFlowElement).getSortAttributes()) {
					// if has been specified an ordering over the partition keys or attributes
					// conditions
					
					//if (partitionKeysHaveSortAttribute(partitionKeys, sortAttribute)) {
						SortKey sortKey = extractSortKeyFromApplicationModel(sortAttribute);
						sortKeys.add(sortKey);
					//}
				}
			}
			// get attributes of attributes conditions if already in the group of the
			// partition keys
			for (ConditionalExpression conditionalExpression : ((ListImpl) interactionFlowElement)
					.getConditionalExpressions()) {
				for (Condition condition : conditionalExpression.getConditions()) {

					if (condition instanceof AttributesCondition) {
						AttributesCondition attributesCondition = (AttributesCondition) condition;
						if (attributesCondition.getAttributes() != null) {
							for (WrapperAttribute wrapperAttribute : attributesCondition.getAttributes()) {

								//if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
								//		attributesCondition)) {
									SortKey sortKey = extractSortKeyFromDataModel(attributesCondition,
											wrapperAttribute);
									sortKeys.add(sortKey);
									//}
							}
						}
					}
				}
			}
		}

		// 2. In case is SELECTOR
		if (interactionFlowElement instanceof SelectorImpl) {

			if (((SelectorImpl) interactionFlowElement).getSortAttributes() != null) {
				// get sort attributes if already in the group of the partition keys
				for (Attribute sortAttribute : ((SelectorImpl) interactionFlowElement).getSortAttributes()) {
					// if has been specified an ordering over the partition keys or attributes
					// conditions
					
					//if (partitionKeysHaveSortAttribute(partitionKeys, sortAttribute)) {
						SortKey sortKey = extractSortKeyFromApplicationModel(sortAttribute);
						sortKeys.add(sortKey);
					//}
				}
			}
			// get attributes of attributes conditions if already in the group of the
			// partition keys
			for (ConditionalExpression conditionalExpression : ((SelectorImpl) interactionFlowElement)
					.getConditionalExpressions()) {
				for (Condition condition : conditionalExpression.getConditions()) {

					if (condition instanceof AttributesCondition) {
						AttributesCondition attributesCondition = (AttributesCondition) condition;
						if (attributesCondition.getAttributes() != null) {
							for (WrapperAttribute wrapperAttribute : attributesCondition.getAttributes()) {

								//	if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
								//			attributesCondition)) {
									SortKey sortKey = extractSortKeyFromDataModel(attributesCondition,
											wrapperAttribute);
									sortKeys.add(sortKey);
									//	}
							}
						}
					}
				}
			}
		}

		// 3. In case is DETAIL
		if (interactionFlowElement instanceof DetailImpl) {

			// get attributes of attributes conditions if already in the group of the
			// partition keys
			for (ConditionalExpression conditionalExpression : ((DetailImpl) interactionFlowElement)
					.getConditionalExpressions()) {
				for (Condition condition : conditionalExpression.getConditions()) {

					if (condition instanceof AttributesCondition) {
						AttributesCondition attributesCondition = (AttributesCondition) condition;
						for (WrapperAttribute wrapperAttribute : attributesCondition.getAttributes()) {

							//		if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
							//			attributesCondition)) {
								SortKey sortKey = extractSortKeyFromDataModel(attributesCondition, wrapperAttribute);
								sortKeys.add(sortKey);
								//	}
						}
					}
				}
			}
		}

		return sortKeys;
	}

	private SortKey extractSortKeyFromApplicationModel(Attribute attribute) {
		SortKey sortKey = new SortKey(attribute.getId());
		sortKey.setName(attribute.getName());
		sortKey.setType(attribute.getType());
		sortKey.setEntity(attribute.getEntity().getName());
		sortKey.setOrdering(attribute.getOrdering());
		return sortKey;
	}

	private SortKey extractSortKeyFromDataModel(AttributesCondition attributesCondition,
			WrapperAttribute wrapperAttribute) {
		SortKey sortKey = new SortKey(wrapperAttribute.getId());
		sortKey.setName(wrapperAttribute.getAttribute().getName());
		sortKey.setType(wrapperAttribute.getAttribute().getType());
		sortKey.setEntity(wrapperAttribute.getEntity().getName());

		if (attributesCondition.getPredicate().equals("lt") || attributesCondition.getPredicate().equals("lteq"))
			sortKey.setOrdering(Ordering.ASCENDING);
		if (attributesCondition.getPredicate().equals("gt") || attributesCondition.getPredicate().equals("gteq"))
			sortKey.setOrdering(Ordering.DESCENDING);

		return sortKey;
	}

	/**
	 * @param partitionKeys
	 * @param wrapperAttribute
	 * @param attributesCondition
	 * @return true if the attribute of the attributes condition is already present
	 *         in the partition keys group and for which is specified an ordering in
	 *         the predicate, otherwise false
	 */
	private boolean partitionKeysHaveAttributeConditions(List<PartitionKey> partitionKeys,
			WrapperAttribute wrapperAttribute, AttributesCondition attributesCondition) {
		Boolean found = false;
		for (PartitionKey partitionKey : partitionKeys) {
			if (partitionKey.getId().equals(wrapperAttribute.getId())
					&& (attributesCondition.getPredicate().equals("lt")
							|| attributesCondition.getPredicate().equals("lteq")
							|| attributesCondition.getPredicate().equals("gt")
							|| attributesCondition.getPredicate().equals("gteq")))
				found = true;
		}
		return found;
	}

	/**
	 * @param partitionKeys
	 * @param sortAttribute
	 * @return true if the sort attribute is already present in the partition keys
	 *         group, otherwise false
	 */
	private Boolean partitionKeysHaveSortAttribute(List<PartitionKey> partitionKeys, Attribute sortAttribute) {
		Boolean found = false;
		for (PartitionKey partitionKey : partitionKeys) {
			if (partitionKey.getId().equals(sortAttribute.getId()))
				found = true;
		}
		return found;
	}

	/**
	 * @param ife:
	 *            interaction flow element from which extract attribute(s)
	 * @return partition key computed from the source of the binding parameter; if
	 *         the source is null means that is a field of a form which has not an
	 *         attribute set. Retrieved firstly all the sources of the binding
	 *         parameter and then all the targets. Are generated duplicates Return
	 *         null if there is no binding parameter
	 */
	private List<PartitionKey> retrievePartitionKey(InteractionFlowElement ife, InteractionFlowElement nextIfe) {

		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();

		for (InteractionFlow outInteractionFlow : ife.getOutInteractionFlows()) {

			// looks for the right pointing link to the next interaction flow element; if
			// nextIfe is null, a leaf is reached
			if (nextIfe == null || outInteractionFlow.getTo().equals(nextIfe.getId())) {

				for (BindingParameter bindingParameter : outInteractionFlow.getBindingParameter()) {

					// TODO remove this condition when handle actions
					if (bindingParameter.getSources() == null || bindingParameter.getTargets() == null)
						return null;

					// get all sources of binding parameters
					for (int x = 0; x < bindingParameter.getSources().size(); x++) {

						if (bindingParameter.getSources().get(x) instanceof Attribute) {
							Attribute sourceAttribute = (Attribute) bindingParameter.getSources().get(x);

							if (sourceAttribute.getId() != null) {
								PartitionKey partitionKey = new PartitionKey(sourceAttribute.getId());
								partitionKey.setName(sourceAttribute.getName());
								partitionKey.setType(sourceAttribute.getType());
								partitionKey.setEntity(sourceAttribute.getEntity().getName());

								partitionKeys.add(partitionKey);

							}
						}
					}

					// get all targets of binding parameters
					for (int y = 0; y < bindingParameter.getTargets().size(); y++) {

						if (bindingParameter.getTargets().get(y) instanceof Attribute) {
							Attribute targetAttribute = (Attribute) bindingParameter.getTargets().get(y);

							if (targetAttribute.getId() != null) {
								if (bindingParameter.getTargets().get(y) instanceof Attribute) {
									PartitionKey partitionKey = new PartitionKey(targetAttribute.getId());
									partitionKey.setName(targetAttribute.getName());
									partitionKey.setType(targetAttribute.getType());
									partitionKey.setEntity(targetAttribute.getEntity().getName());

									partitionKeys.add(partitionKey);
								}
							}

						}

					}

				}
				return partitionKeys;
			}
		}

		return null;
	}

	@Override
	public List<Entry> createEntries(Path path) {

		List<Entry> entries = new ArrayList<Entry>();

		for (InteractionFlowElement interactionFlowElement : path.getInteractionFlowElements()) {

			if (interactionFlowElement instanceof ListImpl)
				entries.addAll(retrieveListDisplayAttributes((ListImpl) interactionFlowElement));

			if (interactionFlowElement instanceof DetailImpl)
				entries.addAll(retrieveDetailDisplayAttributes((DetailImpl) interactionFlowElement));

			if (interactionFlowElement instanceof SelectorImpl)
				entries.addAll(retrieveSelectorAttributes((SelectorImpl) interactionFlowElement));

			if (interactionFlowElement instanceof FormImpl)
				entries.addAll(retrieveFormAttributes((FormImpl) interactionFlowElement));

		}

		return entries;
	}

	/**
	 * @param formImpl
	 * @return attributes of ONLY fields
	 */
	private List<Entry> retrieveFormAttributes(FormImpl formImpl) {

		List<Entry> entries = new ArrayList<Entry>();
		if (formImpl.getFields() == null || formImpl.getFields().isEmpty())
			return entries;

		for (Field field : formImpl.getFields()) {
			if (field instanceof FieldImpl && ((FieldImpl) field).getAttribute().getId() != null) {
				Entry entry = new Entry();
				entry.setId(((FieldImpl) field).getAttribute().getId());
				entry.setName(((FieldImpl) field).getAttribute().getName());
				entry.setType(((FieldImpl) field).getAttribute().getType());
				entry.setEntityName(((FieldImpl) field).getAttribute().getEntity().getName());
				entry.setInteractionFlowElementName(formImpl.getId());

				entries.add(entry);
			}
		}
		return entries;
	}

	/**
	 * @param listImpl
	 * @return display attributes of the list
	 */
	private List<Entry> retrieveListDisplayAttributes(ListImpl listImpl) {

		List<Entry> entries = new ArrayList<Entry>();

		if (listImpl.getDisplayAttributes() == null || listImpl.getDisplayAttributes().isEmpty())
			return entries;

		for (Attribute displayAttribute : listImpl.getDisplayAttributes()) {
			Entry entry = new Entry();
			entry.setId(displayAttribute.getId());
			entry.setName(displayAttribute.getName());
			entry.setType(displayAttribute.getType());
			entry.setInteractionFlowElementName(listImpl.getId());

			if (displayAttribute.getEntity() != null)
				entry.setEntityName(displayAttribute.getEntity().getName());

			entries.add(entry);

		}
		return entries;
	}

	/**
	 * @param detailImpl
	 * @return display attributes of the detail
	 */
	private List<Entry> retrieveDetailDisplayAttributes(DetailImpl detailImpl) {

		List<Entry> entries = new ArrayList<Entry>();
		if (detailImpl.getDisplayAttributes() == null || detailImpl.getDisplayAttributes().isEmpty())
			return entries;

		for (Attribute displayAttribute : detailImpl.getDisplayAttributes()) {
			Entry entry = new Entry();
			entry.setId(displayAttribute.getId());
			entry.setName(displayAttribute.getName());
			entry.setType(displayAttribute.getType());
			entry.setInteractionFlowElementName(detailImpl.getId());

			if (displayAttribute.getEntity() != null)
				entry.setEntityName(displayAttribute.getEntity().getName());

			entries.add(entry);

		}
		return entries;
	}

	/**
	 * @param selectorImpl
	 * @return attributes selected by the selector
	 */
	private List<Entry> retrieveSelectorAttributes(SelectorImpl selectorImpl) {

		List<Entry> entries = new ArrayList<Entry>();
		if (selectorImpl.getAttributes() == null || selectorImpl.getAttributes().isEmpty())
			return entries;

		for (com.engine.mapper.datamodel.DataModel.Entity.Attribute attribute : selectorImpl.getAttributes().values()) {
			Entry entry = new Entry();
			entry.setId(attribute.getId());
			entry.setName(attribute.getName());
			entry.setType(attribute.getType());
			entry.setInteractionFlowElementName(selectorImpl.getId());

			if (selectorImpl.getEntity() != null)
				entry.setEntityName(selectorImpl.getEntity().getName());

			entries.add(entry);

		}
		return entries;
	}

	/* 
	 * @param collections to evaluate
	 * @return new collection with field aggregated.
	 * For each collection are grouped all the fields by interaction flow element id. Then they are compared one by one and aggregated all the fields
	 * that brings more than one field related to the same entity
	 */
	@Override
	public List<Collection> optimizeReadingAccessPaths(List<Collection> collections) {

		for (Collection collection : collections) {

			Map<String, List<Entry>> entriesGrouped = collection.getBlock().getEntries().stream()
					.collect(Collectors.groupingBy(w -> w.getInteractionFlowElementName()));

			Map<String, List<Entry>> entriesGroupedTemp = new HashMap<String, List<Entry>>(entriesGrouped);

			for (Map.Entry<String, List<Entry>> e : entriesGrouped.entrySet()) {

				List<Entry> aggregateEntries = new ArrayList<Entry>();
				for (Map.Entry<String, List<Entry>> e2 : entriesGroupedTemp.entrySet()) {

					if (!e.getKey().equals(e2.getKey())) {

						for (Entry entry : e.getValue()) {
							for (Entry entry2 : e2.getValue()) {
								if (entry.getId().equals(entry2.getId()) && !aggregateHasBeenAlreadyAdded(entry,aggregateEntries) && !entry.isAKey(collection.getBlock().getKey()))
									aggregateEntries.add(entry);
							}
						}

					}
				}
				
				// must be found at least 2 aggregated elements between interaction flow elements
				if (aggregateEntries.size() > 1) {
					Entry aggregateEntry = new Entry(aggregateEntries);
					List<Entry> entryTemp = new ArrayList<Entry>(collection.getBlock().getEntries());

					for (Entry entry : entryTemp) {
						for (Entry entry2 : aggregateEntries) {
							if (entry.getId().equals(entry2.getId())) {
								collection.getBlock().getEntries().remove(entry);
							}
						}
					}
					if (!aggregateHasBeenAlreadyAdded(aggregateEntry, collection.getBlock().getEntries()))
						collection.getBlock().getEntries().add(aggregateEntry);
				}

			}
			collection.getBlock().removeDuplicatesEntries();
		}

		return collections;
	}



	/**
	 * @param aggregateEntry
	 * @param entries
	 * @return true if aggregate has been already inserted in the entry list of the block
	 */
	private boolean aggregateHasBeenAlreadyAdded(Entry aggregateEntry, List<Entry> entries) {

		Boolean found = false;

		for (Entry entry : entries) {
			if (entry.getId().equals(aggregateEntry.getId()))
				found = true;
		}
		return found;

	}
	
	
	
	
}
