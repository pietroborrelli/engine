package com.engine.domain.abstractmodel;

import java.util.concurrent.atomic.AtomicInteger;

public class Collection {

	public static final AtomicInteger next = new AtomicInteger(0);
	
	private Integer id;
	private String name;
	private Block block;
	
	public Collection() {
		//auto increment avoid duplicate if two or more collections have same name
		this.id=next.incrementAndGet();
		this.block = new Block();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Collection [name=" + name + "]";
	}
	
	
}
