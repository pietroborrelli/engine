package com.engine.domain.abstractmodel;

import java.util.List;

public class PrimaryKey {

	private List<PartitionKey> partitionKeys;
	private List<SortKey> sortKeys;
	private String idEntity;
	
	public PrimaryKey(List<PartitionKey> partitionKeys, List<SortKey> sortKeys,String idEntity) {
		super();
		this.partitionKeys = partitionKeys;
		this.sortKeys = sortKeys;
		this.idEntity= idEntity;
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
	public String getIdEntity() {
		return idEntity;
	}
	public void setIdEntity(String idEntity) {
		this.idEntity = idEntity;
	}
	@Override
	public String toString() {
		return "PrimaryKey [partitionKeys=" + partitionKeys + ", sortKeys=" + sortKeys + "]";
	}
	
	
	
}
