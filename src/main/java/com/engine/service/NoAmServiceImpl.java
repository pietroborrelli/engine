package com.engine.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
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

		List<Entry> entries = new ArrayList<Entry>();

		for (Path path : paths) {

			entries = createEntries(path);
			System.out.println(entries.size() + " entries");

			//block = createBlock(path);
		}

		return null;
	}

	@Override
	public Collection createCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block createBlock(Path Path) {
		// TODO Auto-generated method stub
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
