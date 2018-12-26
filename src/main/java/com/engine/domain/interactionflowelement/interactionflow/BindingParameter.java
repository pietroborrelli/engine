package com.engine.domain.interactionflowelement.interactionflow;

import java.util.List;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.ViewComponentPart;

public class BindingParameter {

	private String id;

	private String name;

	private String targetId;
	//list because target may be an attributes conditions -> multiple attributes
	private List<ViewComponentPart> targets;

	private String sourceId;
	//list because source may be an attributes conditions -> multiple attributes
	private List<ViewComponentPart> sources;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public List<ViewComponentPart> getTargets() {
		return targets;
	}

	public void setTargets(List<ViewComponentPart> targets) {
		this.targets = targets;
	}

	public List<ViewComponentPart> getSources() {
		return sources;
	}

	public void setSources(List<ViewComponentPart> sources) {
		this.sources = sources;
	}

}
