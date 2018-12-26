package com.engine.inspector;

import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.KeyCondition;

public class AttributesConditionE implements ConditionExtractor {

	@Override
	public Condition mapCondition(Node node) {
		
		AttributesCondition attributesCondition = new AttributesCondition();
		
		// iterate over each attribute
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "id": {
				attributesCondition.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				attributesCondition.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "predicate": {
				attributesCondition.setPredicate(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			case "booleanOperator":{
				attributesCondition.setPredicate(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "attributes":{
				
				String[] idAttributes = attribute.getNodeValue().split(" ") ;
				attributesCondition.setAttributes(new HashMap<String,com.engine.mapper.datamodel.DataModel.Entity.Attribute>());
				for (String key : idAttributes ) {
					attributesCondition.getAttributes().put(key, null);
				}
				System.out.print("--> estratto\n");
				break;
			}
			
			}

		}
		
		return attributesCondition;
	}

	
}
