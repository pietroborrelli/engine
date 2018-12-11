package com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;

public class FieldImpl extends Field {

	private boolean modifiable;

	private boolean hidden;

	private Attribute attribute;

	public FieldImpl(String id, String name, String type) {
		setId(id);
		setName(name);
		setType(type);
	}
	
	public boolean isModifiable() {
		return modifiable;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

}
