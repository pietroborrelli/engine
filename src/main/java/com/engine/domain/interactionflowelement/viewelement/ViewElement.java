package com.engine.domain.interactionflowelement.viewelement;

import com.engine.domain.interactionflowelement.InteractionFlowElement;

public abstract class ViewElement extends InteractionFlowElement {

	private String id;

	private String name;

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

}
