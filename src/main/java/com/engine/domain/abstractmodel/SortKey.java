package com.engine.domain.abstractmodel;

import com.engine.domain.enumeration.Ordering;

public class SortKey {

	private String id;
	private String name;
	private String type;
	private String referenceAttributeEntity;
	private Ordering ordering;
	
	public SortKey(String id, String referenceAttributeEntity, Ordering ordering) {
		super();
		this.id = id;
		this.ordering = ordering;
		this.referenceAttributeEntity=referenceAttributeEntity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Ordering getOrdering() {
		return ordering;
	}
	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}
	
	public String getReferenceAttributeEntity() {
		return referenceAttributeEntity;
	}
	public void setReferenceAttributeEntity(String referenceAttributeEntity) {
		this.referenceAttributeEntity = referenceAttributeEntity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ordering == null) ? 0 : ordering.hashCode());
		result = prime * result + ((referenceAttributeEntity == null) ? 0 : referenceAttributeEntity.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortKey other = (SortKey) obj;
		if (ordering != other.ordering)
			return false;
		if (referenceAttributeEntity == null) {
			if (other.referenceAttributeEntity != null)
				return false;
		} else if (!referenceAttributeEntity.equals(other.referenceAttributeEntity))
			return false;
		return true;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "{Optional} SortKey [name=" + name + ", ordering=" + ordering + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
