package com.engine.domain.enumeration;
public enum Predicate {

	GREATER_OR_EQUAL("gteq"),

	LESS_OR_EQUAL("lteq"),

	IN("in");

	
	private String value;
	
	Predicate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
