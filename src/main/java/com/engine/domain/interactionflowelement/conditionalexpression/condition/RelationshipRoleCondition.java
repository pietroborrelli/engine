package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import com.engine.mapper.datamodel.DataModel.Relationship;

public final class RelationshipRoleCondition extends Condition {

	private Relationship relationship;

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

}
