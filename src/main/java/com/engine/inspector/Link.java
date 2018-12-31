package com.engine.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.interactionflow.dataflow.DataLinkImpl;
import com.engine.domain.interactionflowelement.interactionflow.navigationflow.LinkImpl;
import com.engine.mapper.datamodel.DataModel;

public final class Link implements LinkExtractor {

	static String DATA_FLOW = "transport";
	static String NORMAL_FLOW = "normal";
	static String BINDING_PARAMETER = "LinkParameter";

	private DataModelUtil dataModelUtil;
	private XPathUtil xPathUtil;

	public Link(DataModel dataModel) {
		this.dataModelUtil = new DataModelUtil(dataModel);
		this.xPathUtil = xPathUtil;
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
	public InteractionFlow mapInteractionFlow(Node node) {
		String to = "";
		String automaticCoupling = "";
		String type = "";
		String id = "";
		String name = "";

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
			linkImpl.setBindingParameter(mapBindingParameter(node));

			return linkImpl;

		} else if (type.equals(DATA_FLOW)) {
			DataLinkImpl dataLinkImpl = new DataLinkImpl();
			dataLinkImpl.setTo(to);
			dataLinkImpl.setAutomaticCoupling(automaticCoupling);
			dataLinkImpl.setType(type);
			dataLinkImpl.setId(id);
			dataLinkImpl.setName(name);
			dataLinkImpl.setBindingParameter(mapBindingParameter(node));

			return dataLinkImpl;
		}

		try {
			throw new Exception("PROCEDURA INTERROTTA: Trovato un link che non è ne data ne navigation:" + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<BindingParameter> mapBindingParameter(Node node) {
		List<BindingParameter> bindingParameters = new ArrayList<BindingParameter>();
		// for each child check if is a LinkParameter
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node bindingParameterNode = node.getChildNodes().item(i);
			if (bindingParameterNode.getNodeType() == Node.ELEMENT_NODE
					&& Objects.equals(BINDING_PARAMETER, bindingParameterNode.getNodeName())) {
				System.out.println("Trovato un binding parameter sul link");
				BindingParameter bindingParameter = new BindingParameter();

				for (int attributeCount = 0; attributeCount < bindingParameterNode.getAttributes()
						.getLength(); attributeCount++) {
					Attr attribute = (Attr) bindingParameterNode.getAttributes().item(attributeCount);

					// extract
					System.out.println(attribute.getName() + ":" + attribute.getValue());
					switch (attribute.getName()) {

					case "id": {
						bindingParameter.setId(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}

					case "name": {
						bindingParameter.setName(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}

					/*
					 * can be either attribute or field
					 */
					case "source": {
						bindingParameter.setSourceId(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}

					/*
					 * can be either attribute or field
					 */
					case "target": {
						bindingParameter.setTargetId(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}
					
					}

				}
				bindingParameters.add(bindingParameter);
			}
		}
		return bindingParameters;
	}

	public DataModelUtil getDataModelUtil() {
		return dataModelUtil;
	}

	public void setDataModelUtil(DataModelUtil dataModelUtil) {
		this.dataModelUtil = dataModelUtil;
	}

	

}
