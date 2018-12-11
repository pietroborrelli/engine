package com.engine.inspector;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

public interface PageExtractor {

	public String extractPageName(Document document) throws XPathExpressionException;

	public String extractPageId(Document document) throws XPathExpressionException;

}
