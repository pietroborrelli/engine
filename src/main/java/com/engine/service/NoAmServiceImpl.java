package com.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.MultipleSelectionFieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.SelectionFieldImpl;
import com.engine.domain.wrapper.Path;
import com.engine.inspector.DataModelUtil;
import com.engine.mapper.datamodel.DataModel.Relationship;

@Service
public class NoAmServiceImpl implements NoAmService {

	public DataModelUtil dataModelUtil;

	@Override
	public List<Collection> computeAbstractModels(Path path) {

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
		
		//collects all the entities that are navigated in the path
		List<com.engine.mapper.datamodel.DataModel.Entity> entities = new ArrayList<com.engine.mapper.datamodel.DataModel.Entity>();
		com.engine.mapper.datamodel.DataModel.Entity entity=null;
		
		// get all partition keys
		for (int i = 0; i < interactionFlowElements.size(); i++) {
			InteractionFlowElement current = interactionFlowElements.get(i);
			InteractionFlowElement next = null;

			if ((i + 1) < interactionFlowElements.size())
				next = interactionFlowElements.get(i + 1);

			if (current instanceof ListImpl && ((ListImpl)current).getEntity()!=null) {
				entity = ((ListImpl)current).getEntity();
				entities.add(entity);
			}
			if (current instanceof DetailImpl && ((DetailImpl)current).getEntity()!=null) {
				entity = ((DetailImpl)current).getEntity();
				entities.add(entity);
			}
			if (current instanceof FormImpl && ((FormImpl)current).getEntity()!=null) {
				entity = ((FormImpl)current).getEntity();
				entities.add(entity);
			}
			if (current instanceof SelectorImpl && ((SelectorImpl)current).getEntity()!=null) {
				entity = ((SelectorImpl)current).getEntity();
				entities.add(entity);
			}
			
			
			if (retrievePartitionKey(current, next) != null) {
				partitionKeys.addAll(retrievePartitionKey(current, next));
			}

		}
		
		//extracted attributes keys as sort key needed to guarantee uniqueness to the record
		List<SortKey> extractedEntitiesKeys = new ArrayList<SortKey>();
		entities.stream().forEach(e -> extractedEntitiesKeys.addAll(dataModelUtil.extractKeyAttributesIntoSortKeys(e)));
		List<SortKey> distinctExtractedEntitiesKeys = extractedEntitiesKeys.stream().distinct().collect(Collectors.toList());
		
		// removed duplicates on partition list
		partitionKeys = partitionKeys.stream().distinct().collect(Collectors.toList());

		// get all sort keys
		for (int s = 0; s < interactionFlowElements.size(); s++)
			sortKeys.addAll(retrieveSortKey(interactionFlowElements.get(s), partitionKeys));

		// removed duplicates on sort list
		sortKeys = sortKeys.stream().distinct().collect(Collectors.toList());
		// update sort keys: removed keys with ordering = null
		sortKeys = removeSortKeysWithOrderIsNull(sortKeys);

		// update sort keys: removed same keys with different order
		sortKeys = removeSortKeysWithDifferentOrder(sortKeys);

		// update partition key list by removing duplicates from sort keys
		partitionKeys = removeDuplicatesFromPartitionKeys(partitionKeys, sortKeys);

		// update sort list to guarantee uniqueness
		sortKeys = guaranteeRecordUniqueness(sortKeys,partitionKeys,distinctExtractedEntitiesKeys);
		
		// update partition key list by removing partition keys related to "weak"
		// entities (handled 1:N case)
		// partitionKeys = removeWeakPartitionKeys(partitionKeys);

		primaryKey.setPartitionKeys(partitionKeys);
		primaryKey.setSortKeys(sortKeys);

		block.setKey(primaryKey);

		// add partition keys in entries if does not exist
		entries = addEntriesFromKeys(partitionKeys, sortKeys, entries);

		block.setEntries(entries);

		return block;
	}

