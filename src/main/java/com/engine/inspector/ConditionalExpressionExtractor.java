package com.engine.inspector;

import java.util.ArrayList;
import java.util.Objects;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;

public class ConditionalExpressionExtractor {

	static String SELECTOR = "Selector";

	private ConditionExtractor conditionExtractor;

	public Condition mapCondition(Node node) throws Exception {
		return conditionExtractor.mapCondition(node);
	}

	public ConditionExtractor getConditionExtractor() {
		return conditionExtractor;
	}

	public void setConditionExtractor(ConditionExtractor conditionExtractor) {
		this.conditionExtractor = conditionExtractor;
	}

	/**
	 * @param node
	 * @return list of conditional expressions, empty if no condition is found
	 */
	public ArrayList<ConditionalExpression> extractConditionalExpressions(Node node) {

		ArrayList<ConditionalExpression> conditionalExpressions = new ArrayList<ConditionalExpression>();

		if (!node.hasChildNodes())
			// set empty conditional expressions
			return conditionalExpressions;

		// for each child check if is a selector
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node selector = node.getChildNodes().item(i);
			if (selector.getNodeType() == Node.ELEMENT_NODE && Objects.equals(SELECTOR, selector.getNodeName())) {

				ConditionalExpression conditionalExpression = new ConditionalExpression();

				// iterate over each attribute
				for (int attributeCount = 0; attributeCount < selector.getAttributes().getLength(); attributeCount++) {
					Attr attribute = (Attr) selector.getAttributes().item(attributeCount);

					// extract
					System.out.println(attribute.getName() + ":" + attribute.getValue());
					switch (attribute.getName()) {

					case "id": {
						conditionalExpression.setId(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}

					case "booleanOperator": {
						conditionalExpression.setBooleanOperator(attribute.getNodeValue());
						System.out.print("--> estratto\n");
						break;
					}
					}

				}

				conditionalExpressions.add(conditionalExpression);
			}
		}
		return conditionalExpressions;
	}

}
