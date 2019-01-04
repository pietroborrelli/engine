package com.engine.inspector;

import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.enumeration.Ordering;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.mapper.datamodel.DataModel;

public final class PowerIndexUnit implements ViewComponentExtractor {

	static String VIEWCOMPONENT_SORTABLE_ATTRIBUTE = "SortAttribute";
	
	private DataModelUtil dataModelUtil;
	private ConditionalExpressionExtractor conditionalExpressionExtractor;
	
	public PowerIndexUnit(DataModel dataModel) {
		this.dataModelUtil = new DataModelUtil(dataModel);
		this.conditionalExpressionExtractor = new ConditionalExpressionExtractor(dataModel);
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
	 * @return view component extracted from the node and data model
	 */
	@Override
	public ViewComponent mapViewComponent(Node node)  {
		ListImpl list = new ListImpl();
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "displayAttributes": {
				ArrayList<Attribute> displayAttributes = new ArrayList<Attribute>();
				for (String idDisplayAttribute : Arrays.asList(attribute.getValue().split(" "))) {
					if (idDisplayAttribute != null)
						displayAttributes.add(new Attribute(idDisplayAttribute));
					else {
						System.out.println("--> scartato - Attenzione displayAttribute null\n");
						break;
					}
				}
				list.setDisplayAttributes(displayAttributes);
				System.out.print("--> estratto\n");
				break;
			}

			case "entity": {
				list.setEntity(dataModelUtil.findEntity(attribute.getNodeValue()));
				if (list.getEntity() == null)
					System.out.println("--> scartato - Attenzione entity null\n");
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				list.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				list.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		if (list.getDisplayAttributes() != null) {
			// set names of attributes from entity
			list.getDisplayAttributes().stream()
					.forEach(d -> d.setName(dataModelUtil.findAttributeName(list.getEntity(), d.getId())));

			// set types of attributes from entity
			list.getDisplayAttributes().stream()
					.forEach(d -> d.setType(dataModelUtil.findAttributeType(list.getEntity(), d.getId())));

			// set entity on attribute
			list.getDisplayAttributes().stream().forEach(d -> d.setEntity(list.getEntity()));

			// find existing sortable attributes
			list.setSortAttributes(extractSortableAttributes(node, list));

		}else {
		try {
			throw new Exception("PROCEDURA INTERROTTA: Display attributes della lista " + list.getName() + "(con id: "
					+ list.getId() + ") vuoti ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
		list.setConditionalExpressions(getConditionalExpressionExtractor().extractConditionalExpressions(node));
	    
	    return list;
	}

	

	/**
	 * @param node
	 * @param list
	 * @return list of sortable attributes. Added only attributes that are
	 *         displayable
	 */
	private ArrayList<Attribute> extractSortableAttributes(Node node, ListImpl list) {
		
		//array of sort attributes
		ArrayList<Attribute> sortAttributes = new ArrayList<Attribute>();
		
		//check if list has at leas a child
		Node firstChild = node.getFirstChild();
		if (firstChild == null)
			return sortAttributes;
		
		// get child of the node and iterate over its siblings
		Node sibling = node.getFirstChild().getNextSibling();
		
		
		while (sibling != null) {
			if (sibling.getNodeName().equals(VIEWCOMPONENT_SORTABLE_ATTRIBUTE)) {
				Attribute sortAttribute = new Attribute();
				
				for (int attributeCount = 0; attributeCount < sibling.getAttributes().getLength(); attributeCount++) {
					Attr attribute = (Attr) sibling.getAttributes().item(attributeCount);

					switch (attribute.getName()) {

					case "attribute": {
						sortAttribute.setId(attribute.getValue());
						break;
						}
					
					case "order": {
						if (attribute.getValue().equals(Ordering.ASCENDING.value()))
							sortAttribute.setOrdering(Ordering.ASCENDING);
						else
							sortAttribute.setOrdering(Ordering.DESCENDING);
						break;
						}
					
					}
					
				}
				
				// set name of attribute from entity
				sortAttribute.setName(dataModelUtil.findAttributeName(list.getEntity(), sortAttribute.getId()));

				// set type of attribute from entity
				sortAttribute.setType(dataModelUtil.findAttributeType(list.getEntity(), sortAttribute.getId()));

				// set entity on attribute
				sortAttribute.setEntity(list.getEntity());
				
				sortAttributes.add(sortAttribute);
			}
			sibling = sibling.getNextSibling();
		}
		return sortAttributes;
	}



	public ConditionalExpressionExtractor getConditionalExpressionExtractor() {
		return conditionalExpressionExtractor;
	}

	public void setConditionalExpressionExtractor(ConditionalExpressionExtractor conditionalExpressionExtractor) {
		this.conditionalExpressionExtractor = conditionalExpressionExtractor;
	}
}
