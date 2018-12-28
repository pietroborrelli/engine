package com.engine.domain.interactionflowelement;

import java.util.List;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;

public abstract class InteractionFlowElement {

	private String id;

	private String name;
	
	private List<InteractionFlow> outInteractionFlows;

	private List<InteractionFlow> inInteractionFlows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	private Boolean isRoot = false;
	public void setName(String name) {
		this.name = name;
	}

	public List<InteractionFlow> getOutInteractionFlows() {
		return outInteractionFlows;
	}

	public void setOutInteractionFlows(List<InteractionFlow> outInteractionFlows) {
		this.outInteractionFlows = outInteractionFlows;
	}

	public List<InteractionFlow> getInInteractionFlows() {
		return inInteractionFlows;
	}

	public void setInInteractionFlows(List<InteractionFlow> inInteractionFlows) {
		this.inInteractionFlows = inInteractionFlows;
	}

	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	
}
