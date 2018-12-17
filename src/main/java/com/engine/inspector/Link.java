package com.engine.inspector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.interactionflow.dataflow.DataLinkImpl;
import com.engine.domain.interactionflowelement.interactionflow.navigationflow.LinkImpl;
import com.engine.mapper.datamodel.DataModel;

public final class Link implements LinkExtractor {

	static String DATA_FLOW = "transport";
	static String NORMAL_FLOW = "normal";

	private DataModelUtil dataModelUtil;

	public Link(DataModel dataModel) {
		this.dataModelUtil = new DataModelUtil(dataModel);
	}

	@Override
	public InteractionFlow extractSpecificLink(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @param node: raw node from the document
	 * 
	 * @return interaction flow extracted from the node and data model, can be Link
	 * or DataLink
	 */
	@Override
	public InteractionFlow mapInteractionFlow(Node node) throws Exception {
		String to ="";
		String automaticCoupling ="";
		String type ="";
		String id ="";
		String name ="";
		
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "to": {
				to = attribute.getNodeValue();
				System.out.print("--> estratto\n");
				break;
			}

			case "automaticCoupling": {
				automaticCoupling = attribute.getNodeValue();
				System.out.print("--> estratto\n");
				break;
			}

			case "type": {
				type = attribute.getNodeValue();
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				id = attribute.getNodeValue();
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				name = attribute.getNodeValue();
				System.out.print("--> estratto\n");
				break;
			}
			}

		}
		if (type.equals(NORMAL_FLOW)) {
			LinkImpl linkImpl = new LinkImpl();
			linkImpl.setTo(to);
			linkImpl.setAutomaticCoupling(automaticCoupling);
			linkImpl.setType(type);
			linkImpl.setId(id);
			linkImpl.setName(name);
			
			return linkImpl;
					
		} else if (type.equals(DATA_FLOW)) {
			DataLinkImpl dataLinkImpl = new DataLinkImpl();
			dataLinkImpl.setTo(to);
			dataLinkImpl.setAutomaticCoupling(automaticCoupling);
			dataLinkImpl.setType(type);
			dataLinkImpl.setId(id);
			dataLinkImpl.setName(name);
			
			return dataLinkImpl;
		}
		
		throw new Exception("PROCEDURA INTERROTTA: Trovato un link che non è ne data ne navigation:" + id);

	}

}
