package com.engine.inspector;

import java.util.HashMap;
import java.util.stream.Collectors;

import com.engine.mapper.datamodel.DataModel;
import com.engine.mapper.datamodel.DataModel.Entity;
import com.engine.mapper.datamodel.DataModel.Entity.Attribute;
import com.engine.mapper.datamodel.DataModel.Relationship;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole1;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole2;

public class DataModelUtil {

	private DataModel dataModel;

	public DataModelUtil(DataModel dataModel) {
		this.dataModel = dataModel;
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
	 * @param relationship
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
	 * @param relationship
	 *            role 1
	 * @return relationship role 1 of the data model
	 */
	public RelationshipRole1 findRelationshipRole1(String relationshipRole1) {

		for (Object r : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isRelationship(r) && ((Relationship) r).getRelationshipRole1() != null
					&& ((Relationship) r).getRelationshipRole1().getId().equals(relationshipRole1)) {

				return ((Relationship) r).getRelationshipRole1();
			}
		}

		return null;

	}

	/**
	 * @param relationship
	 *            role 2
	 * @return relationship role 2 of the data model
	 */
	public RelationshipRole2 findRelationshipRole2(String relationshipRole2) {

		for (Object r : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isRelationship(r) && ((Relationship) r).getRelationshipRole2() != null
					&& ((Relationship) r).getRelationshipRole2().getId().equals(relationshipRole2)) {

				return ((Relationship) r).getRelationshipRole2();
			}
		}

		return null;

	}

	/**
	 * @param entity
	 * @return relationship of the data model by name
	 */
	public Relationship findRelationshipByName(String relationshipName) {

		for (Object r : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isRelationship(r) && ((Relationship) r).getName().equals(relationshipName)) {
				return (Relationship) r;
			}
		}
		return null;

	}

	/**
	 * @param entity:
	 *            used to access directly entity
	 * @param idDisplayAttribute:
	 *            id of the attribute displayed in the view component
	 * @return name of the attribute ; null if no name is found
	 */
	public String findAttributeName(Entity entity, String idDisplayAttribute) {
//System.out.println(entity.getName()+"--"+idDisplayAttribute);
		return entity.getAttribute().stream().filter(an -> an.getId().equals(idDisplayAttribute))
				.collect(Collectors.toList()).get(0).getName();

	}

	/**
	 * @param entity:
	 *            used to access directly entity
	 * @param idDisplayAttribute:
	 *            id of the attribute displayed in the view component
	 * @return type of the attribute ; null if no type is found in the entity
	 */
	public String findAttributeType(Entity entity, String idDisplayAttribute) {
		return entity.getAttribute().stream().filter(an -> an.getId().equals(idDisplayAttribute))
				.collect(Collectors.toList()).get(0).getType();
	}
	
	/**
	 * @param entity:
	 *            used to access directly entity
	 * @param attributeName:
	 *            id of the attribute name
	 * @return attribute ; null if no type is found in the entity
	 */
	public Attribute findAttributesByEntityAndId(String entityName, String attributeName){
		
		return findEntity(entityName).getAttribute().stream().filter(an -> an.getId().equals(attributeName))
		.collect(Collectors.toList()).get(0);
		
		
	}
	

	/**
	 * @param entity
	 *            object extracted from the wr file
	 * @return true if entity is instance of entity data model
	 */
	public Boolean isEntity(Object entity) {
		if (entity instanceof Entity)
			return true;
		else
			return false;
	}

	/**
	 * @param relationship
	 *            object extracted from the wr file
	 * @return true if entity is instance of entity data model
	 */
	public Boolean isRelationship(Object relationship) {
		if (relationship instanceof Relationship)
			return true;
		else
			return false;
	}



}
