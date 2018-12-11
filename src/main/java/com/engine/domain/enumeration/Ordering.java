package com.engine.domain.enumeration;

public enum Ordering {

	ASCENDING("ascending"),
	DESCENDING("descending");
	
	private String value;
	
	Ordering(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
