package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.KeyCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.RelationshipRoleCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.WrapperAttribute;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.inspector.DataModelUtil;
import com.engine.mapper.datamodel.DataModel.Entity;

public interface BlockService {

	/**
	 * @param sortKeys
	 * @param partitionKeys
	 * @param distinctExtractedEntitiesKeys
	 * @return new sort key list comprising the attributes id of the entities, in order to guarantee uniqueness of the record.
	 */
	List<SortKey> guaranteeRecordUniqueness(List<SortKey> sortKeys, List<PartitionKey> partitionKeys,
			List<SortKey> distinctExtractedEntitiesKeys);



	/**
	 * @param sortKeys
	 * @return new sort keys without duplicate sort keys that differ only for
	 *         ordering. given priority to DESC order
	 */
	List<SortKey> removeSortKeysWithDifferentOrder(List<SortKey> sortKeys);

	/**
	 * @param partitionKeys
	 * @param sortKeys
	 * @return new partition keys list without keys that are already candidates to
	 *         be sort keys
	 */
	List<PartitionKey> removeDuplicatesFromPartitionKeys(List<PartitionKey> partitionKeys, List<SortKey> sortKeys);

	/**
	 * @param interactionFlowElement
	 * @param partitionKeys
	 * @return sort keys according with the type of the view component
	 */
	List<SortKey> retrieveSortKey(InteractionFlowElement interactionFlowElement, List<PartitionKey> partitionKeys);

	/**
	 * @param ife:
	 *            interaction flow element from which extract attribute(s)
	 * @return partition key computed from targets which express a condition. Are
	 *         generated duplicates Return null if there is no binding parameter
	 */
	List<PartitionKey> retrievePartitionKeysFromLink(InteractionFlowElement ife, InteractionFlowElement nextIfe);

	List<PartitionKey> retrievePartitionKeysFromViewComponent(InteractionFlowElement ife);
	
	SortKey extractSortKeyFromApplicationModel(Attribute attribute);

	SortKey extractSortKeyFromDataModel(AttributesCondition attributesCondition, WrapperAttribute wrapperAttribute);
	
	List<SortKey> removeSortKeysWithOrderIsNull(List<SortKey> sortKeys);
	
	boolean partitionKeysHaveAttributeConditions(List<PartitionKey> partitionKeys,
			WrapperAttribute wrapperAttribute, AttributesCondition attributesCondition);
	
	Boolean partitionKeysHaveSortAttribute(List<PartitionKey> partitionKeys, Attribute sortAttribute) ;
	
	List<SortKey> removeDuplicatesFromSortKeys(List<PartitionKey> partitionKeys,
			List<SortKey> sortKeys);
	
	boolean haveSameSortKeys(List<SortKey> sortKeys, List<SortKey> sortKeys2) ;
	
	boolean haveSamePartitionKeys(List<PartitionKey> partitionKeys, List<PartitionKey> partitionKeys2);

	PartitionKey extractAttributesIntoPartitionKeys(AttributesCondition attributesCondition,
			WrapperAttribute wrapperAttribute);
	

	DataModelUtil getDataModelUtil();

	void setDataModelUtil(DataModelUtil dataModelUtil);

	List<PartitionKey> extractKeyAttributesIntoPartitionKeys(KeyCondition keyCondition, Entity entity);

	PartitionKey extractRelationshipRoleIntoPartitionKeys(RelationshipRoleCondition relationshipRoleCondition,
			String attributeId);

}