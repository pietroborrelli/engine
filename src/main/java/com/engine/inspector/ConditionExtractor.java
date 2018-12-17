package com.engine.inspector;

import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;

public interface ConditionExtractor {

	Condition mapCondition(Node node);

}
