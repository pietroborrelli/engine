package com.engine.domain.interactionflowelement.viewelement.viewcomponent;

import java.util.ArrayList;
import java.util.HashMap;

import com.engine.domain.interactionflowelement.conditionalexpression.ConditionalExpression;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public abstract class Selector extends ViewComponent {
	
	private ArrayList<Attribute> sortAttributes;
	private HashMap<String,com.engine.mapper.datamodel.DataModel.Entity.Attribute> attributes;
	private ArrayList<ConditionalExpression> conditionalExpressions;
	

	public ArrayList<Attribute> getSortAttributes() {
		return sortAttributes;
	}
	public void setSortAttributes(ArrayList<Attribute> sortAttributes) {
		this.sortAttributes = sortAttributes;
	}
	public HashMap<String,com.engine.mapper.datamodel.DataModel.Entity.Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(HashMap<String,com.engine.mapper.datamodel.DataModel.Entity.Attribute> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<ConditionalExpression> getConditionalExpressions() {
		return conditionalExpressions;
	}

	public void setConditionalExpressions(ArrayList<ConditionalExpression> conditionalExpressions) {
		this.conditionalExpressions = conditionalExpressions;
	}


}
