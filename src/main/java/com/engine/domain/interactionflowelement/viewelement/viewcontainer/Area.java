package com.engine.domain.interactionflowelement.viewelement.viewcontainer;

import java.util.List;

public final class Area extends ViewContainer {

	public Area(String name) {
		setName(name);
	}
	
	private List<Page> pages;

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

}
