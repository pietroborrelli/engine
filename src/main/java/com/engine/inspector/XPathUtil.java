package com.engine.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XPathUtil {

	private XPath xPath;

	public XPathUtil() {
		// create xPath to navigate the model
		XPathFactory xpathfactory = XPathFactory.newInstance();
		this.xPath = xpathfactory.newXPath();
	}

	public XPath getxPath() {
		return xPath;
	}

	public void setxPath(XPath xPath) {
		this.xPath = xPath;
	}

	/**
	 * @param document
	 * @return list of leaves nodes (without outgoing arcs)
	 * @throws XPathExpressionException
	 */
	public List<Node> findLeavesNodes(Document document) throws XPathExpressionException {

		ArrayList<Node> leavesList = (ArrayList<Node>) findLeavesList(document);
		ArrayList<Node> leavesDetail = 	(ArrayList<Node>) findLeavesDetail(document);
		ArrayList<Node> leavesForm = (ArrayList<Node>) findLeavesForm(document);

		return Stream.of(leavesList, leavesDetail, leavesForm )
				.flatMap(Collection::stream)
	            .collect(Collectors.toList());
	}

	public List<Node> findLeavesList(Document document) throws XPathExpressionException {
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = (NodeList) getxPath().compile("//PowerIndexUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia PowerIndexUnit");
			leavesNodes.add(node);
		}
		return leavesNodes;
	}

	public List<Node> findLeavesDetail(Document document) throws XPathExpressionException {
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = (NodeList) getxPath().compile("//DataUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia DataUnit");
			leavesNodes.add(node);
		}
		return leavesNodes;
	}

	public List<Node> findLeavesForm(Document document) throws XPathExpressionException {
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = (NodeList) getxPath().compile("//EntryUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia EntryUnit");
			leavesNodes.add(node);
		}
		return leavesNodes;
	}
	
	public Node findRelationshipRoleConditionByRole(String role, Document document) throws XPathExpressionException {
		Node node = (Node) getxPath().compile("//RelationshipRoleCondition[@role=" + role +"]").evaluate(document,
				XPathConstants.NODE);
		return node;
	}

}
