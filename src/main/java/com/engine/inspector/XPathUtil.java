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

import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;

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
	 * @return list of leaves nodes (without outgoing arcs OR with outgoing arcs
	 *         pointing on actions, but not to view components)
	 */
	public List<Node> findLeavesNodes(Document document) {

		ArrayList<Node> leavesList = (ArrayList<Node>) findLeavesList(document);
		ArrayList<Node> leavesDetail = (ArrayList<Node>) findLeavesDetail(document);
		ArrayList<Node> leavesForm = (ArrayList<Node>) findLeavesForm(document);
		ArrayList<Node> leavesSelector = (ArrayList<Node>) findLeavesSelector(document);

		return Stream.of(leavesList, leavesDetail, leavesForm, leavesSelector).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public List<Node> findLeavesList(Document document) {
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
			nodes = (NodeList) getxPath().compile("//PowerIndexUnit[not(Link)]").evaluate(document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			xe.printStackTrace();
		}

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia PowerIndexUnit");
			leavesNodes.add(node);
		}

		// add leaves with outgoing arcs pointing actions but without outgoing arcs on
		// view components
		try {
			nodes = (NodeList) getxPath()
					.compile("//PowerIndexUnit[Link[contains(@to,'#miu')] and ( "
							+ "not(Link[contains(@to,'#pwu')]) and " + "not(Link[contains(@to,'#enu')]) and "
							+ "not(Link[contains(@to,'#dau')])" + " )" + "]")
					// + "descendant::Link[contains(@to,'#miu') and not[contains(@to,'#enu')]]]")
					.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia PowerIndexUnit");
			leavesNodes.add(node);
		}

		return leavesNodes;
	}

	public List<Node> findLeavesDetail(Document document) {
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//DataUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia DataUnit");
			leavesNodes.add(node);
		}

		// add leaves with outgoing arcs pointing actions but without outgoing arcs on
		// view components
		try {
		nodes = (NodeList) getxPath()
				.compile("//DataUnit[Link[contains(@to,'#miu')] and ( " + "not(Link[contains(@to,'#pwu')]) and "
						+ "not(Link[contains(@to,'#enu')]) and " + "not(Link[contains(@to,'#dau')])" + " )" + "]")
				// + "descendant::Link[contains(@to,'#miu') and not[contains(@to,'#enu')]]]")
				.evaluate(document, XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia DataUnit");
			leavesNodes.add(node);
		}

		return leavesNodes;
	}

	public List<Node> findLeavesForm(Document document) {
		// add leaves without outgoing arcs
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//EntryUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia EntryUnit");
			leavesNodes.add(node);
		}

		// add leaves with outgoing arcs pointing actions but without outgoing arcs on
		// view components
		try {
		nodes = (NodeList) getxPath()
				.compile("//EntryUnit[Link[contains(@to,'#miu')] and ( " + "not(Link[contains(@to,'#pwu')]) and "
						+ "not(Link[contains(@to,'#enu')]) and " + "not(Link[contains(@to,'#dau')])" + " )" + "]")
				// + "descendant::Link[contains(@to,'#miu') and not[contains(@to,'#enu')]]]")
				.evaluate(document, XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia EntryUnit");
			leavesNodes.add(node);
		}

		return leavesNodes;
	}

	public List<Node> findLeavesSelector(Document document) {
		// add leaves without outgoing arcs
		List<Node> leavesNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//SelectorUnit[not(Link)]").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia SelectorUnit");
			leavesNodes.add(node);
		}

		// add leaves with outgoing arcs pointing actions but without outgoing arcs on
		// view components
		try {
		nodes = (NodeList) getxPath()
				.compile("//SelectorUnit[Link[contains(@to,'#miu')] and ( " + "not(Link[contains(@to,'#pwu')]) and "
						+ "not(Link[contains(@to,'#enu')]) and " + "not(Link[contains(@to,'#dau')])" + " )" + "]")
				// + "descendant::Link[contains(@to,'#miu') and not[contains(@to,'#enu')]]]")
				.evaluate(document, XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovata una foglia SelectorUnit");
			leavesNodes.add(node);
		}

		return leavesNodes;
	}

	//TODO find all Nodes
	/**
	 * @param document
	 * @return list of all nodes (without outgoing arcs OR with outgoing arcs
	 *         pointing on actions, but not to view components)
	 */
	public List<Node> findAllNodes(Document document) {

		ArrayList<Node> nodesList = (ArrayList<Node>) findNodesList(document);
		ArrayList<Node> nodesDetail = (ArrayList<Node>) findNodesDetail(document);
		ArrayList<Node> nodesForm = (ArrayList<Node>) findNodesForm(document);
		ArrayList<Node> nodesSelector = (ArrayList<Node>) findNodesSelector(document);

		return Stream.of(nodesList, nodesDetail, nodesForm, nodesSelector).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
	
	

	/**
	 * @param document
	 * @return list of all list nodes
	 */
	public List<Node> findNodesList(Document document) {
		List<Node> nodesList = new ArrayList<Node>();
		NodeList nodes = null;
		try {
			nodes = (NodeList) getxPath().compile("//PowerIndexUnit").evaluate(document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException xe) {
			xe.printStackTrace();
		}

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un PowerIndexUnit");
			nodesList.add(node);
		}

		return nodesList;
	}

	/**
	 * @param document
	 * @return list of all detail nodes
	 */
	public List<Node> findNodesDetail(Document document) {
		List<Node> detailNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//DataUnit").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un DataUnit");
			detailNodes.add(node);
		}

		return detailNodes;
	}

	/**
	 * @param document
	 * @return list of all form nodes
	 */
	public List<Node> findNodesForm(Document document) {
		List<Node> formNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//EntryUnit").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un EntryUnit");
			formNodes.add(node);
		}
		return formNodes;
	}

	/**
	 * @param document
	 * @return list of all selector nodes
	 */
	public List<Node> findNodesSelector(Document document) {
		List<Node> selectorNodes = new ArrayList<Node>();
		NodeList nodes = null;
		try {
		nodes = (NodeList) getxPath().compile("//SelectorUnit").evaluate(document,
				XPathConstants.NODESET);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un SelectorUnit");
			selectorNodes.add(node);
		}

		return selectorNodes;
	}
	
	/**
	 * 
	 * @param document
	 * @param viewComponent
	 * @return true if viewcomponent is root, otherwise false
	 */
	public Boolean isRoot(Document document, ViewComponent viewComponent) {
		NodeList linkNodes = null;
		NodeList okLinkNodes = null;
		NodeList koLinkNodes = null;
		try {
			linkNodes = (NodeList) getxPath().compile("//Link[@to='" + viewComponent.getId() + "']").evaluate(document,
					XPathConstants.NODESET);
			okLinkNodes = (NodeList) getxPath().compile("//OKLink[@to='" + viewComponent.getId() + "']")
					.evaluate(document, XPathConstants.NODESET);
			koLinkNodes = (NodeList) getxPath().compile("//KOLink[@to='" + viewComponent.getId() + "']")
					.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (linkNodes.getLength() == 0 && okLinkNodes.getLength() == 0 && koLinkNodes.getLength() == 0)
			return true;
		else
			return false;
	}

	public List<Node> findIncomingInteractionFlowsOfViewComponent(Document document, ViewComponent viewComponent)
			{
		ArrayList<Node> incomingInteractionFlows = new ArrayList<Node>();
		NodeList nodes=null;
		try {
			nodes = (NodeList) getxPath().compile("//Link[@to='" + viewComponent.getId() + "']").evaluate(document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un link che punta a " + viewComponent.getId());
			incomingInteractionFlows.add(node);
		}

		try {
			nodes = (NodeList) getxPath().compile("//OKLink[@to='" + viewComponent.getId() + "']").evaluate(document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un OK link che punta a " + viewComponent.getId());
			incomingInteractionFlows.add(node);
		}

		try {
			nodes = (NodeList) getxPath().compile("//KOLink[@to='" + viewComponent.getId() + "']").evaluate(document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println("Trovato un KO link che punta a " + viewComponent.getId());
			incomingInteractionFlows.add(node);
		}

		return incomingInteractionFlows;
	}

	public List<Node> findOutgoingInteractionFlowsOfViewComponent(Document document, ViewComponent viewComponent)
			{
		ArrayList<Node> outgoingInteractionFlows = new ArrayList<Node>();
		NodeList nodes;
		try {
			nodes = (NodeList) getxPath().compile("//Link[contains(@id, '" + viewComponent.getId() + "')]")
					.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				System.out.println("Trovato un link che esce da " + viewComponent.getId());
				outgoingInteractionFlows.add(node);
			}

			nodes = (NodeList) getxPath().compile("//OKLink[contains(@id, '" + viewComponent.getId() + "')]")
					.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				System.out.println("Trovato un OK link che esce da " + viewComponent.getId());
				outgoingInteractionFlows.add(node);
			}

			nodes = (NodeList) getxPath().compile("//KOLink[contains(@id, '" + viewComponent.getId() + "')]")
					.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				System.out.println("Trovato un KO link che esce da " + viewComponent.getId());
				outgoingInteractionFlows.add(node);
			}
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}

		return outgoingInteractionFlows;
	}

	/**
	 * @param document
	 * @param interactionFlow
	 * @return source node; null if there is no source
	 */
	public Node findSourceOfInteractionFlow(Document document, InteractionFlow interactionFlow) {
		Node node = null;
		try {

			// Ancestor list
			node = (Node) getxPath().compile("//Link[@id='" + interactionFlow.getId() + "']/ancestor::PowerIndexUnit")
					.evaluate(document, XPathConstants.NODE);
			if (node != null)
				return node;

			// Ancestor detail
			node = (Node) getxPath().compile("//Link[@id='" + interactionFlow.getId() + "']/ancestor::DataUnit")
					.evaluate(document, XPathConstants.NODE);
			if (node != null)
				return node;

			// Ancestor form
			node = (Node) getxPath().compile("//Link[@id='" + interactionFlow.getId() + "']/ancestor::EntryUnit")
					.evaluate(document, XPathConstants.NODE);
			if (node != null)
				return node;

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return node;
	}

	public Node findRelationshipRoleConditionByRole(String role, Document document) {
		Node node = null;
		try{
			node = (Node) getxPath().compile("//RelationshipRoleCondition[@role=" + role + "]").evaluate(document,
				XPathConstants.NODE);
		}catch(XPathExpressionException xe) {
			xe.printStackTrace();
		}
		return node;
	}

}
