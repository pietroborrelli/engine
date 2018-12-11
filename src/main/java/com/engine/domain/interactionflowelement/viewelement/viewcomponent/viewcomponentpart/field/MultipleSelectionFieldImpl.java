package com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;

public class MultipleSelectionFieldImpl extends Field {

	private RelationshipRoleCondition relationshipRoleCondition;

	public MultipleSelectionFieldImpl(String id, String name, String type) {
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

}
