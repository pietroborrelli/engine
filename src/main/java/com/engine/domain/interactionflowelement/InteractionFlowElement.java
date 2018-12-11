package com.engine.domain.interactionflowelement;

import java.util.List;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;

public abstract class InteractionFlowElement {

	private List<InteractionFlow> outInteractionFlows;

	private List<InteractionFlow> inInteractionFlows;

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

}
