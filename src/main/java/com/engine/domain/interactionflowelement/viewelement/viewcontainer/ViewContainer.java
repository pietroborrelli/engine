package com.engine.domain.interactionflowelement.viewelement.viewcontainer;
import java.util.List;

import com.engine.domain.interactionflowelement.action.Action;
import com.engine.domain.interactionflowelement.viewelement.ViewElement;

public abstract class ViewContainer extends ViewElement {

	private List<ViewElement> viewElements;

	private List<Action> actions;

	public List<ViewElement> getViewElements() {
		return viewElements;
	}

	public void setViewElements(List<ViewElement> viewElements) {
		this.viewElements = viewElements;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}
