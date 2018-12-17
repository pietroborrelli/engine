package com.engine.inspector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.interactionflow.navigationflow.KOLinkImpl;
import com.engine.domain.interactionflowelement.interactionflow.navigationflow.OKLinkImpl;
import com.engine.mapper.datamodel.DataModel;

public final class KOLink implements LinkExtractor {

	private DataModelUtil dataModelUtil;

	public KOLink(DataModel dataModel) {
		this.dataModelUtil = new DataModelUtil(dataModel);
	}
	
	@Override
	public InteractionFlow extractSpecificLink(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractionFlow mapInteractionFlow(Node node) throws Exception {
		KOLinkImpl koLinkImpl = new KOLinkImpl();
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "to": {
				koLinkImpl.setTo(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "automaticCoupling": {
				koLinkImpl.setAutomaticCoupling(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				koLinkImpl.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				koLinkImpl.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		return koLinkImpl;
		
	}

}
