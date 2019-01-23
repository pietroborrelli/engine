package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import java.util.List;


public final class AttributesCondition extends Condition {

	private String value;
	private String booleanOperator;
	private List<WrapperAttribute> attributes;

	public String getBooleanOperator() {
		return booleanOperator;
	}

	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	public List<WrapperAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<WrapperAttribute> attributes) {
		this.attributes = attributes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
