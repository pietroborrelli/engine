package com.engine.service;

import java.util.List;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.wrapper.Path;

public interface NoAmService {

	List<Collection> computeAbstractModelsByPaths(List<Path> paths);
	Collection createCollection();
	Block createBlock(Path Path);
	List<Entry> createEntries (Path path);
}
