package com.engine.domain.abstractmodel;

import com.engine.domain.wrapper.Path;

public class Collection {

	
	private String id;
	private String name;
	private Block block;
	private Path path;
	
	public Collection(String id) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Collection [name=" + name + "]";
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	
}
