package com.engine.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.engine.mapper.datamodel.DataModel;
import com.engine.mapper.datamodel.DataModel.Entity;
import com.engine.mapper.datamodel.DataModel.Entity.Attribute;
import com.engine.mapper.datamodel.DataModel.Relationship;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole1;
import com.engine.mapper.datamodel.DataModel.Relationship.RelationshipRole2;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.enumeration.Ordering;
import com.engine.domain.enumeration.Predicate;

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
	 * @param entity
	 * @return entity of the data model
	 */
	public Entity findEntityByName(String nameEntity) {

		for (Object e : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isEntity(e) && ((Entity) e).getName().equals(nameEntity)) {
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
	 * @param source
	 *            entity
	 * @param target
	 *            entity
	 * @return relationship of the data model by source and target entity
	 */
	public Relationship findRelationshipBySourceAndTarget(String sourceEntity, String targetEntity) {

		for (Object r : dataModel.getEntityOrRelationshipOrDatabase()) {
			if (isRelationship(r) && ((Relationship) r).getSourceEntity().equals(sourceEntity)
					&& ((Relationship) r).getTargetEntity().equals(targetEntity)) {
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
		// System.out.println(entity.getName()+"--"+idDisplayAttribute);
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
	public Attribute findAttributesByEntityAndId(String entityName, String attributeId) {

		return findEntity(entityName).getAttribute().stream().filter(an -> an.getId().equals(attributeId))
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

	/**
	 * @param entity
	 * @return lsit of sort keys extracted from the key attribute of the entity. 
	 */
	public List<SortKey> extractKeyAttributesIntoSortKeys(Entity entity) {

		List<Attribute> attributesKey = entity.getAttribute().stream().filter(ak -> (ak.isKey()!=null && ak.isKey()==true))
				.collect(Collectors.toList());
		List<SortKey> sortKeys = new ArrayList<SortKey>();

		for (Attribute attributeKey : attributesKey) {
			SortKey sortKey = new SortKey(attributeKey.getId());
			sortKey.setEntity(entity.getName());
			sortKey.setName(attributeKey.getName());
			sortKey.setOrdering(Ordering.ASC);
			sortKey.setType(attributeKey.getType());

			sortKeys.add(sortKey);

		}
		return sortKeys;

	}

	/**
	 * @param entity
	 * @return list of partition keys extracted from the key attribute of the entity. 
	 */
	public List<PartitionKey> extractKeyAttributesIntoPartitionKeys(Entity entity) {

		List<Attribute> attributesKey = entity.getAttribute().stream().filter(ak -> (ak.isKey()!=null && ak.isKey()==true))
				.collect(Collectors.toList());
		List<PartitionKey> partitionKeys = new ArrayList<PartitionKey>();

		for (Attribute attributeKey : attributesKey) {
			PartitionKey partitionKey = new PartitionKey(attributeKey.getId());
			partitionKey.setEntity(entity.getName());
			partitionKey.setName(attributeKey.getName());
			partitionKey.setType(attributeKey.getType());
			partitionKey.setPredicate(Predicate.EQUAL);
			
			partitionKeys.add(partitionKey);

		}
		return partitionKeys;

	}
	
	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

}
