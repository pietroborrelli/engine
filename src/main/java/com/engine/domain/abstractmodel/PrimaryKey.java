package com.engine.domain.abstractmodel;

import java.util.List;

public class PrimaryKey {

	private List<PartitionKey> partitionKeys;
	private List<SortKey> sortKeys;

	
	public PrimaryKey(List<PartitionKey> partitionKeys, List<SortKey> sortKeys) {
		super();
		this.partitionKeys = partitionKeys;
		this.sortKeys = sortKeys;
	}
	public List<PartitionKey> getPartitionKeys() {
		return partitionKeys;
	}
	public void setPartitionKeys(List<PartitionKey> partitionKeys) {
		this.partitionKeys = partitionKeys;
	}
	public List<SortKey> getSortKeys() {
		return sortKeys;
	}
	public void setSortKeys(List<SortKey> sortKeys) {
		this.sortKeys = sortKeys;
	}
	
	@Override
	public String toString() {
		return "PrimaryKey [partitionKeys=" + partitionKeys + ", sortKeys=" + sortKeys + "]";
	}
	
	
	
}
