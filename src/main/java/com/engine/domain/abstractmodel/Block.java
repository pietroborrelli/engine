package com.engine.domain.abstractmodel;

import java.util.List;


public class Block {

	private PrimaryKey key;
	private List<Entry> entries;
	
	public Block() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PrimaryKey getKey() {
		return key;
	}
//mettere un to string per convertire la chiave!!
	public void setKey(PrimaryKey key) {
		this.key = key;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(Entry entry) {
		this.entries.add(entry);
	}

	@Override
	public String toString() {
		return "Block [key=" + key + ", entries=" + entries + "]";
	}
	

	
}
