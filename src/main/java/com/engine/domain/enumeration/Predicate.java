package com.engine.domain.enumeration;
public enum Predicate {

	GREATER_OR_EQUAL(">="),

	LESS_OR_EQUAL("<="),
	
	LESS("<"),
	
	GREATER(">"),
	
	EQUAL("="),

	IN("in"),
	
	NOT_IN("not in");

	
	private String value;
	
	Predicate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
