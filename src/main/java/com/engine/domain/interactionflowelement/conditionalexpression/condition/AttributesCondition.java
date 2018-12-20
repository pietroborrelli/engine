package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import java.util.HashMap;

import com.engine.mapper.datamodel.DataModel.Entity.Attribute;


public final class AttributesCondition extends Condition {

	private String booleanOperator;
	private HashMap<String,Attribute> attributes;

	public String getBooleanOperator() {
		return booleanOperator;
	}

	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	public HashMap<String,Attribute>  getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String,Attribute> attributes) {
		this.attributes = attributes;
	}

}
