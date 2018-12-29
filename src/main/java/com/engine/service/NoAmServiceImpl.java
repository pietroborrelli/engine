package com.engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.PrimaryKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
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

		System.out.println("---------CREAZIONE DEL NO AM---------");

		List<Entry> entries = new ArrayList<Entry>();
		Block block = new Block();

		for (Path path : paths) {

			entries = createEntries(path);
			System.out.println(entries.size() + " entries");

			block = createBlock(path, entries);
			System.out.println(block.getKey().getPartitionKeys().size() + " partition keys");
			System.out.println(block.getKey().getSortKeys().size() + " sort keys");
		}

		return null;
	}

	@Override
	public Collection createCollection() {
		// TODO Auto-generated method stub
		return null;
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
			sortKeys.addAll(retrieveSortKey(path.getInteractionFlowElements().get(s),partitionKeys));
		
		// removed duplicates on sort list
		sortKeys = sortKeys.stream().distinct().collect(Collectors.toList());
		


		// TODO update partition key list by removing duplicates from sort keys

		primaryKey.setPartitionKeys(partitionKeys);
		primaryKey.setSortKeys(sortKeys);

		block.setKey(primaryKey);
		block.setEntries(entries);

		return block;
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
		// get sort attributes if already in the group of the partition keys 
		if (interactionFlowElement instanceof ListImpl) {

			for (Attribute sortAttribute : ((ListImpl) interactionFlowElement).getSortAttributes()) {
				//if is specified an ordering over the partition keys
				if (partitionKeysHaveSortAttribute(partitionKeys, sortAttribute)) {
				
					SortKey sortKey = new SortKey(sortAttribute.getId());
					sortKey.setName(sortAttribute.getName());
					sortKey.setType(sortAttribute.getType());
					sortKey.setEntity(sortAttribute.getEntity().getName());
					sortKey.setOrdering(sortAttribute.getOrdering());
					
					sortKeys.add(sortKey);
				}
			}
			
		// get condition attributes
		
		}

		// In case is SELECTOR get sort attributes and condition attributes
		if (interactionFlowElement instanceof SelectorImpl) {
			((SelectorImpl) interactionFlowElement).getSortAttributes();
		}

		return sortKeys;
	}

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

			if (selectorImpl.getEntity() != null)
				entry.setEntityName(selectorImpl.getEntity().getName());

			entries.add(entry);

		}
		return entries;
	}
}
