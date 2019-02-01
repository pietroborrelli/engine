package com.engine.inspector;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.enumeration.Ordering;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ListImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.SelectorImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.mapper.datamodel.DataModel;

public final class SelectorUnit implements ViewComponentExtractor {

	static String VIEWCOMPONENT_SORTABLE_ATTRIBUTE = "SortAttribute";

	private DataModelUtil dataModelUtil;
	private ConditionalExpressionExtractor conditionalExpressionExtractor;

	public SelectorUnit(DataModel dataModel) {
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
		SelectorImpl selectorImpl = new SelectorImpl();
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {
			
			case "entity": {
				selectorImpl.setEntity(dataModelUtil.findEntity(attribute.getNodeValue()));
				if (selectorImpl.getEntity() == null)
					System.out.println("--> scartato - Attenzione entity null\n");
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				selectorImpl.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				selectorImpl.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "distinctAttributes":{
				String[] idAttributes = attribute.getNodeValue().split(" ") ;
				selectorImpl.setAttributes(new HashMap<String,com.engine.mapper.datamodel.DataModel.Entity.Attribute>());
				for (String key : idAttributes ) {
					selectorImpl.getAttributes().put(key, dataModelUtil.findAttributesByEntityAndId(key.substring(0,key.lastIndexOf("#")), key));
				}
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		selectorImpl.setConditionalExpressions(getConditionalExpressionExtractor().extractConditionalExpressions(node));
		
		
		return selectorImpl;
	}

	/**
	 * @param node
	 * @param list
	 * @return list of sortable attributes. Added only attributes that are
	 *         displayable
	 */
	private ArrayList<Attribute> findSortableAttributes(Node node, ListImpl list) {
		
		//array of sort attributes
		ArrayList<Attribute> sortAttributes = new ArrayList<Attribute>();
		
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
						if (attribute.getValue().equals(Ordering.ASC.value()))
							sortAttribute.setOrdering(Ordering.ASC);
						else
							sortAttribute.setOrdering(Ordering.DESC);
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
