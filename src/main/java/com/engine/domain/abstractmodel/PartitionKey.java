package com.engine.domain.abstractmodel;

public class PartitionKey {

	private String id;
	private String name;
	private String type;
	private String referenceAttributeEntity;

	// id contains reference to attribute of entity
	public PartitionKey(String id) {
		super();
		this.id = id;
		this.referenceAttributeEntity=id.substring(id.lastIndexOf(".") + 1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReferenceAttributeEntity() {
		return referenceAttributeEntity;
	}

	public void setReferenceAttributeEntity(String referenceAttributeEntity) {
		this.referenceAttributeEntity = referenceAttributeEntity;
	}

	@Override
	public String toString() {
		return "PartitionKey [name=" + name + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
