package com.engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.enumeration.Predicate;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.WrapperAttribute;
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

@Service
public class EntryServiceImpl implements EntryService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.engine.service.EntryService#retrieveFormAttributes(com.engine.domain.
	 * interactionflowelement.viewelement.viewcomponent.FormImpl)
	 */
	@Override
	public List<Entry> retrieveFormAttributes(FormImpl formImpl) {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.engine.service.EntryService#retrieveListDisplayAttributes(com.engine.
	 * domain.interactionflowelement.viewelement.viewcomponent.ListImpl)
	 */
	@Override
	public List<Entry> retrieveListDisplayAttributes(ListImpl listImpl) {

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

			entry.setPredicate(findPredicate(listImpl, entry.getId()));
			entry.setValueCondition(findValueCondition(listImpl, entry.getId()));

			entries.add(entry);

		}
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.engine.service.EntryService#retrieveDetailDisplayAttributes(com.engine.
	 * domain.interactionflowelement.viewelement.viewcomponent.DetailImpl)
	 */
	@Override
	public List<Entry> retrieveDetailDisplayAttributes(DetailImpl detailImpl) {

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

			entry.setPredicate(findPredicate(detailImpl, entry.getId()));
			entry.setValueCondition(findValueCondition(detailImpl, entry.getId()));

			entries.add(entry);

		}
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.engine.service.EntryService#retrieveSelectorAttributes(com.engine.domain.
	 * interactionflowelement.viewelement.viewcomponent.SelectorImpl)
	 */
	@Override
	public List<Entry> retrieveSelectorAttributes(SelectorImpl selectorImpl) {

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

			entry.setPredicate(findPredicate(selectorImpl, entry.getId()));
			entry.setValueCondition(findValueCondition(selectorImpl, entry.getId()));

			entries.add(entry);

		}
		return entries;
	}

	public List<Entry> removeDuplicatesEntries(List<Entry> entries) {

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

	public boolean haveSameEntries(List<Entry> entries, List<Entry> entries2) {
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

	/**
	 * @param aggregateEntry
	 * @param entries
	 * @return true if aggregate has been already inserted in the entry list of the
	 *         block, even if single attributes are in grouped in different order
	 */
	public boolean aggregateHasBeenAlreadyAdded(Entry aggregateEntry, List<Entry> entries) {

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

	/**
	 * @param candidateAggregateEntries
	 * @param path
	 * @return true if the aggregate entries are present in all view components
	 *         entity based
	 */
	public boolean checkPresence(List<Entry> candidateAggregateEntries, Path path) {
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
	 * @param interactionFlowElement
	 * @return predicate otherwise null if no predicate is set
	 */
	@Override
	public Predicate findPredicate(InteractionFlowElement interactionFlowElement, String idAttribute) {

		// 1. In case is LIST
		if (interactionFlowElement instanceof ListImpl) {
			for (ConditionalExpression conditionalExpression : ((ListImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchPredicates(condition);

						}
					}
				}
			}
		}

		// 2. In case is DETAIL
		if (interactionFlowElement instanceof DetailImpl) {
			for (ConditionalExpression conditionalExpression : ((DetailImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchPredicates(condition);

						}
					}
				}
			}
		}

		// 3. In case is SELECTOR
		if (interactionFlowElement instanceof SelectorImpl) {
			for (ConditionalExpression conditionalExpression : ((SelectorImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchPredicates(condition);

						}
					}
				}
			}
		}
		return null;
	}

	private Predicate switchPredicates(Condition condition) {
		if (condition.getPredicate().equals("beginWith")) {
			return Predicate.BEGIN_WITH;
		}
		if (condition.getPredicate().equals("contains"))
			return Predicate.CONTAINS;

		if (condition.getPredicate().equals("endsWith"))
			return Predicate.END_WITH;

		if (condition.getPredicate().equals("empty"))
			return Predicate.IS_EMPTY;

		if (condition.getPredicate().equals("notEmpty"))
			return Predicate.IS_NOT_EMPTY;

		if (condition.getPredicate().equals("notNull"))
			return Predicate.IS_NOT_NULL;

		if (condition.getPredicate().equals("null"))
			return Predicate.NULL;

		if (condition.getPredicate().equals("notBeginWith"))
			return Predicate.NOT_BEGIN_WITH;

		if (condition.getPredicate().equals("notContains"))
			return Predicate.NOT_CONTAINS;

		if (condition.getPredicate().equals("notEndsWith"))
			return Predicate.NOT_END_WITH;

		if (condition.getPredicate().equals("notEqual"))
			return Predicate.NOT_EQUAL;

		return null;
	}

	private boolean hasAttributeACondition(List<WrapperAttribute> attributes, String idAttribute) {

		for (WrapperAttribute wrapperAttribute : attributes) {
			if (wrapperAttribute.getId().equals(idAttribute))
				return true;
		}

		return false;
	}

	@Override
	public String findValueCondition(InteractionFlowElement interactionFlowElement, String idAttribute) {

		// 1. In case is LIST
		if (interactionFlowElement instanceof ListImpl) {
			for (ConditionalExpression conditionalExpression : ((ListImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchValueCondition(condition);
						}
					}
				}
			}
		}

		// 2. In case is DETAIL
		if (interactionFlowElement instanceof DetailImpl) {
			for (ConditionalExpression conditionalExpression : ((DetailImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchValueCondition(condition);
						}
					}
				}
			}
		}

		// 3. In case is SELECTOR
		if (interactionFlowElement instanceof SelectorImpl) {
			for (ConditionalExpression conditionalExpression : ((SelectorImpl) interactionFlowElement)
					.getConditionalExpressions()) {

				if (conditionalExpression.getBooleanOperator().equals("and")) {

					for (Condition condition : conditionalExpression.getConditions()) {

						// attributes condition
						if (condition instanceof AttributesCondition && hasAttributeACondition(
								((AttributesCondition) condition).getAttributes(), idAttribute)) {

							return switchValueCondition(condition);
						}
					}
				}
			}
		}
		return null;
	}

	private String switchValueCondition(Condition condition) {
		if (condition.getPredicate().equals("beginWith"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("contains"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("endsWith"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("empty"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("notEmpty"))
			return "";

		if (condition.getPredicate().equals("notNull"))
			return "";

		if (condition.getPredicate().equals("null"))
			return "";

		if (condition.getPredicate().equals("notBeginWith"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("notContains"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("notEndsWith"))
			return ((AttributesCondition) condition).getValue();

		if (condition.getPredicate().equals("notEqual"))
			return ((AttributesCondition) condition).getValue();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.engine.service.BlockService#addEntriesFromKeys(java.util.List,
	 * java.util.List, java.util.List)
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
}
