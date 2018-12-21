package com.engine.inspector;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

public interface PageExtractor {

	public String extractPageName(Document document) ;

	public String extractPageId(Document document) ;

}
