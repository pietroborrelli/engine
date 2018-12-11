package com.engine.inspector;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.ViewComponent;
import com.engine.domain.interactionflowelement.viewelement.viewcontainer.Page;
import com.engine.mapper.datamodel.DataModel;

public class FrontEndInspector {

	static String VIEWCOMPONENT_LIST="PowerIndexUnit";
	static String VIEWCOMPONENT_DETAIL="DataUnit";
	static String VIEWCOMPONENT_FORM="EntryUnit";
	
	private DataModel dataModel;
	private XPathUtil xPathUtil;
	private Context context;

	public FrontEndInspector(DataModel dataModel) {
		this.setxPathUtil(new XPathUtil());
		this.dataModel = dataModel;

	}

	
	public Page elaborateDocument(Document document) throws XPathExpressionException {

		this.context = new Context(new PageExtractorImpl());
		return new Page(context.extractPageName(document), context.extractPageId(document));
	}

	public List<ViewComponent> elaborateViewComponents() {
		//this.context = new Context (new  )
		return null;
	}
	
	
	

	/**
	 * @param document
	 * @return the leaves view components in the page (detail, list and form
	 * @throws Exception 
	 */
	public List<ViewComponent> findLeavesViewComponents(Document document) throws Exception {
		//get nodes from document
		List<Node> leavesNodes = getxPathUtil().findLeavesNodes(document);
		
		//map leaves nodes with corresponding view components
		List<ViewComponent> leavesViewComponents = new ArrayList<ViewComponent>();
		for (Node node : leavesNodes ) {
			if (node.getNodeName().equals(VIEWCOMPONENT_DETAIL)){
				this.context = new Context(new DataUnit(dataModel));
				leavesViewComponents.add(this.context.mapViewComponent(node));
			}
			if (node.getNodeName().equals(VIEWCOMPONENT_LIST)){
				this.context = new Context(new PowerIndexUnit(dataModel));
				leavesViewComponents.add(this.context.mapViewComponent(node));
			}
			if (node.getNodeName().equals(VIEWCOMPONENT_FORM)){
				this.context = new Context(new EntryUnit(dataModel));
				leavesViewComponents.add(this.context.mapViewComponent(node));
			}
		}
		
		return leavesViewComponents;
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

}
