package com.engine.inspector;

import java.util.ArrayList;
import java.util.Objects;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.Condition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;
import com.engine.mapper.datamodel.DataModel;

public class ConditionalExpressionExtractor {

	static String SELECTOR = "Selector";
	static String KEY_CONDITION = "KeyCondition";
	static String ATTRIBUTES_CONDITION = "AttributesCondition";
	static String RELATIONSHIPROLE_CONDITION = "RelationshipRoleCondition";

	private ConditionExtractor conditionExtractor;
	private DataModelUtil dataModelUtil;

	public ConditionalExpressionExtractor(DataModel dataModel) {
		this.setDataModelUtil(new DataModelUtil(dataModel));
	}

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
				// find conditions on this conditional expressions
				ArrayList<Condition> conditions = new ArrayList<Condition>();
				for (int c = 0; c < selector.getChildNodes().getLength(); c++) {
					Node condition = selector.getChildNodes().item(c);
					if (condition.getNodeType() == Node.ELEMENT_NODE
							&& Objects.equals(KEY_CONDITION, condition.getNodeName())) {
						System.out.println("Found a " + KEY_CONDITION);
						this.conditionExtractor = new KeyConditionE();
						conditions.add(conditionExtractor.mapCondition(condition));
					}
					if (condition.getNodeType() == Node.ELEMENT_NODE
							&& Objects.equals(ATTRIBUTES_CONDITION, condition.getNodeName())) {
						System.out.println("Found a " + ATTRIBUTES_CONDITION);
						this.conditionExtractor = new AttributesConditionE();
						AttributesCondition temp = (AttributesCondition) conditionExtractor.mapCondition(condition);
						// temp.setAttributes(dataModelUtil.);
						temp.getAttributes().entrySet().stream()
								.forEach(e -> e.setValue(dataModelUtil.findAttributesByEntityAndId(e.getKey().substring(
										0, e.getKey().lastIndexOf("#")),
										e.getKey())));
						conditions.add(temp);
					}
					if (condition.getNodeType() == Node.ELEMENT_NODE
							&& Objects.equals(RELATIONSHIPROLE_CONDITION, condition.getNodeName())) {
						System.out.println("Found a " + RELATIONSHIPROLE_CONDITION);
						this.conditionExtractor = new RelationshipRoleConditionE();
						RelationshipRoleCondition temp = (RelationshipRoleCondition) conditionExtractor
								.mapCondition(condition);
						if (temp.getRole()!=null)
							temp.setRelationship(dataModelUtil
									.findRelationship(temp.getRole().substring(0, temp.getRole().indexOf("#"))));
						temp.setRelationshipRole1(dataModelUtil.findRelationshipRole1(temp.getRole()));
						temp.setRelationshipRole2(dataModelUtil.findRelationshipRole2(temp.getRole()));

						conditions.add(temp);
					}

				}
				conditionalExpression.setConditions(conditions);

				conditionalExpressions.add(conditionalExpression);
			}
		}
		return conditionalExpressions;
	}

	public DataModelUtil getDataModelUtil() {
		return dataModelUtil;
	}

	public void setDataModelUtil(DataModelUtil dataModelUtil) {
		this.dataModelUtil = dataModelUtil;
	}

}
