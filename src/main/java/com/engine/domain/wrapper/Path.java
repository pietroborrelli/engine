package com.engine.domain.wrapper;

import java.util.List;

import com.engine.domain.interactionflowelement.InteractionFlowElement;

/*
 * wrapper class
 */
public class Path {

	private Integer idPath;
	private List<InteractionFlowElement> interactionFlowElements;
	
	public Path(Integer idPath,List<InteractionFlowElement> interactionFlowElements) {
		this.idPath = idPath;
		this.interactionFlowElements = interactionFlowElements;
	}
	
	public Integer getIdPath() {
		return idPath;
	}
	public void setIdPath(Integer idPath) {
		this.idPath = idPath;
	}
	public List<InteractionFlowElement> getInteractionFlowElements() {
		return interactionFlowElements;
	}
	public void setInteractionFlowElements(List<InteractionFlowElement> interactionFlowElements) {
		this.interactionFlowElements = interactionFlowElements;
	}



	
	
}
