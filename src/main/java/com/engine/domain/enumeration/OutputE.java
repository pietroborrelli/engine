package com.engine.domain.enumeration;

public enum OutputE {

	NO("no"),
	PATH_OPT("path_opt"),
	PAGE_OPT("page_opt");
	
	private String value;
	
	OutputE(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
