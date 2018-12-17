package com.engine.inspector;

import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;

public interface LinkExtractor {

	public InteractionFlow extractSpecificLink(String link);

	public InteractionFlow mapInteractionFlow(Node node) throws Exception;

}
