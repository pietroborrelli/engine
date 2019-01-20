package com.engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
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
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.SelectorImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

@Service
public class BlockServiceImpl implements BlockService {


	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#guaranteeRecordUniqueness(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public List<SortKey> guaranteeRecordUniqueness(List<SortKey> sortKeys, List<PartitionKey> partitionKeys,
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#addEntriesFromKeys(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public List<Entry> addEntriesFromKeys(List<PartitionKey> partitionKeys, List<SortKey> sortKeys,
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#removeSortKeysWithDifferentOrder(java.util.List)
	 */
	@Override
	public List<SortKey> removeSortKeysWithDifferentOrder(List<SortKey> sortKeys) {
	
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#removeDuplicatesFromPartitionKeys(java.util.List, java.util.List)
	 */
	@Override
	public List<PartitionKey> removeDuplicatesFromPartitionKeys(List<PartitionKey> partitionKeys,
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#retrieveSortKey(com.engine.domain.interactionflowelement.InteractionFlowElement, java.util.List)
	 */
	@Override
	public List<SortKey> retrieveSortKey(InteractionFlowElement interactionFlowElement,
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#retrievePartitionKey(com.engine.domain.interactionflowelement.InteractionFlowElement, com.engine.domain.interactionflowelement.InteractionFlowElement)
	 */
	@Override
	public List<PartitionKey> retrievePartitionKey(InteractionFlowElement ife, InteractionFlowElement nextIfe) {
	
		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();
	
		for (InteractionFlow inInteractionFlow : ife.getInInteractionFlows()) {
	
			// looks for the right pointing link to the next interaction flow element; if
			// nextIfe is null, a leaf is reached
			if (nextIfe == null || inInteractionFlow.getTo().equals(ife.getId() )) {
	
				for (BindingParameter bindingParameter : inInteractionFlow.getBindingParameter()) {
	
					// TODO remove this condition when handle actions
					if (bindingParameter.getSources() == null || bindingParameter.getTargets() == null)
						return null;
	
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

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#extractSortKeyFromApplicationModel(com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute)
	 */
	@Override
	public SortKey extractSortKeyFromApplicationModel(Attribute attribute) {
		SortKey sortKey = new SortKey(attribute.getId());
		sortKey.setName(attribute.getName());
		sortKey.setType(attribute.getType());
		sortKey.setEntity(attribute.getEntity().getName());
		sortKey.setOrdering(attribute.getOrdering());
		return sortKey;
	}

	/* (non-Javadoc)
	 * @see com.engine.service.BlockService#extractSortKeyFromDataModel(com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition, com.engine.domain.interactionflowelement.conditionalexpression.condition.WrapperAttribute)
	 */
	@Override
	public SortKey extractSortKeyFromDataModel(AttributesCondition attributesCondition,
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
	 * Sort keys with order = null comes from attributes conditions with "equal"
	 * predicate
	 * 
	 * @param sortKeys
	 * @return keys without null ordering
	 */
	public List<SortKey> removeSortKeysWithOrderIsNull(List<SortKey> sortKeys) {
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
	 * @param wrapperAttribute
	 * @param attributesCondition
	 * @return true if the attribute of the attributes condition is already present
	 *         in the partition keys group and for which is specified an ordering in
	 *         the predicate, otherwise false
	 */
	public boolean partitionKeysHaveAttributeConditions(List<PartitionKey> partitionKeys,
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
	public Boolean partitionKeysHaveSortAttribute(List<PartitionKey> partitionKeys, Attribute sortAttribute) {
		Boolean found = false;
		for (PartitionKey partitionKey : partitionKeys) {
			if (partitionKey.getId().equals(sortAttribute.getId()))
				found = true;
		}
		return found;
	}
	
	/**
	 * @param partitionKeys
	 * @param sortKeys
	 * @return new sort keys list without keys that are already partition keys
	 */
	public List<SortKey> removeDuplicatesFromSortKeys(List<PartitionKey> partitionKeys,
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
	


	

	public boolean haveSameSortKeys(List<SortKey> sortKeys, List<SortKey> sortKeys2) {
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

	public boolean haveSamePartitionKeys(List<PartitionKey> partitionKeys, List<PartitionKey> partitionKeys2) {
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

	
}