package com.engine.domain.abstractmodel;

public class Collection {

	
	private Integer id;
	private String name;
	private Block block;
	
	public Collection(Integer id) {
		this.id = id;
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
