package com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart;

import com.engine.domain.interactionflowelement.InteractionFlowElement;

public abstract class ViewComponentPart extends InteractionFlowElement {

	private String type;

	private String name;

	private String id;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
