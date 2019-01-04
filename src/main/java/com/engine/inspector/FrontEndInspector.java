package com.engine.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.engine.domain.enumeration.Ordering;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.AttributesCondition;
import com.engine.domain.interactionflowelement.conditionalexpression.condition.WrapperAttribute;
import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;
import com.engine.domain.interactionflowelement.interactionflow.InteractionFlow;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.Attribute;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.ViewComponentPart;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.Field;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.FieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.MultipleSelectionFieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.SelectionFieldImpl;
import com.engine.domain.interactionflowelement.viewelement.viewcontainer.Page;
import com.engine.domain.wrapper.Path;
import com.engine.mapper.datamodel.DataModel;

public class FrontEndInspector {

	static String VIEWCOMPONENT_LIST = "PowerIndexUnit";
	static String VIEWCOMPONENT_DETAIL = "DataUnit";
	static String VIEWCOMPONENT_FORM = "EntryUnit";
	static String VIEWCOMPONENT_SELECTOR = "SelectorUnit";

	static String NAVIGATIONFLOW_LINK = "Link";
	static String NAVIGATIONFLOW_OKLINK = "OKLink";
	static String NAVIGATIONFLOW_KOLINK = "KOLink";

	static Integer countPath = 0;

	private DataModel dataModel;
	private DataModelUtil dataModelUtil;
	private XPathUtil xPathUtil;
	private Context context;
	private Document document;
	private Stack<InteractionFlowElement> stack;
	
	/*
	 * support lists for traversing
	 */
	private List<InteractionFlowElement> viewComponents;
	private List<Path> paths;

