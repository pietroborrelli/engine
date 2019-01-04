package com.engine.inspector;

import org.w3c.dom.Document;

public interface PageExtractor {

	public String extractPageName(Document document) ;

	public String extractPageId(Document document) ;

}
