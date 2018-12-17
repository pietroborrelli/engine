package com.engine.inspector;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;

public class Context {

	private ViewComponentExtractor viewComponentExtractor;

	private LinkExtractor linkExtractor;

	private PageExtractor pageExtractor;
	
	public Context(PageExtractor pageExtractor) {
		this.pageExtractor=pageExtractor;
	}

	public Context(ViewComponentExtractor viewComponentExtractor) {
		this.viewComponentExtractor=viewComponentExtractor;
	}
	
	public Context(LinkExtractor linkExtractor) {
		this.linkExtractor=linkExtractor;
	}
	
	public ViewComponentExtractor getViewComponentExtractor() {
		return viewComponentExtractor;
	}

	public void setViewComponentExtractor(ViewComponentExtractor viewComponentExtractor) {
		this.viewComponentExtractor = viewComponentExtractor;
	}

	public LinkExtractor getLinkExtractor() {
		return linkExtractor;
	}

	public void setLinkExtractor(LinkExtractor linkExtractor) {
		this.linkExtractor = linkExtractor;
	}

	public PageExtractor getPageExtractor() {
		return pageExtractor;
	}

	public void setPageExtractor(PageExtractor pageExtractor) {
		this.pageExtractor = pageExtractor;
	}

	public String extractPageName(Document document) throws XPathExpressionException {
		return pageExtractor.extractPageName(document);
	}

	public String extractPageId(Document document) throws XPathExpressionException {
		return pageExtractor.extractPageId(document);
	}
	
	public ViewComponent mapViewComponent(Node node) throws Exception {
		return viewComponentExtractor.mapViewComponent(node);
	}
	
	public InteractionFlow mapInteractionFlow(Node node)throws Exception {
		return linkExtractor.mapInteractionFlow(node);
	}
}
