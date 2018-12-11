package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import java.util.List;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public final class AttributesCondition extends Condition {

	private String booleanOperator;

	private List<Attribute> attributes;

	public String getBooleanOperator() {
		return booleanOperator;
	}

	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

}
