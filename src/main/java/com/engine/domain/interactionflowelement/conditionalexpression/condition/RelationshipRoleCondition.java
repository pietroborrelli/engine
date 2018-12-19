package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import com.engine.mapper.datamodel.DataModel.Relationship;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole1;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole2;

public final class RelationshipRoleCondition extends Condition {

	private Relationship relationship;
	private RelationshipRole1 relationshipRole1;
	private RelationshipRole2 relationshipRole2;

	private String role;
	
	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public RelationshipRole1 getRelationshipRole1() {
		return relationshipRole1;
	}

	public void setRelationshipRole1(RelationshipRole1 relationshipRole1) {
		this.relationshipRole1 = relationshipRole1;
	}

	public RelationshipRole2 getRelationshipRole2() {
		return relationshipRole2;
	}

	public void setRelationshipRole2(RelationshipRole2 relationshipRole2) {
		this.relationshipRole2 = relationshipRole2;
	}

}
