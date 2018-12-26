package com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart;

import com.engine.domain.enumeration.Ordering;
import com.engine.mapper.datamodel.DataModel.Entity;

public class Attribute extends ViewComponentPart {

	private Entity entity;
	private Ordering ordering;
	private Boolean key;
	
	public Attribute(String id) {
		setId(id);
	}
	
	public Attribute() {
		// TODO Auto-generated constructor stub
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	public Boolean isKey() {
		return key;
	}

	public void setKey(Boolean key) {
		this.key = key;
	}

}
