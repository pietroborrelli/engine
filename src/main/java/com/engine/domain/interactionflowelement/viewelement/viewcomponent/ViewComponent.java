package com.engine.domain.interactionflowelement.viewelement.viewcomponent;

import java.util.ArrayList;

import com.engine.domain.interactionflowelement.viewelement.ViewElement;
import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.ViewComponentPart;
import com.engine.mapper.datamodel.DataModel.Entity;

public abstract class ViewComponent extends ViewElement {

	private Entity entity;

	private ArrayList<ViewComponentPart> viewComponentParts;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public ArrayList<ViewComponentPart> getViewComponentParts() {
		return viewComponentParts;
	}

	public void setViewComponentParts(ArrayList<ViewComponentPart> viewComponentParts) {
		this.viewComponentParts = viewComponentParts;
	}

}
