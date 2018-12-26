package com.engine.inspector;

import java.util.List;

import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;

public interface LinkExtractor {

	public InteractionFlow extractSpecificLink(String link);

	public InteractionFlow mapInteractionFlow(Node node);
	
	public List<BindingParameter> mapBindingParameter(Node node);

}
