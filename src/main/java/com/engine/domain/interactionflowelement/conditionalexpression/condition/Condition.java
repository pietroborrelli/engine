package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import com.engine.domain.enumeration.Predicate;

public abstract class Condition {

	private String id;

	private String name;

	private Predicate predicate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	
}
