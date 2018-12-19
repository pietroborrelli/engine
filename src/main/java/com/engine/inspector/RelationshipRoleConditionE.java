package com.engine.inspector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;

public class RelationshipRoleConditionE implements ConditionExtractor {

	@Override
	public Condition mapCondition(Node node) {
		
		RelationshipRoleCondition relationshipRoleCondition = new RelationshipRoleCondition();
		
		// iterate over each attribute
		for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
			Attr attribute = (Attr) node.getAttributes().item(attributeCount);

			// extract
			System.out.println(attribute.getName() + ":" + attribute.getValue());
			switch (attribute.getName()) {

			case "id": {
				relationshipRoleCondition.setId(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}

			case "name": {
				relationshipRoleCondition.setName(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "predicate": {
				relationshipRoleCondition.setPredicate(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			case "role": {
				relationshipRoleCondition.setRole(attribute.getNodeValue());
				System.out.print("--> estratto\n");
				break;
			}
			
			}

		}
		
		return relationshipRoleCondition;
	}

}
