package com.engine.domain.abstractmodel;

public class Entry {

	private String id;
	private String name;
	private String type;
	private String entityName;
	
	public Entry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Entry(String id) {
		super();
		this.id=id;
	}
	
	public Entry(String id, String name) {
		super();
		this.id=id;
		this.name=name;
	}
	
	public Entry(String id, String name, String type) {
		this.id=id;
		this.name=name;
		this.type=type;	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void concatenateId(String id) {
		this.id += id + " - ";
	}
	
	public void concatenateName(String name) {
		this.name += name + " - ";
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if (entityName!=null)
			return 	"Entry [name=" + entityName+"."+name + ", type=" + type + "]";
		else
			return "Entry [name=" + name + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Entry other = (Entry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getEntityName() {
		return entityName;
	}


	public void setEntityName(String name2) {
		this.entityName = name2;
		
	}
	
}
