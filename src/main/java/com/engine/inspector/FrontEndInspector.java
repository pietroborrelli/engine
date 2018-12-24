package com.engine.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcontainer.Page;
import com.engine.mapper.datamodel.DataModel;

public class FrontEndInspector {

	static String VIEWCOMPONENT_LIST = "PowerIndexUnit";
	static String VIEWCOMPONENT_DETAIL = "DataUnit";
	static String VIEWCOMPONENT_FORM = "EntryUnit";
	static String VIEWCOMPONENT_SELECTOR = "SelectorUnit";

	static String NAVIGATIONFLOW_LINK = "Link";
	static String NAVIGATIONFLOW_OKLINK = "OKLink";
	static String NAVIGATIONFLOW_KOLINK = "KOLink";

	private DataModel dataModel;
	private XPathUtil xPathUtil;
	private Context context;
	private Document document;
	private Stack<ViewComponent> stack;

	public FrontEndInspector(DataModel dataModel) {
		this.setxPathUtil(new XPathUtil());
		this.dataModel = dataModel;

	}

	public Page elaborateDocument() {

		this.context = new Context(new PageExtractorImpl());
		return new Page(context.extractPageName(getDocument()), context.extractPageId(getDocument()));
	}

	/**
	 * @param document
	 * @return the leaves view components in the page (detail, list and form
	 * @throws Exception
	 */
	public List<ViewComponent> findLeavesViewComponents() {
		// get nodes from document
		List<Node> leavesNodes = getxPathUtil().findLeavesNodes(getDocument());
		// map leaves nodes with corresponding view components and return
		return mapNodesIntoViewComponents(leavesNodes);

	}

