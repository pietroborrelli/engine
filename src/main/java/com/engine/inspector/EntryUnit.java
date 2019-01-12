package com.engine.inspector;

import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.FormImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.Field;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.FieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.MultipleSelectionFieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.SelectionFieldImpl;
import com.engine.mapper.datamodel.DataModel;

public final class EntryUnit implements ViewComponentExtractor {

	static String FORM_FIELD = "Field";
	static String FORM_SELECTION_FIELD = "SelectionField";
	static String FORM_MULTI_SELECTION_FIELD = "MultiSelectionField";

	static String FORM_FIELD_SHORT = "fld";
	static String FORM_SELECTION_FIELD_SHORT = "sfld";
	static String FORM_MULTI_SELECTION_FIELD_SHORT = "msfld";
	private DataModelUtil dataModelUtil;

	public EntryUnit(DataModel dataModel) {
		this.dataModelUtil = new DataModelUtil(dataModel);
	}

	@Override
	public ViewComponent extractSpecificViewComponent(String viewComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewComponent extractNextViewComponent(String viewComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @param node: raw node from the document
	 * 
	 * @return form-entity based extracted from the node and data model
	 */
	@Override
	public ViewComponent mapViewComponent(Node node) {
		FormImpl form = new FormImpl();
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "entity": {
				form.setEntity(dataModelUtil.findEntity(attribute.getNodeValue()));
				if (form.getEntity() == null)
					System.out.println("--> scartato - Attenzione entity null\n");
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				form.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				form.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		form.setFields(findFields(node));

		return form;
	}

	/**
	 * @param node
	 * @param form
	 * @return fields,selection fields and multi selection fields bound to the form
	 */
	public ArrayList<Field> findFields(Node node) {

		ArrayList<Field> fields = new ArrayList<Field>();

		// get child of the node and iterate over its siblings
		Node sibling = node.getFirstChild().getNextSibling();

		while (sibling != null) {

			// ci sono nodi vuoti a volte quindi proseguo con i nodi fratelli
			if (!sibling.hasAttributes()) {
				sibling = sibling.getNextSibling();
				continue;
			}

			String id = "";
			String name = "";
			String type = "";
			String attributeId = "";
			String roleId = "";

			for (int attributeCount = 0; attributeCount < sibling.getAttributes().getLength(); attributeCount++) {
				Attr attribute = (Attr) sibling.getAttributes().item(attributeCount);

				switch (attribute.getName()) {

				case "id": {
					id = attribute.getValue();
					break;
				}

				case "name": {
					name = attribute.getValue();
					break;
				}

				case "type": {
					type = attribute.getValue();
					break;
				}

				case "attribute": {
					attributeId = attribute.getValue();
					break;
				}

				case "role": {
					roleId = attribute.getValue();
					break;
				}

				}

			}
			// custom actions for each fields

			if (sibling.getNodeName().equals(FORM_FIELD)) {
				System.out.println("Trovato FIELD ");

				FieldImpl field = new FieldImpl(id, name, type);

				Attribute entityAttribute = new Attribute();
				// set attribute id if has reference to attribute of entity
				if (!attributeId.isEmpty()) {
					entityAttribute.setId(attributeId);
					// set name of attribute from entity
					entityAttribute.setName(dataModelUtil.findAttributeName(
							dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
							entityAttribute.getId()));

					// set type of attribute from entity
					entityAttribute.setType(dataModelUtil.findAttributeType(
							dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
							entityAttribute.getId()));

					// set entity on attribute
					entityAttribute.setEntity(
							dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))));
				}
				field.setAttribute(entityAttribute);

				fields.add(field);

			}

			if (sibling.getNodeName().equals(FORM_SELECTION_FIELD)) {
				System.out.println("Trovato SELECTION FIELD ");
				SelectionFieldImpl selectionField = new SelectionFieldImpl(id, name, type);
				if (roleId != "") {
					// get relationship, predicate and name left empty
					RelationshipRoleCondition relationshipRoleCondition = new RelationshipRoleCondition();
					relationshipRoleCondition.setId(roleId);
					relationshipRoleCondition
							.setRelationship(dataModelUtil.findRelationship(roleId.substring(0, roleId.indexOf("#"))));

					selectionField.setRelationshipRoleCondition(relationshipRoleCondition);

					fields.add(selectionField);

				}

			}

			if (sibling.getNodeName().equals(FORM_MULTI_SELECTION_FIELD)) {
				System.out.println("Trovato MULTI SELECTION FIELD ");

				MultipleSelectionFieldImpl multipleSelectionField = new MultipleSelectionFieldImpl(id, name, type);
				if (roleId != "") {
					// get relationship, predicate and name left empty
					RelationshipRoleCondition relationshipRoleCondition = new RelationshipRoleCondition();
					relationshipRoleCondition.setId(roleId);
					relationshipRoleCondition
							.setRelationship(dataModelUtil.findRelationship(roleId.substring(0, roleId.indexOf("#"))));

					multipleSelectionField.setRelationshipRoleCondition(relationshipRoleCondition);

					fields.add(multipleSelectionField);

				}
			}
			sibling = sibling.getNextSibling();
		}

		return fields;
	}

