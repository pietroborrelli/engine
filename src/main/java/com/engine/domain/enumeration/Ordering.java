package com.engine.domain.enumeration;

public enum Ordering {

	ASC("ascending"),
	DESC("descending");
	
	private String value;
	
	Ordering(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
