package com.engine.domain.interactionflowelement.conditionalexpression.condition;

import com.engine.mapper.datamodel.DataModel.Entity;
import com.engine.mapper.datamodel.DataModel.Entity.Attribute;

public class WrapperAttribute {

	private String id;
	private Entity entity;
	private Attribute attribute;
	
	public WrapperAttribute() {
		super();
	}
	public WrapperAttribute(String id, Entity entity, Attribute attribute) {
		super();
		this.id = id;
		this.entity = entity;
		this.attribute = attribute;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	
	
}
