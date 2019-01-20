package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.wrapper.Path;
import com.engine.inspector.DataModelUtil;

public interface NoAmService {

	List<Collection> computeAbstractModels(Path path);
	Block createBlock(List<InteractionFlowElement> interactionFlowElements, List<Entry> entries);
	void setDataModelUtil(DataModelUtil dataModelUtil);
	List<Entry> createEntries(List<InteractionFlowElement> interactionFlowElements);
	Collection createCollection(List<InteractionFlowElement> interactionFlowElements, Path path, Block block,
			List<Entry> entries);
	List<Collection> optimizeCollections(List<Collection> collections);
	List<Collection> pathOptimization(List<Collection> collections);
	List<Collection> pageOptimization(List<Collection> collectionsPageScope);
	List<Collection> optimizeEntries(List<Collection> collections);
}