	/**
	 * @param sortKeys
	 * @param partitionKeys
	 * @param distinctExtractedEntitiesKeys
	 * @return new sort key list comprising the attributes id of the entities, in order to guarantee uniqueness of the record.
	 */
	private List<SortKey> guaranteeRecordUniqueness(List<SortKey> sortKeys, List<PartitionKey> partitionKeys,
			List<SortKey> distinctExtractedEntitiesKeys) {
		
		if (partitionKeys.isEmpty())
			return sortKeys;
		
		List<SortKey> newSortKeys = new ArrayList<SortKey>(sortKeys);
		
		for (SortKey extractedSortKey : distinctExtractedEntitiesKeys) {
			boolean foundInSortKeys = false;
			boolean foundInPartitionKeys = false;
			
			for (SortKey sortKey : sortKeys) {
				if (extractedSortKey.getId().equals(sortKey.getId()))
					foundInSortKeys=true;
			}
			
			for (PartitionKey partitionKey : partitionKeys) {
				if (extractedSortKey.getId().equals(partitionKey.getId()))
					foundInPartitionKeys=true;
			}
			
			if (!foundInSortKeys && !foundInPartitionKeys)
				newSortKeys.add(extractedSortKey);
			
		}

		return newSortKeys.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * Sort keys with order = null comes from attributes conditions with "equal"
	 * predicate
	 * 
	 * @param sortKeys
	 * @return keys without null ordering
	 */
	private List<SortKey> removeSortKeysWithOrderIsNull(List<SortKey> sortKeys) {
		List<SortKey> tempSortKeys = new ArrayList<SortKey>(sortKeys);

		for (SortKey sortKey : tempSortKeys) {

			if (sortKey.getOrdering() == null) {

				sortKeys.remove(sortKey);

			}
		}

		return sortKeys;
	}

	/**
	 * @param partitionKeys
	 * @param entries
	 * @return entries enriched with partition keys and sort keys, needed for the
	 *         script of the physical model
	 */
	private List<Entry> addEntriesFromKeys(List<PartitionKey> partitionKeys, List<SortKey> sortKeys,
			List<Entry> entries) {

		List<Entry> entriesTemp = new ArrayList<Entry>(entries);

		// check on partition keys
		for (PartitionKey partitionKey : partitionKeys) {
			boolean found = false;
			for (Entry entry : entriesTemp) {

				if (partitionKey.getId().equals(entry.getId()))
					found = true;
			}
			if (!found)
				entries.add(new Entry(partitionKey.getId(), partitionKey.getName(), partitionKey.getType(),
						partitionKey.getEntity(), "from_partition_key"));
		}

		// check on sort keys
		for (SortKey sortKey : sortKeys) {
			boolean found = false;
			for (Entry entry : entriesTemp) {

				if (sortKey.getId().equals(entry.getId()))
					found = true;
			}
			if (!found)
				entries.add(new Entry(sortKey.getId(), sortKey.getName(), sortKey.getType(), sortKey.getEntity(),
						"from_sort_key"));
		}

		return entries;
	}

	/**
	 * @param partitionKeys
	 * @return new partition keys list without 'weak' keys. In 1:N relationships,
	 *         the entity with cardinality 1 is the owner/parent of the
	 *         relationship. It's discarded the one with N cardinality (note that in
	 *         web ratio data model document, logic is inverted). In 1:1 not
	 *         relevant: partition keys are not discarded and the entries are flat.
	 *         In N:M cannot be understood which is the owner of the relationship
	 */
	private List<PartitionKey> removeWeakPartitionKeys(List<PartitionKey> partitionKeys) {

		// not enough keys to compare, returns
		if (partitionKeys.size() < 2)
			return partitionKeys;

		List<PartitionKey> tempPartitionKeys = new ArrayList<PartitionKey>(partitionKeys);
		List<PartitionKey> updatedPartitionKeys = new ArrayList<PartitionKey>(partitionKeys);

		for (PartitionKey partitionKey : partitionKeys) {
			for (PartitionKey tempPartitionKey : tempPartitionKeys) {

				// not comparing same attribute, retrieved entity source and target != null ,
				// retrieved relationship != null
				if (!partitionKey.getId().equals(tempPartitionKey.getId())
						&& dataModelUtil.findEntityByName(partitionKey.getEntity()) != null
						&& dataModelUtil.findEntityByName(tempPartitionKey.getEntity()) != null
						&& dataModelUtil.findRelationshipBySourceAndTarget(
								dataModelUtil.findEntityByName(partitionKey.getEntity()).getId(),
								dataModelUtil.findEntityByName(tempPartitionKey.getEntity()).getId()) != null) {

					Relationship relationship = dataModelUtil.findRelationshipBySourceAndTarget(
							dataModelUtil.findEntityByName(partitionKey.getEntity()).getId(),
							dataModelUtil.findEntityByName(tempPartitionKey.getEntity()).getId());

					if ((relationship.getRelationshipRole1().getMaxCard().equals("N") && relationship
							.getRelationshipRole1().getJoinColumn().getAttribute().equals(partitionKey.getId()))
							|| (relationship.getRelationshipRole2().getMaxCard().equals("N")
									&& relationship.getRelationshipRole2().getJoinColumn().getAttribute()
											.equals(partitionKey.getId())))
						// if found , removed the partition key which participates to the relationship
						updatedPartitionKeys.remove(tempPartitionKey);

				}

			}
		}

		return updatedPartitionKeys;
	}

	/**
	 * @param sortKeys
	 * @return new sort keys without duplicate sort keys that differ only for
	 *         ordering. given priority to DESC order
	 */
	private List<SortKey> removeSortKeysWithDifferentOrder(List<SortKey> sortKeys) {

		List<SortKey> tempSortKeys = new ArrayList<SortKey>(sortKeys);
		List<SortKey> tempSortKeys2 = new ArrayList<SortKey>(sortKeys);

		for (SortKey sortKey : tempSortKeys) {

			for (SortKey sortKey2 : tempSortKeys2) {

				if (sortKey.getId().equals(sortKey2.getId()) && !sortKey.getOrdering().equals(sortKey2.getOrdering())) {

					if (sortKey.getOrdering().equals(Ordering.ASCENDING))
						sortKeys.remove(sortKey);
					else
						sortKeys.remove(sortKey2);

				}
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
	 * @param partitionKeys
	 * @param sortKeys
	 * @return new sort keys list without keys that are already partition keys
	 */
	private List<SortKey> removeDuplicatesFromSortKeys(List<PartitionKey> partitionKeys,
			List<SortKey> sortKeys) {

		List<SortKey> tempSortKeys = new ArrayList<SortKey>(sortKeys);

		for (SortKey sortKey : tempSortKeys) {
			for (PartitionKey partitionKey : partitionKeys) {

				if (sortKey.getId().equals(partitionKey.getId()))
					sortKeys.remove(sortKey);
			}
		}

		return sortKeys;
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

					// if (partitionKeysHaveSortAttribute(partitionKeys, sortAttribute)) {
					SortKey sortKey = extractSortKeyFromApplicationModel(sortAttribute);
					sortKeys.add(sortKey);
					// }
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

								// if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
								// attributesCondition)) {
								SortKey sortKey = extractSortKeyFromDataModel(attributesCondition, wrapperAttribute);
								sortKeys.add(sortKey);
								// }
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

					// if (partitionKeysHaveSortAttribute(partitionKeys, sortAttribute)) {
					SortKey sortKey = extractSortKeyFromApplicationModel(sortAttribute);
					sortKeys.add(sortKey);
					// }
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

								// if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
								// attributesCondition)) {
								SortKey sortKey = extractSortKeyFromDataModel(attributesCondition, wrapperAttribute);
								sortKeys.add(sortKey);
								// }
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

							// if (partitionKeysHaveAttributeConditions(partitionKeys, wrapperAttribute,
							// attributesCondition)) {
							SortKey sortKey = extractSortKeyFromDataModel(attributesCondition, wrapperAttribute);
							sortKeys.add(sortKey);
							// }
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
	 * @return partition key computed from targets which express a condition. Are
	 *         generated duplicates Return null if there is no binding parameter
	 */
	private List<PartitionKey> retrievePartitionKey(InteractionFlowElement ife, InteractionFlowElement nextIfe) {

		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();

		for (InteractionFlow inInteractionFlow : ife.getInInteractionFlows()) {

			// looks for the right pointing link to the next interaction flow element; if
			// nextIfe is null, a leaf is reached
			if (nextIfe == null || inInteractionFlow.getTo().equals(ife.getId() )) {

				for (BindingParameter bindingParameter : inInteractionFlow.getBindingParameter()) {

					// TODO remove this condition when handle actions
					if (bindingParameter.getSources() == null || bindingParameter.getTargets() == null)
						return null;

					// get all sources of binding parameters
					// for (int x = 0; x < bindingParameter.getSources().size(); x++) {
					//
					// if (bindingParameter.getSources().get(x) instanceof Attribute) {
					// Attribute sourceAttribute = (Attribute) bindingParameter.getSources().get(x);
					//
					// if (sourceAttribute.getId() != null) {
					// PartitionKey partitionKey = new PartitionKey(sourceAttribute.getId());
					// partitionKey.setName(sourceAttribute.getName());
					// partitionKey.setType(sourceAttribute.getType());
					// partitionKey.setEntity(sourceAttribute.getEntity().getName());
					//
					// partitionKeys.add(partitionKey);
					//
					// }
					// }
					// }

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
	public List<Entry> createEntries(List<InteractionFlowElement> interactionFlowElements) {

		List<Entry> entries = new ArrayList<Entry>();

		for (InteractionFlowElement interactionFlowElement : interactionFlowElements) {

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
	 * Optimization on path scope
	 */
	@Override
	public List<Collection> pathOptimization(List<Collection> collections) {

		// entries aggregation
		List<Collection> optimizedCollections = optimizeReadingAccessPaths(collections);

		// collection aggregation
		optimizedCollections = optimizeCollections(collections);

		return removeDuplicatesCollections(optimizedCollections);
	}

	@Override
	// TODO da finire ottimizzazione sul percorso
	public List<Collection> optimizeCollections(List<Collection> collections) {

		ArrayList<Collection> collectionsTemp = new ArrayList<Collection>(collections);
		ArrayList<Collection> collectionsTemp2 = new ArrayList<Collection>(collections);
		ArrayList<Collection> optimizedCollections = new ArrayList<Collection>();

		for (Collection collectionTemp : collectionsTemp) {

			String name = "("+collectionTemp.getName()+")";
			Integer countCollection = 1;
			ArrayList<PartitionKey> partitionKeys = new ArrayList<PartitionKey>(collectionTemp.getBlock().getKey().getPartitionKeys());
			ArrayList<SortKey> sortKeys = new ArrayList<SortKey>(collectionTemp.getBlock().getKey().getSortKeys());
			ArrayList<Entry> entries = new ArrayList<Entry>(collectionTemp.getBlock().getEntries());

			
			for (Collection collectionTemp2 : collectionsTemp2) {
				
				// comparing different collections
				if (!collectionTemp.getName().equals(collectionTemp2.getName()) && !collectionTemp.getId().equals(collectionTemp2.getId())) {

					if (haveSamePartitionKeys(collectionTemp2.getBlock().getKey().getPartitionKeys(),
							partitionKeys.stream().distinct().collect(Collectors.toList()))) {

						partitionKeys.addAll(collectionTemp2.getBlock().getKey().getPartitionKeys());

						sortKeys.addAll(collectionTemp2.getBlock().getKey().getSortKeys());
						sortKeys.addAll(collectionTemp.getBlock().getKey().getSortKeys());

						entries.addAll(collectionTemp2.getBlock().getEntries());
						entries.addAll(collectionTemp.getBlock().getEntries());

						name = name + "-" + "(" + collectionTemp2.getName() +")";
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
				
				List<SortKey> sortKeysToAdd = removeSortKeysWithDifferentOrder(primaryKey.getSortKeys());
				sortKeysToAdd = removeDuplicatesFromSortKeys(primaryKey.getPartitionKeys(),sortKeysToAdd);
				
				primaryKey.setSortKeys(sortKeysToAdd);

				collection.getBlock().setKey(primaryKey);

				collection.getBlock().setEntries(collectionTemp.getBlock().getEntries());
				collection.getBlock().getEntries().addAll(entries);

				collection.getBlock().setEntries(removeDuplicatesEntries(collection.getBlock().getEntries()));

				optimizedCollections.add(collection);

			}

		}

		return optimizedCollections;
	}

	private List<Entry> removeDuplicatesEntries(List<Entry> entries) {

		entries = entries.stream().distinct().collect(Collectors.toList());

		ArrayList<Entry> entriesTemp = new ArrayList<Entry>(entries);
		ArrayList<Entry> entriesTemp2 = new ArrayList<Entry>(entries);

		for (Entry entryTemp : entriesTemp) {

			for (Entry entryTemp2 : entriesTemp2) {

				// > 4, because for example ent1#att26 contains ent1#att2 !!
				if (entryTemp.getId().length() > entryTemp2.getId().length()
						&& entryTemp.getId().contains(entryTemp2.getId())
						&& (entryTemp.getId().length() - entryTemp2.getId().length() > 4))

					entries.remove(entryTemp2);
				else if (entryTemp2.getId().length() > entryTemp.getId().length()
						&& entryTemp2.getId().contains(entryTemp.getId())
						&& (entryTemp2.getId().length() - entryTemp.getId().length() > 4))
					entries.remove(entryTemp);

			}
		}
		return entries;
	}

	/**
	 * @param collections
	 * @return list of collection without duplicates (comparison based on the keys
	 *         and entries)
	 */
	private List<Collection> removeDuplicatesCollections(List<Collection> collections) {

		ArrayList<Collection> collectionsTemp = new ArrayList<Collection>(collections);
		ArrayList<Collection> collectionsTemp2 = new ArrayList<Collection>(collections);
		ArrayList<Collection> newCollection = new ArrayList<Collection>();

		for (Collection collectionTemp : collectionsTemp) {

			for (Collection collectionTemp2 : collectionsTemp2) {
				// comparing different collections
				if (	!collectionTemp.getName().equals(collectionTemp2.getName()) && !collectionTemp.getId().equals(collectionTemp2.getId())
						&& haveSamePartitionKeys(collectionTemp.getBlock().getKey().getPartitionKeys(),
								collectionTemp2.getBlock().getKey().getPartitionKeys())
						&& haveSameSortKeys(collectionTemp.getBlock().getKey().getSortKeys(),
								collectionTemp2.getBlock().getKey().getSortKeys())
						&& haveSameEntries(collectionTemp.getBlock().getEntries(),
								collectionTemp2.getBlock().getEntries())
						&& !collectionHasAlreadyBeenAdded(collectionTemp2, newCollection)) {

					newCollection.add(collectionTemp2);

				}

			}
		}
		collections.removeAll(newCollection);
		return collections;
	}

	/**
	 * @param aggregateEntry
	 * @param entries
	 * @return true if collection has been already inserted in the new collection
	 *         list,
	 */
	private boolean collectionHasAlreadyBeenAdded(Collection collection, List<Collection> alreadyAddedCollections) {

		Boolean found = false;

		for (Collection alreadyAddedCollection : alreadyAddedCollections) {

			if (haveSamePartitionKeys(collection.getBlock().getKey().getPartitionKeys(),
					alreadyAddedCollection.getBlock().getKey().getPartitionKeys())
					&& haveSameSortKeys(collection.getBlock().getKey().getSortKeys(),
							alreadyAddedCollection.getBlock().getKey().getSortKeys())
					&& haveSameEntries(collection.getBlock().getEntries(),
							alreadyAddedCollection.getBlock().getEntries()))
				found = true;
		}

		return found;

	}

	private boolean haveSameEntries(List<Entry> entries, List<Entry> entries2) {
		boolean same = false;
		for (Entry entry : entries) {
			same = false;

			for (Entry entry2 : entries2) {

				if (entry.equals(entry2))
					same = true;
			}

			if (!same)
				return same;
		}

		return same;
	}

	private boolean haveSameSortKeys(List<SortKey> sortKeys, List<SortKey> sortKeys2) {
		boolean same = false;
		
		if (sortKeys.size() == 0 || sortKeys2.size() ==0)
			return true;
		
		if (sortKeys.size() != sortKeys2.size())
			return same;
		
		for (SortKey sortKey : sortKeys) {
			same = false;

			for (SortKey sortKey2 : sortKeys2) {

				if (sortKey.equals(sortKey2))
					same = true;
			}

			if (!same)
				return same;
		}

		return same;
	}

	private boolean haveSamePartitionKeys(List<PartitionKey> partitionKeys, List<PartitionKey> partitionKeys2) {
		boolean same = false;
		
		if (partitionKeys.size() == 0 || partitionKeys2.size() ==0)
			return true;
		
		if (partitionKeys.size() != partitionKeys2.size())
			return same;
		
		for (PartitionKey partitionKey : partitionKeys) {
			same = false;

			for (PartitionKey partitionKey2 : partitionKeys2) {

				if (partitionKey.equals(partitionKey2))
					same = true;
			}

			if (!same)
				return same;
		}

		return same;
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
	public List<Collection> optimizeReadingAccessPaths(List<Collection> collections) {

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
										&& !aggregateHasBeenAlreadyAdded(entry, candidateAggregateEntries)
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
						&& checkPresence(candidateAggregateEntries, collection.getPath())) {
					Entry aggregateEntry = new Entry(candidateAggregateEntries);
					List<Entry> entryTemp = new ArrayList<Entry>(collection.getBlock().getEntries());

					for (Entry entry : entryTemp) {
						for (Entry entry2 : candidateAggregateEntries) {
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
	 * @param candidateAggregateEntries
	 * @param path
	 * @return true if the aggregate entries are present in all view components
	 *         entity based
	 */
	private boolean checkPresence(List<Entry> candidateAggregateEntries, Path path) {
		boolean aggregable = true;

		for (Entry candidateEntry : candidateAggregateEntries) {
			String entityName = candidateEntry.getEntityName();
			if (entityName == null)
				continue;

			for (InteractionFlowElement ife : path.getInteractionFlowElements()) {
				boolean found = false;

				if (ife instanceof ListImpl && ((ListImpl) ife).getEntity() != null) {
					ListImpl listImpl = (ListImpl) ife;

					if (!listImpl.getEntity().getName().equals(entityName))
						continue;

					for (Attribute attribute : listImpl.getDisplayAttributes()) {
						if (attribute.getId().equals(candidateEntry.getId()))
							found = true;
					}

				}

				if (ife instanceof DetailImpl && ((DetailImpl) ife).getEntity() != null) {
					DetailImpl detailImpl = (DetailImpl) ife;

					if (!detailImpl.getEntity().getName().equals(entityName))
						continue;

					for (Attribute attribute : detailImpl.getDisplayAttributes()) {
						if (attribute.getId().equals(candidateEntry.getId()))
							found = true;
					}
				}

				if (ife instanceof SelectorImpl && ((SelectorImpl) ife).getEntity() != null) {
					SelectorImpl selectorImpl = (SelectorImpl) ife;

					if (!selectorImpl.getEntity().getName().equals(entityName))
						continue;

					for (String attributeId : selectorImpl.getAttributes().keySet()) {
						if (attributeId.equals(candidateEntry.getId()))
							found = true;
					}
				}

				if (ife instanceof FormImpl && ((FormImpl) ife).getEntity() != null) {
					FormImpl formImpl = (FormImpl) ife;

					if (!formImpl.getEntity().getName().equals(entityName))
						continue;

					for (Field field : formImpl.getFields()) {

						if (field instanceof FieldImpl && ((FieldImpl) field).getAttribute() != null) {
							FieldImpl fieldImpl = (FieldImpl) field;
							if (fieldImpl.getAttribute().getId().equals(candidateEntry.getId()))
								found = true;
						}

						if (field instanceof SelectionFieldImpl
								&& ((SelectionFieldImpl) field).getAttribute() != null) {
							SelectionFieldImpl selectionFieldImpl = (SelectionFieldImpl) field;
							if (selectionFieldImpl.getAttribute().getId().equals(candidateEntry.getId()))
								found = true;
						}

						if (field instanceof MultipleSelectionFieldImpl
								&& ((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition() != null) {
							MultipleSelectionFieldImpl multipleSelectionFieldImpl = (MultipleSelectionFieldImpl) field;
							if (multipleSelectionFieldImpl.getRelationshipRoleCondition().getRelationshipRole1() != null
									&& multipleSelectionFieldImpl.getRelationshipRoleCondition().getRelationshipRole1()
											.getJoinColumn() != null
									&& multipleSelectionFieldImpl.getRelationshipRoleCondition().getRelationshipRole1()
											.getJoinColumn().getAttribute().equals(candidateEntry.getId()))
								found = true;
							else if (multipleSelectionFieldImpl.getRelationshipRoleCondition()
									.getRelationshipRole2() != null
									&& multipleSelectionFieldImpl.getRelationshipRoleCondition().getRelationshipRole2()
											.getJoinColumn() != null
									&& multipleSelectionFieldImpl.getRelationshipRoleCondition().getRelationshipRole2()
											.getJoinColumn().getAttribute().equals(candidateEntry.getId()))
								found = true;
						}
					}
				}

				if (!found)
					return false;

			}

		}
		return aggregable;
	}

	/**
	 * @param aggregateEntry
	 * @param entries
	 * @return true if aggregate has been already inserted in the entry list of the
	 *         block, even if single attributes are in grouped in different order
	 */
	private boolean aggregateHasBeenAlreadyAdded(Entry aggregateEntry, List<Entry> entries) {

		Boolean found = false;

		for (Entry entry : entries) {
			if (entry.getId().equals(aggregateEntry.getId()))
				found = true;

			// may already exist an aggregated entry with name of the single entry in a
			// different order
			if (!entry.equals(aggregateEntry) && entry.getId().length() == aggregateEntry.getId().length()
					&& entry.getId().contains(" ")) {

				int count = 0;
				String[] idsAggregatedAttributes = aggregateEntry.getId().split(" ");
				String[] idsEntry = entry.getId().split(" ");

				if (idsEntry != null && idsAggregatedAttributes != null
						&& idsAggregatedAttributes.length == idsEntry.length) {

					for (String idAggregatedAttribute : idsAggregatedAttributes) {
						for (String idEntry : idsEntry) {
							if (idAggregatedAttribute.equals(idEntry))
								count++;

						}
					}
					if (count == idsEntry.length)
						found = true;
				}
			}
		}
		return found;

	}

	public DataModelUtil getDataModelUtil() {
		return dataModelUtil;
	}

	public void setDataModelUtil(DataModelUtil dataModelUtil) {
		this.dataModelUtil = dataModelUtil;
	}

	
	
	/* Optimization over page scope
	 */
	@Override
	public List<Collection> pageOptimization(List<Collection> collectionsPageScope) {
		// collection aggregation
		return removeDuplicatesCollections(optimizeCollections(removeDuplicatesCollections(collectionsPageScope)));
	}

}
