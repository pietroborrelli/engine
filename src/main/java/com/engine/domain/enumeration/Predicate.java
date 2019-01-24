package com.engine.domain.enumeration;
public enum Predicate {

	GREATER_OR_EQUAL(">="), //gteq

	LESS_OR_EQUAL("<="), //lteq
	
	LESS("<"), //lt
	
	GREATER(">"), //gt
	
	EQUAL("="), //eq
	
	NOT_EQUAL("!="), //neq

	IN("in"), //in
	
	NOT_IN("not in"), //notIn
	
	BEGIN_WITH("LIKE"), //beginsWith
	
	CONTAINS("LIKE"), //contains
	
	END_WITH("LIKE"), //endsWith
	
	IS_EMPTY("IS EMPTY"), //empty
	
	IS_NOT_EMPTY("IS NOT EMPTY"), //notEmpty
	
	NULL("IS NULL"), //null
	
	IS_NOT_NULL("IS NOT NULL"), //notNull
	
	NOT_BEGIN_WITH("NOT LIKE"), //notBeginsWith
	
	NOT_CONTAINS("NOT LIKE"), //notContains
	
	NOT_END_WITH("NOT LIKE"); //notEndsWith

	
	private String value;
	
	Predicate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
