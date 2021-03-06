package com.engine.inspector;

import java.util.Arrays;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public final class PageExtractorImpl implements PageExtractor {

	private XPath xPath;

	private String[] blackListFiles = { "Properties.wr" };

	public PageExtractorImpl() {
		// create xPath to navigate the model
		XPathFactory xpathfactory = XPathFactory.newInstance();
		this.setxPath(xpathfactory.newXPath());
	}

	/**
	 * @param document
	 * @return name of the page
	 * @throws XPathExpressionException
	 */
	public String extractPageName(Document document) {
		Node node = null;

		// if page belongs to black list
		if (Arrays.stream(blackListFiles)
				.anyMatch(document.getBaseURI().substring(document.getBaseURI().lastIndexOf("/")+1)::equals))
			return null;

		try {
			XPathExpression expr = getxPath().compile("/Page/@name");
			node = (Node) expr.evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException xe) {
			xe.printStackTrace();
		}
		if (node != null && node.getNodeValue() != null)
			return node.getNodeValue();
		else
			throw new NullPointerException(
					"Attenzione: pagina con nome null. Documento di riferimento " + document.getDocumentURI());

	}

	/**
	 * @param document
	 * @return name of the page
	 * @throws XPathExpressionException
	 */
	public String extractPageId(Document document) {
		Node node = null;

		// if page belongs to black list
		if (Arrays.stream(blackListFiles)
				.anyMatch(document.getBaseURI().substring(document.getBaseURI().lastIndexOf("/")+1)::equals))
			return null;

		try {
			XPathExpression expr = getxPath().compile("/Page/@id");
			node = (Node) expr.evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException xe) {
			xe.printStackTrace();
		}
		if (node.getNodeValue() != null)
			return node.getNodeValue();
		else
			throw new NullPointerException(
					"Attenzione: pagina con id null. Documento di riferimento " + document.getDocumentURI());

	}

	public XPath getxPath() {
		return xPath;
	}

	public void setxPath(XPath xPath) {
		this.xPath = xPath;
	}


}
