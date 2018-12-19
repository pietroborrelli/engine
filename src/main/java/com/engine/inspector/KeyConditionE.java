package com.engine.inspector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.KeyCondition;

public class KeyConditionE implements ConditionExtractor {

	@Override
	public Condition mapCondition(Node node) {
		
		Condition keyCondition = new KeyCondition();
		
		// iterate over each attribute
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "id": {
				keyCondition.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				keyCondition.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "predicate": {
				keyCondition.setPredicate(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			}

		}
		
		return keyCondition;
	}

}
