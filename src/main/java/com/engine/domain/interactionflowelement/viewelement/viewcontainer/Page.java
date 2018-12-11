package com.engine.domain.interactionflowelement.viewelement.viewcontainer;

import java.util.List;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;

public final class Page extends ViewContainer {

	private List<ViewComponent> viewComponents;

	public Page(String id, String name) {
		setId(id);
		setName(name);
	}
	
	public List<ViewComponent> getViewComponents() {
		return viewComponents;
	}

	public void setViewComponents(List<ViewComponent> viewComponents) {
		this.viewComponents = viewComponents;
	}

}
