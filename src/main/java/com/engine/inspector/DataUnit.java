package com.engine.inspector;

import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.DetailImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.mapper.datamodel.DataModel;

public final class DataUnit implements ViewComponentExtractor {

	private DataModelUtil dataModelUtil;

	public DataUnit(DataModel dataModel) {
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
	 * @return view component extracted from the node and data model
	 */
	@Override
	public ViewComponent mapViewComponent(Node node) throws Exception {
		DetailImpl detail = new DetailImpl();
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
				detail.setDisplayAttributes(displayAttributes);
				System.out.print("--> estratto\n");
				break;
			}

			case "entity": {
				detail.setEntity(dataModelUtil.findEntity(attribute.getNodeValue()));
				if (detail.getEntity() == null)
					System.out.println("--> scartato - Attenzione entity null\n");
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				detail.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				detail.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		if (detail.getDisplayAttributes()!=null) {
		// get names of attributes from entity
		detail.getDisplayAttributes().stream()
				.forEach(d -> d.setName(dataModelUtil.findAttributeName(detail.getEntity(), d.getId())));

		// get types of attributes from entity
		detail.getDisplayAttributes().stream()
				.forEach(d -> d.setType(dataModelUtil.findAttributeType(detail.getEntity(), d.getId())));

		//set entity on attribute
		detail.getDisplayAttributes().stream().forEach(d-> d.setEntity(detail.getEntity()));
		
		return detail;
		}
		
		throw new Exception("PROCEDURA INTERROTTA: Display attributes del detail " + detail.getName() + "(con id: " + detail.getId() + ") vuoti ");

	}

}
