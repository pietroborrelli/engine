package com.engine.domain.interactionflowelement.conditionalexpression;

import java.util.List;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;

public class ConditionalExpression {

	private String id;

	private String booleanOperator;

	private List<Condition> conditions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBooleanOperator() {
		return booleanOperator;
	}

	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	
}
