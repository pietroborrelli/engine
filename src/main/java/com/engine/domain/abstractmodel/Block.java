package com.engine.domain.abstractmodel;

import java.util.ArrayList;
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
	
	/**
	 * @param entries
	 * @return set a new list of entries without duplicates(from the optimization reading access path process)
	 */
	public void removeDuplicatesEntries() {

		List<Entry> tempEntries = new ArrayList<Entry>(entries);

		for (int i = 0; i < tempEntries.size() - 1; i++) {

			if (tempEntries.get(i).getId().equals(tempEntries.get(i + 1).getId())) {
				
				getEntries().remove(tempEntries.get(i + 1));
			}

		}

		
	}

	
}
