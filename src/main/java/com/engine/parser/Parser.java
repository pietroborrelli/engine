package com.engine.parser;

import java.util.List;

import com.engine.domain.abstractmodel.Block;
import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.abstractmodel.PartitionKey;
import com.engine.domain.abstractmodel.SortKey;


public interface Parser {

	public String getNameColumnFamily(Collection collection);
	public String getPrimaryKey(Block block);
	public String getColumns(List<Entry> entries);
	public String buildPhysicalModel(Collection collection);
	public String getSortKeys(Block block);
	public String getPartitionKeys(List<PartitionKey> partitionKeys);
	public String getSortKeys(List<SortKey> sortKeys);
	public String createSelectQuery(Collection collection);
}
