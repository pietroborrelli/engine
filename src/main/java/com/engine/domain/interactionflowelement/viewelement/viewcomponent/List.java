package com.engine.domain.interactionflowelement.viewelement.viewcomponent;

import java.util.ArrayList;

import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public abstract class List extends ViewComponent {

	private ArrayList<Attribute> displayAttributes;

	private ArrayList<Attribute> sortAttributes;

	private ArrayList<ConditionalExpression> conditionalExpressions;

	public ArrayList<Attribute> getDisplayAttributes() {
		return displayAttributes;
	}

	public void setDisplayAttributes(ArrayList<Attribute> displayAttributes) {
		this.displayAttributes = displayAttributes;
	}

	public ArrayList<Attribute> getSortAttributes() {
		return sortAttributes;
	}

	public void setSortAttributes(ArrayList<Attribute> sortAttributes) {
		this.sortAttributes = sortAttributes;
	}

	public ArrayList<ConditionalExpression> getConditionalExpressions() {
		return conditionalExpressions;
	}

	public void setConditionalExpressions(ArrayList<ConditionalExpression> conditionalExpressions) {
		this.conditionalExpressions = conditionalExpressions;
	}

}
