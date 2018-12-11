package com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public class SelectionFieldImpl extends Field {

	private Attribute attribute;
	
	private RelationshipRoleCondition relationshipRoleCondition;

	public SelectionFieldImpl (String id, String name, String type) {
		setId(id);
		setName(name);
		setType(type);
	}
	
	public RelationshipRoleCondition getRelationshipRoleCondition() {
		return relationshipRoleCondition;
	}

	public void setRelationshipRoleCondition(RelationshipRoleCondition relationshipRoleCondition) {
		this.relationshipRoleCondition = relationshipRoleCondition;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

}
