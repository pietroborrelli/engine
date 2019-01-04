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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isRoot == null) ? 0 : isRoot.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InteractionFlowElement other = (InteractionFlowElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isRoot == null) {
			if (other.isRoot != null)
				return false;
		} else if (!isRoot.equals(other.isRoot))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
}