	public FrontEndInspector(DataModel dataModel) {
		this.setxPathUtil(new XPathUtil());
		this.dataModel = dataModel;
		this.dataModelUtil = new DataModelUtil(dataModel);
		this.stack = new Stack<InteractionFlowElement>();

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
	public List<InteractionFlowElement> findLeavesViewComponents() {
		// get nodes from document
		List<Node> leavesNodes = getxPathUtil().findLeavesNodes(getDocument());
		// map leaves nodes with corresponding view components and return
		return mapNodesIntoViewComponents(leavesNodes);

	}

	public List<Path> extractPaths(List<InteractionFlowElement> viewComponents) {

		this.setViewComponents(viewComponents);
		this.setPaths(new ArrayList<Path>());
		
		List<InteractionFlowElement> nodeViewComponents = new ArrayList<InteractionFlowElement>();
		viewComponents.stream().forEach(vc -> {
			if (vc.getIsRoot())
				nodeViewComponents.add(vc);
		});

		if (nodeViewComponents.isEmpty()) {
			System.err.println("Non ho trovato view component ROOT!");
			// return null;
		}
		
		System.out.println("----------- CALCOLO PERCORSI -------------");

		for (InteractionFlowElement nodeViewComponent : nodeViewComponents) {
			
			getStack().push(nodeViewComponent);
			traverse(nodeViewComponent);
			getStack().pop();
		}
		
		return removeDuplicates(getPaths());
		
		
	}

	private List<Path> removeDuplicates(List<Path> paths2) {
		
		Boolean equals = true;
		
		for (Path x : getPaths()) {
			
			for (Path x2 : paths2) {
			
				if (!x.equals(x2))
					equals=false;
			
			}
		}
		return paths2;
	}

	/**
	 * recursive function to explore the paths
	 * 
	 * @param node
	 * @throws Exception
	 */
	private void traverse(InteractionFlowElement interactionFlowElement) {
		System.out.println(interactionFlowElement.getId());
		if (interactionFlowElement.getOutInteractionFlows().isEmpty() ) { // sono arrivato al nodo foglia
			printPath(getStack());
			savePath(getStack());
			return;
		}

		// for each IN interaction flow get the source node from the document e mapped
		// in application domain
		for (InteractionFlow outInteractionFlow : interactionFlowElement.getOutInteractionFlows()) {

			// condition added for actions which are temporary null.. to be removed when actions are implemented
			if (outInteractionFlow.getTargetInteractionFlowElement() != null) {

				getStack().push(getViewComponents().stream()
						.filter(vc -> vc.getId().equals(outInteractionFlow.getTargetInteractionFlowElement().getId()))
						.collect(Collectors.toList()).get(0));

				traverse(getViewComponents().stream()
						.filter(vc -> vc.getId().equals(outInteractionFlow.getTargetInteractionFlowElement().getId()))
						.collect(Collectors.toList()).get(0));
				
				// condition added for actions which are temporary null.. to be removed when actions are implemented
				if (outInteractionFlow.getTargetInteractionFlowElement() != null) 
					getStack().pop();

			}else { //handled elements != view components
				//may produce duplicate paths according with the outgoing arcs
				printPath(getStack());
				savePath(getStack());
			}
		}

	}

	private void savePath(Stack<InteractionFlowElement> stackTemp) {
		
		List <InteractionFlowElement> interactionFlowElements = new ArrayList<InteractionFlowElement>();
		
		stackTemp.forEach(k -> {
			interactionFlowElements.add(k);
		});
		
		getPaths().add(new Path(countPath,interactionFlowElements));
		
	}

	private void printPath(Stack<InteractionFlowElement> stackTemp) {
		System.out.println("Percorso n." + countPath);
		stackTemp.forEach(k -> {
			System.out.println("" + k);
		});
		countPath++;
	}

	private List<InteractionFlow> mapNodesIntoInteractionFlow(List<Node> interactionFlowsNodes) {

		List<InteractionFlow> interactionFlows = new ArrayList<InteractionFlow>();
		for (Node node : interactionFlowsNodes) {
			Boolean found = false;
			if (node.getNodeName().equals(NAVIGATIONFLOW_LINK)) {
				this.context = new Context(new Link(dataModel));
				InteractionFlow interactionFlow = this.context.mapInteractionFlow(node);

				for (BindingParameter bindingParameter : interactionFlow.getBindingParameter()) {
					bindingParameter.setTargets(findTargetsOfBindingParameter(bindingParameter, getDocument()));
					bindingParameter.setSources(findSourcesOfBindingParameter(bindingParameter, getDocument()));
				}

				interactionFlows.add(interactionFlow);
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
				interactionFlows.get(interactionFlows.size() - 1).setSourceInteractionFlowElement(
						mapNodeIntoInteractionFlowElement(xPathUtil.findSourceOfInteractionFlow(getDocument(),
								interactionFlows.get(interactionFlows.size() - 1))));

				interactionFlows.get(interactionFlows.size() - 1).setTargetInteractionFlowElement(
						mapNodeIntoInteractionFlowElement(xPathUtil.findTargetOfInteractionFlow(getDocument(),
								interactionFlows.get(interactionFlows.size() - 1))));
			}
		}
		return interactionFlows;
	}

	/**
	 * @param interactionFlow
	 * @param document
	 * @return an update binding parameter list with source ViewComponentParts
	 */
	private List<ViewComponentPart> findTargetsOfBindingParameter(BindingParameter bindingParameter,
			Document document) {

		List<ViewComponentPart> viewComponentParts = new ArrayList<ViewComponentPart>();

		Attribute attribute = new Attribute();

		// kcond / rcond cases, looking for attribute
		if (bindingParameter.getTargetId().contains("ent")) {
			bindingParameter.setTargetId(
					bindingParameter.getTargetId().substring(bindingParameter.getTargetId().lastIndexOf("ent")));

			if (bindingParameter.getTargetId().contains("Array"))
				bindingParameter.setTargetId(bindingParameter.getTargetId().substring(0,
						bindingParameter.getTargetId().lastIndexOf("Array")));
			// è passato un entita a un action ... escludo
			// TODO binding parameter su action
			if (bindingParameter.getTargetId().equals("entityBean"))
				return null;

			attribute.setEntity(dataModelUtil.findEntity(
					bindingParameter.getTargetId().substring(0, bindingParameter.getTargetId().lastIndexOf("#"))));
			attribute.setId(bindingParameter.getTargetId());
			attribute.setName(dataModelUtil.findAttributeName(attribute.getEntity(), bindingParameter.getTargetId()));
			attribute.setType(dataModelUtil.findAttributeType(attribute.getEntity(), bindingParameter.getTargetId()));
			attribute.setKey(true);

			viewComponentParts.add(attribute);

			return viewComponentParts;

		}

		// acond case, looks for attribute
		if (bindingParameter.getTargetId().contains("acond")) {

			AttributesConditionE attributesConditionExtractor = new AttributesConditionE();
			AttributesCondition attributesCondition = new AttributesCondition();

			attributesCondition = (AttributesCondition) attributesConditionExtractor
					.mapCondition(xPathUtil.findAttributesConditionById(bindingParameter.getTargetId(), getDocument()));

			for (WrapperAttribute wrapperAttribute : attributesCondition.getAttributes()) {

				Attribute attributeApplication = new Attribute();

				attributeApplication.setEntity(dataModelUtil.findEntity(wrapperAttribute.getId().substring(0, wrapperAttribute.getId().lastIndexOf("#"))));
				attributeApplication.setId(wrapperAttribute.getId());
				attributeApplication.setName(dataModelUtil.findAttributeName(attributeApplication.getEntity(), wrapperAttribute.getId()));
				attributeApplication.setType(dataModelUtil.findAttributeType(attributeApplication.getEntity(), wrapperAttribute.getId()));
				attributeApplication.setKey(true);
				// candidate to be sort key of NOAM
				if (attributesCondition.getPredicate().equals("lteq")
						|| attributesCondition.getPredicate().equals("lt"))
					attributeApplication.setOrdering(Ordering.ASCENDING);
				if (attributesCondition.getPredicate().equals("gteq")
						|| attributesCondition.getPredicate().equals("gt"))
					attributeApplication.setOrdering(Ordering.DESCENDING);

				viewComponentParts.add(attributeApplication);
			}

			return viewComponentParts;
		}

		// fields cases
		if (bindingParameter.getTargetId().contains("fld")) {
			EntryUnit entryUnitExtractor = new EntryUnit(getDataModel());

			Field field = (Field) entryUnitExtractor
					.mapField(xPathUtil.findFieldById(entryUnitExtractor.getFieldType(bindingParameter.getTargetId()),
							bindingParameter.getTargetId(), getDocument()));

			if (field instanceof FieldImpl)
				viewComponentParts.add(((FieldImpl) field).getAttribute());

			if (field instanceof SelectionFieldImpl) {
				Attribute selectionFieldAttribute;

				// search on relationshiprole1
				if (dataModelUtil.findRelationshipRole1(
						((SelectionFieldImpl) field).getRelationshipRoleCondition().getId()) != null)
					selectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole1(((SelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());
				else // search on relationshiprole1
					selectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole2(((SelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());

				selectionFieldAttribute.setEntity(dataModelUtil.findEntity(selectionFieldAttribute.getId().substring(0,
						selectionFieldAttribute.getId().lastIndexOf("#"))));
				selectionFieldAttribute.setName(dataModelUtil.findAttributeName(selectionFieldAttribute.getEntity(),
						selectionFieldAttribute.getId()));
				selectionFieldAttribute.setType(dataModelUtil.findAttributeType(selectionFieldAttribute.getEntity(),
						selectionFieldAttribute.getId()));
				selectionFieldAttribute.setKey(true);

				viewComponentParts.add(selectionFieldAttribute);
			}

			if (field instanceof MultipleSelectionFieldImpl) {
				Attribute multipleSelectionFieldAttribute;

				// search on relationshiprole1
				if (dataModelUtil.findRelationshipRole1(
						((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId()) != null)
					multipleSelectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole1(
									((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());
				else // search on relationshiprole1
					multipleSelectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole2(
									((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());

				multipleSelectionFieldAttribute.setEntity(dataModelUtil.findEntity(multipleSelectionFieldAttribute
						.getId().substring(0, multipleSelectionFieldAttribute.getId().lastIndexOf("#"))));
				multipleSelectionFieldAttribute.setName(dataModelUtil.findAttributeName(
						multipleSelectionFieldAttribute.getEntity(), multipleSelectionFieldAttribute.getId()));
				multipleSelectionFieldAttribute.setType(dataModelUtil.findAttributeType(
						multipleSelectionFieldAttribute.getEntity(), multipleSelectionFieldAttribute.getId()));
				multipleSelectionFieldAttribute.setKey(true);

				viewComponentParts.add(multipleSelectionFieldAttribute);
			}

			return viewComponentParts;
		}

		return null;
	}

	/**
	 * @param interactionFlow
	 * @param document
	 * @return an update binding parameter list with source ViewComponentParts
	 */
	private List<ViewComponentPart> findSourcesOfBindingParameter(BindingParameter bindingParameter,
			Document document) {

		List<ViewComponentPart> viewComponentParts = new ArrayList<ViewComponentPart>();

		Attribute attribute = new Attribute();

		// kcond / rcond cases, looking for attribute
		if (bindingParameter.getSourceId().contains("ent")) {
			bindingParameter.setSourceId(
					bindingParameter.getSourceId().substring(bindingParameter.getSourceId().lastIndexOf("ent")));

			if (bindingParameter.getSourceId().contains("Array"))
				bindingParameter.setSourceId(bindingParameter.getSourceId().substring(0,
						bindingParameter.getSourceId().lastIndexOf("Array")));
			// è passato un entita a un action ... escludo
			// TODO binding parameter su action
			if (bindingParameter.getSourceId().equals("entityBean"))
				return null;

			attribute.setEntity(dataModelUtil.findEntity(
					bindingParameter.getSourceId().substring(0, bindingParameter.getSourceId().lastIndexOf("#"))));
			attribute.setId(bindingParameter.getSourceId());
			attribute.setName(dataModelUtil.findAttributeName(attribute.getEntity(), bindingParameter.getSourceId()));
			attribute.setType(dataModelUtil.findAttributeType(attribute.getEntity(), bindingParameter.getSourceId()));
			attribute.setKey(true);

			viewComponentParts.add(attribute);

			return viewComponentParts;

		}

		// acond case, looks for attribute
		if (bindingParameter.getSourceId().contains("acond")) {

			AttributesConditionE attributesConditionExtractor = new AttributesConditionE();
			AttributesCondition attributesCondition = new AttributesCondition();

			attributesCondition = (AttributesCondition) attributesConditionExtractor
					.mapCondition(xPathUtil.findAttributesConditionById(bindingParameter.getSourceId(), getDocument()));

			for (WrapperAttribute wrapperAttribute : attributesCondition.getAttributes()) {

				Attribute attributeApplication = new Attribute();

				attributeApplication.setEntity(dataModelUtil.findEntity(wrapperAttribute.getId().substring(0, wrapperAttribute.getId().lastIndexOf("#"))));
				attributeApplication.setId(wrapperAttribute.getId());
				attributeApplication.setName(dataModelUtil.findAttributeName(attributeApplication.getEntity(), wrapperAttribute.getId()));
				attributeApplication.setType(dataModelUtil.findAttributeType(attributeApplication.getEntity(), wrapperAttribute.getId()));
				attributeApplication.setKey(true);
				// candidate to be sort key of NOAM
				if (attributesCondition.getPredicate().equals("lteq")
						|| attributesCondition.getPredicate().equals("lt"))
					attributeApplication.setOrdering(Ordering.ASCENDING);
				if (attributesCondition.getPredicate().equals("gteq")
						|| attributesCondition.getPredicate().equals("gt"))
					attributeApplication.setOrdering(Ordering.DESCENDING);

				viewComponentParts.add(attributeApplication);
			}

			return viewComponentParts;
		}

		// fields cases
		if (bindingParameter.getSourceId().contains("fld")) {
			EntryUnit entryUnitExtractor = new EntryUnit(getDataModel());

			Field field = (Field) entryUnitExtractor
					.mapField(xPathUtil.findFieldById(entryUnitExtractor.getFieldType(bindingParameter.getSourceId()),
							bindingParameter.getSourceId(), getDocument()));

			if (field instanceof FieldImpl)
				viewComponentParts.add(((FieldImpl) field).getAttribute());

			if (field instanceof SelectionFieldImpl) {
				Attribute selectionFieldAttribute;

				// search on relationshiprole1
				if (dataModelUtil.findRelationshipRole1(
						((SelectionFieldImpl) field).getRelationshipRoleCondition().getId()) != null)
					selectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole1(((SelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());
				else // search on relationshiprole1
					selectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole2(((SelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());

				selectionFieldAttribute.setEntity(dataModelUtil.findEntity(selectionFieldAttribute.getId().substring(0,
						selectionFieldAttribute.getId().lastIndexOf("#"))));
				selectionFieldAttribute.setName(dataModelUtil.findAttributeName(selectionFieldAttribute.getEntity(),
						selectionFieldAttribute.getId()));
				selectionFieldAttribute.setType(dataModelUtil.findAttributeType(selectionFieldAttribute.getEntity(),
						selectionFieldAttribute.getId()));
				selectionFieldAttribute.setKey(true);

				viewComponentParts.add(selectionFieldAttribute);
			}

			if (field instanceof MultipleSelectionFieldImpl) {
				Attribute multipleSelectionFieldAttribute;

				// search on relationshiprole1
				if (dataModelUtil.findRelationshipRole1(
						((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId()) != null)
					multipleSelectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole1(
									((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());
				else // search on relationshiprole1
					multipleSelectionFieldAttribute = new Attribute(dataModelUtil
							.findRelationshipRole2(
									((MultipleSelectionFieldImpl) field).getRelationshipRoleCondition().getId())
							.getJoinColumn().getAttribute());

				multipleSelectionFieldAttribute.setEntity(dataModelUtil.findEntity(multipleSelectionFieldAttribute
						.getId().substring(0, multipleSelectionFieldAttribute.getId().lastIndexOf("#"))));
				multipleSelectionFieldAttribute.setName(dataModelUtil.findAttributeName(
						multipleSelectionFieldAttribute.getEntity(), multipleSelectionFieldAttribute.getId()));
				multipleSelectionFieldAttribute.setType(dataModelUtil.findAttributeType(
						multipleSelectionFieldAttribute.getEntity(), multipleSelectionFieldAttribute.getId()));
				multipleSelectionFieldAttribute.setKey(true);

				viewComponentParts.add(multipleSelectionFieldAttribute);
			}

			return viewComponentParts;
		}

		return null;
	}

	/**
	 * @param viewComponentNodes
	 * @return list of view components mapped in application domain
	 */
	public List<InteractionFlowElement> mapNodesIntoViewComponents(List<Node> viewComponentNodes) {
		List<InteractionFlowElement> viewComponents = new ArrayList<InteractionFlowElement>();
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

	public List<InteractionFlowElement> findViewComponents() {
		// get nodes from document
		List<Node> nodes = getxPathUtil().findAllNodes(getDocument());
		// map leaves nodes with corresponding view components and return
		List<InteractionFlowElement> viewComponents = mapNodesIntoViewComponents(nodes);

		// update view components with in and out interaction flows
		for (InteractionFlowElement viewComponent : viewComponents) {
			viewComponent.setInInteractionFlows(mapNodesIntoInteractionFlow(
					xPathUtil.findIncomingInteractionFlowsOfViewComponent(document, viewComponent)));
			viewComponent.setOutInteractionFlows(mapNodesIntoInteractionFlow(
					xPathUtil.findOutgoingInteractionFlowsOfViewComponent(document, viewComponent)));
			// check if view component is candidate to be root
			if (viewComponent.getInInteractionFlows().isEmpty())
				viewComponent.setIsRoot(true);
		}

		return viewComponents;
	}

	public DataModelUtil getDataModelUtil() {
		return dataModelUtil;
	}

	public void setDataModelUtil(DataModelUtil dataModelUtil) {
		this.dataModelUtil = dataModelUtil;
	}

	public List<InteractionFlowElement> getViewComponents() {
		return viewComponents;
	}

	public void setViewComponents(List<InteractionFlowElement> viewComponents) {
		this.viewComponents = viewComponents;
	}

	public Stack<InteractionFlowElement> getStack() {
		return stack;
	}

	public void setStack(Stack<InteractionFlowElement> stack) {
		this.stack = stack;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
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
