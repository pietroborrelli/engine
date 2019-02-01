package com.engine.domain.abstractmodel;

import com.engine.domain.enumeration.Ordering;
import com.engine.domain.enumeration.Predicate;

public class SortKey {

	private String id;
	private String name;
	private String type;
	private String entity;
	private Ordering ordering;
	private Predicate predicate;
	private String valueCondition;
	
	public SortKey(String id, String entity, Ordering ordering) {
		super();
		this.id = id;
		this.ordering = ordering;
		this.entity=entity;
	}
	
	public SortKey(String id) {
		super();
		this.id = id;
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
	
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ordering == null) ? 0 : ordering.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
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
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		else if (entity.equals(other.entity) && !name.equals(other.name))
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
		return "{Optional} SortKey [name=" + entity.toLowerCase() + "_" + name + ", ordering=" + ordering + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public String getValueCondition() {
		return valueCondition;
	}

	public void setValueCondition(String valueCondition) {
		this.valueCondition = valueCondition;
	}
	

}
