package com.engine.inspector;

import java.util.stream.Collectors;

import com.engine.mapper.datamodel.DataModel;
import com.engine.mapper.datamodel.DataModel.Entity;
import com.engine.mapper.datamodel.DataModel.Relationship;

public class DataModelUtil {

	private DataModel dataModel;

	public DataModelUtil(DataModel dataModel) {
		this.dataModel=dataModel;
	}
	
	
	/**
	 * @param entity
	 * @return entity of the data model
	 */
	public Entity findEntity(String entity) {
		
		for (Object e : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isEntity(e) && ((Entity) e).getId().equals(entity)) {
				return (Entity) e;
			}
		}
		return null;
		
		
	}

	/**
	 * @param entity
	 * @return relationship of the data model
	 */
	public Relationship findRelationship(String relationship) {
		
		for (Object r : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isRelationship(r) && ((Relationship) r).getId().equals(relationship)) {
				return (Relationship) r;
			}
		}
		return null;
	
		
	}

	/**
	 * @param entity: used to access directly entity
	 * @param idDisplayAttribute: id of the attribute displayed in the view component
	 * @return name of the attribute ; null if no name is found
	 */
	public String findAttributeName(Entity entity, String idDisplayAttribute) {

		return entity.getAttribute().stream().filter(an -> an.getId().equals(idDisplayAttribute))
				.collect(Collectors.toList()).get(0).getName();

	}

	/**
	 * @param entity: used to access directly entity 
	 * @param idDisplayAttribute: id of the attribute displayed in the view component
	 * @return type of the attribute ; null if no type is found in the entity
	 */
	public String findAttributeType(Entity entity, String idDisplayAttribute) {
		return entity.getAttribute().stream().filter(an -> an.getId().equals(idDisplayAttribute))
				.collect(Collectors.toList()).get(0).getType();
	}
	
	
	
	/**
	 * @param entity object extracted from the wr file
	 * @return true if entity is instance of entity data model
	 */
	public Boolean isEntity(Object entity) {
		if (entity instanceof Entity)
			return true;
		else return false;
	}
	
	
	/**
	 * @param entity object extracted from the wr file
	 * @return true if entity is instance of entity data model
	 */
	public Boolean isRelationship(Object relationship) {
		if (relationship instanceof Relationship)
			return true;
		else return false;
	}

}
