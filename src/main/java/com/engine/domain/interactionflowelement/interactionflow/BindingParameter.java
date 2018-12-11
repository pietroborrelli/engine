package com.engine.domain.interactionflowelement.interactionflow;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.ViewComponentPart;

public class BindingParameter {

	private String id;

	private String name;

	private Condition target;

	private ViewComponentPart source;

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

	public Condition getTarget() {
		return target;
	}

	public void setTarget(Condition target) {
		this.target = target;
	}

	public ViewComponentPart getSource() {
		return source;
	}

	public void setSource(ViewComponentPart source) {
		this.source = source;
	}

}
