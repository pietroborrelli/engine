package com.engine.domain.interactionflowelement.viewelement.viewcomponent;

import java.util.ArrayList;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public abstract class Selector extends ViewComponent {
	
	private ArrayList<Attribute> sortAttributes;
	private ArrayList<Attribute> attributes;
	
	
	public ArrayList<Attribute> getSortAttributes() {
		return sortAttributes;
	}
	public void setSortAttributes(ArrayList<Attribute> sortAttributes) {
		this.sortAttributes = sortAttributes;
	}
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}


}