	public void extractPaths(List<ViewComponent> leavesViewComponents) {
		// initialize stack of support to get all the paths
		// setStack(new Stack<InteractionFlowElement>());
		for (ViewComponent leaf : leavesViewComponents) {
			try {
				traverse(leaf);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * recursive function to explore the paths, only if in
	 * 
	 * @param node
	 * @throws Exception
	 */
	private void traverse(ViewComponent viewComponent) {

		if (xPathUtil.isRoot(document, viewComponent)) { // sono arrivato al nodo radice, condizione
			return;
		}

		// for each IN interaction flow get the source node from the document e mapped
		// in application domain
		// viewComponent.getInInteractionFlows().stream().forEach(in ->
		// in.setSourceInteractionFlowElement(
		// mapNodeIntoViewComponent(xPathUtil.findSourceOfInteractionFlow(document,
		// in))));

		// for (InteractionFlow interactionFlow : viewComponent.getInInteractionFlows())
		// {
		// if (xPathUtil.parentIsViewComponent)
		// traverse(xPathUtil.findParentViewComponent)
		// }

		// for (ViewComponent parent :)
		// traverse(parent);

		return;

	}

	private List<InteractionFlow> mapNodesIntoInteractionFlow(List<Node> interactionFlowsNodes) {

		List<InteractionFlow> interactionFlows = new ArrayList<InteractionFlow>();
		for (Node node : interactionFlowsNodes) {
			Boolean found = false;
			if (node.getNodeName().equals(NAVIGATIONFLOW_LINK)) {
				this.context = new Context(new Link(dataModel));
				interactionFlows.add(this.context.mapInteractionFlow(node));
				found = true;
			}
			if (node.getNodeName().equals(NAVIGATIONFLOW_OKLINK)) {
				this.context = new Context(new OKLink(dataModel));
				interactionFlows.add(this.context.mapInteractionFlow(node));
				found = true;
			}
			if (node.getNodeName().equals(NAVIGATIONFLOW_KOLINK)) {
				this.context = new Context(new KOLink(dataModel));
				interactionFlows.add(this.context.mapInteractionFlow(node));
				found = true;
			}
			if (found) {
				interactionFlows.get(interactionFlows.size()-1).setSourceInteractionFlowElement(
						mapNodeIntoInteractionFlowElement(xPathUtil.findSourceOfInteractionFlow( getDocument(),
								interactionFlows.get(interactionFlows.size()-1))));
				
				interactionFlows.get(interactionFlows.size()-1).setTargetInteractionFlowElement(
						mapNodeIntoInteractionFlowElement(xPathUtil.findTargetOfInteractionFlow( getDocument(),
								interactionFlows.get(interactionFlows.size()-1))));
			}
		}
		return interactionFlows;
	}

	/**
	 * @param viewComponentNodes
	 * @return list of view components mapped in application domain
	 */
	public List<ViewComponent> mapNodesIntoViewComponents(List<Node> viewComponentNodes) {
		List<ViewComponent> viewComponents = new ArrayList<ViewComponent>();
		for (Node node : viewComponentNodes) {
			if (node.getNodeName().equals(VIEWCOMPONENT_DETAIL)) {
				this.context = new Context(new DataUnit(dataModel));
				viewComponents.add(this.context.mapViewComponent(node));
			}
			if (node.getNodeName().equals(VIEWCOMPONENT_LIST)) {
				this.context = new Context(new PowerIndexUnit(dataModel));
				viewComponents.add(this.context.mapViewComponent(node));

			}
			if (node.getNodeName().equals(VIEWCOMPONENT_FORM)) {
				this.context = new Context(new EntryUnit(dataModel));
				viewComponents.add(this.context.mapViewComponent(node));
			}
			if (node.getNodeName().equals(VIEWCOMPONENT_SELECTOR)) {
				this.context = new Context(new SelectorUnit(dataModel));
				viewComponents.add(this.context.mapViewComponent(node));
			}
		}
		return viewComponents;
	}

	/**
	 * @param viewComponentNode
	 * @return a single view component mapped in application domain
	 */
	public ViewComponent mapNodeIntoViewComponent(Node viewComponentNode) {
		try {
			if (viewComponentNode.getNodeName().equals(VIEWCOMPONENT_DETAIL)) {
				this.context = new Context(new DataUnit(dataModel));

				return this.context.mapViewComponent(viewComponentNode);
			}
			if (viewComponentNode.getNodeName().equals(VIEWCOMPONENT_LIST)) {
				this.context = new Context(new PowerIndexUnit(dataModel));
				return this.context.mapViewComponent(viewComponentNode);
			}
			if (viewComponentNode.getNodeName().equals(VIEWCOMPONENT_FORM)) {
				this.context = new Context(new EntryUnit(dataModel));
				return this.context.mapViewComponent(viewComponentNode);
			}
			if (viewComponentNode.getNodeName().equals(VIEWCOMPONENT_SELECTOR)) {
				this.context = new Context(new SelectorUnit(dataModel));
				return this.context.mapViewComponent(viewComponentNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param interaction
	 *            flow element. May be a view component or an action
	 * @return a single view component mapped in application domain
	 */
	public InteractionFlowElement mapNodeIntoInteractionFlowElement(Node interactionFlowElement) {
		try {
			if (interactionFlowElement.getNodeName().equals(VIEWCOMPONENT_DETAIL)) {
				this.context = new Context(new DataUnit(dataModel));

				return this.context.mapViewComponent(interactionFlowElement);
			}
			if (interactionFlowElement.getNodeName().equals(VIEWCOMPONENT_LIST)) {
				this.context = new Context(new PowerIndexUnit(dataModel));
				return this.context.mapViewComponent(interactionFlowElement);
			}
			if (interactionFlowElement.getNodeName().equals(VIEWCOMPONENT_FORM)) {
				this.context = new Context(new EntryUnit(dataModel));
				return this.context.mapViewComponent(interactionFlowElement);
			}
			if (interactionFlowElement.getNodeName().equals(VIEWCOMPONENT_SELECTOR)) {
				this.context = new Context(new SelectorUnit(dataModel));
				return this.context.mapViewComponent(interactionFlowElement);
			}
			// TODO : add here action to found source and target of a link

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public XPathUtil getxPathUtil() {
		return xPathUtil;
	}

	public void setxPathUtil(XPathUtil xPathUtil) {
		this.xPathUtil = xPathUtil;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<ViewComponent> findViewComponents() {
		// get nodes from document
		List<Node> nodes = getxPathUtil().findAllNodes(getDocument());
		// map leaves nodes with corresponding view components and return
		List<ViewComponent> viewComponents = mapNodesIntoViewComponents(nodes);

		for (ViewComponent viewComponent : viewComponents) {
			viewComponent.setInInteractionFlows(mapNodesIntoInteractionFlow(
					xPathUtil.findIncomingInteractionFlowsOfViewComponent(document, viewComponent)));
			viewComponent.setOutInteractionFlows(mapNodesIntoInteractionFlow(
					xPathUtil.findOutgoingInteractionFlowsOfViewComponent(document, viewComponent)));
		}

		return viewComponents;
	}

	// public Stack<InteractionFlowElement> getStack() {
	// return stack;
	// }
	//
	//
	// public void setStack(Stack<InteractionFlowElement> stack) {
	// this.stack = stack;
	// }
	//
	//
}
