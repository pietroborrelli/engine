package com.engine.inspector;

import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;

public interface ViewComponentExtractor {

	public ViewComponent extractSpecificViewComponent(String viewComponent) ;
	
	public ViewComponent extractNextViewComponent(String viewComponent);
	
	public ViewComponent mapViewComponent(Node node) throws Exception;
	
}
