package com.engine.domain.abstractmodel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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


		Set<Entry> set = new HashSet<>(entries);
		entries.clear();
		entries.addAll(set);
		Collections.sort(entries, (e1, e2) -> (e1.getEntityName()+"."+e1.getName()).compareTo(e2.getEntityName()+"."+e2.getName()));
		
	}

	
}