	/**
	 * @param fieldId
	 * @return node name of the field
	 */
	public String getFieldType(String fieldId) {

		String fieldIdShort = fieldId.substring(fieldId.lastIndexOf("#") + 1);

		if (fieldIdShort.contains("_label"))
			fieldIdShort = fieldIdShort.substring(0, fieldIdShort.lastIndexOf("_label"));

		if (fieldIdShort.contains("_output"))
			fieldIdShort = fieldIdShort.substring(0, fieldIdShort.lastIndexOf("_output"));
		
		if (fieldIdShort.contains("_presel"))
			fieldIdShort = fieldIdShort.substring(0, fieldIdShort.lastIndexOf("_presel"));
		
		if (fieldIdShort.startsWith(FORM_FIELD_SHORT)) {

			return "Field";

		}

		if (fieldIdShort.startsWith(FORM_SELECTION_FIELD_SHORT)) {

			return "SelectionField";

		}

		if (fieldIdShort.startsWith(FORM_MULTI_SELECTION_FIELD_SHORT)) {

			return "MultiSelectionField";
		}

		return null;
	}

	public Field mapField(Node node) {

		String id = "";
		String name = "";
		String type = "";
		String attributeId = "";
		String roleId = "";

		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			switch (attribute.getName()) {

			case "id": {
				id = attribute.getValue();
				break;
			}

			case "name": {
				name = attribute.getValue();
				break;
			}

			case "type": {
				type = attribute.getValue();
				break;
			}

			case "attribute": {
				attributeId = attribute.getValue();
				break;
			}

			case "role": {
				roleId = attribute.getValue();
				break;
			}

			}

		}
		// custom actions for each fields

		if (node.getNodeName().equals(FORM_FIELD)) {
			System.out.println("Trovato FIELD ");

			FieldImpl field = new FieldImpl(id, name, type);

			Attribute entityAttribute = new Attribute();
			// set attribute id if has reference to attribute of entity
			if (!attributeId.isEmpty()) {
				entityAttribute.setId(attributeId);
				// set name of attribute from entity
				entityAttribute.setName(dataModelUtil.findAttributeName(
						dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
						entityAttribute.getId()));

				// set type of attribute from entity
				entityAttribute.setType(dataModelUtil.findAttributeType(
						dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
						entityAttribute.getId()));

				// set entity on attribute
				entityAttribute
						.setEntity(dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))));
			}
			field.setAttribute(entityAttribute);

			return field;

		}

		if (node.getNodeName().equals(FORM_SELECTION_FIELD)) {
			System.out.println("Trovato SELECTION FIELD ");
			SelectionFieldImpl selectionField = new SelectionFieldImpl(id, name, type);
			if (roleId != "") {
				// get relationship, predicate and name left empty
				RelationshipRoleCondition relationshipRoleCondition = new RelationshipRoleCondition();
				relationshipRoleCondition.setId(roleId);
				relationshipRoleCondition
						.setRelationship(dataModelUtil.findRelationship(roleId.substring(0, roleId.indexOf("#"))));

				selectionField.setRelationshipRoleCondition(relationshipRoleCondition);

				

			}
			if (!attributeId.isEmpty()) {
				Attribute entityAttribute = new Attribute();
				entityAttribute.setId(attributeId);
				// set name of attribute from entity
				entityAttribute.setName(dataModelUtil.findAttributeName(
						dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
						entityAttribute.getId()));

				// set type of attribute from entity
				entityAttribute.setType(dataModelUtil.findAttributeType(
						dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))),
						entityAttribute.getId()));

				// set entity on attribute
				entityAttribute
						.setEntity(dataModelUtil.findEntity(attributeId.substring(0, attributeId.lastIndexOf("#"))));
				
				selectionField.setAttribute(entityAttribute);
			}
			return selectionField;
		}

		if (node.getNodeName().equals(FORM_MULTI_SELECTION_FIELD)) {
			System.out.println("Trovato MULTI SELECTION FIELD ");

			MultipleSelectionFieldImpl multipleSelectionField = new MultipleSelectionFieldImpl(id, name, type);
			if (roleId != "") {
				// get relationship, predicate and name left empty
				RelationshipRoleCondition relationshipRoleCondition = new RelationshipRoleCondition();
				relationshipRoleCondition.setId(roleId);
				relationshipRoleCondition
						.setRelationship(dataModelUtil.findRelationship(roleId.substring(0, roleId.indexOf("#"))));

				multipleSelectionField.setRelationshipRoleCondition(relationshipRoleCondition);

				return multipleSelectionField;

			}
		}

		return null;
	}

}
