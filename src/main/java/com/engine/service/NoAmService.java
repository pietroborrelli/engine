package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.wrapper.Path;

public interface NoAmService {

	List<Collection> computeAbstractModelsByPaths(List<Path> paths);
	Block createBlock(Path path, List<Entry> entries);
	List<Entry> createEntries (Path path);
	Collection createCollection(Path path, Block block, List<Entry> entries);
	List<Collection> optimizeReadingAccessPaths(List<Collection> collections);
}
