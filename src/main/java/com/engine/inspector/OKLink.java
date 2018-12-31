package com.engine.inspector;

import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.interactionflow.navigationflow.OKLinkImpl;
import com.engine.mapper.datamodel.DataModel;

public final class OKLink implements LinkExtractor {

	private DataModel dataModel;
	
	public OKLink(DataModel dataModel) {
		this.dataModel=dataModel;
	}

	@Override
	public InteractionFlow extractSpecificLink(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractionFlow mapInteractionFlow(Node node) {
		OKLinkImpl okLinkImpl = new OKLinkImpl();
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "to": {
				okLinkImpl.setTo(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "automaticCoupling": {
				okLinkImpl.setAutomaticCoupling(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "id": {
				okLinkImpl.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				okLinkImpl.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			}

		}

		return okLinkImpl;
		
	}

	@Override
	public List<BindingParameter> mapBindingParameter(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

}
