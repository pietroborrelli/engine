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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((interactionFlowElements == null) ? 0 : interactionFlowElements.hashCode());
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
		Path other = (Path) obj;
		if (interactionFlowElements == null) {
			if (other.interactionFlowElements != null)
				return false;
		} else if (!interactionFlowElements.equals(other.interactionFlowElements))
			return false;
		return true;
	}



	
	
}
