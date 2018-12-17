package com.engine.domain.interactionflowelement.interactionflow;
import java.util.List;

import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.interactionflow.BindingParameter;

public abstract class InteractionFlow {

	private String id;

	private String name;
	
	private String to;
	
	private String automaticCoupling;
	
	

	private InteractionFlowElement targetInteractionFlowElement;
	private InteractionFlowElement sourceInteractionFlowElement;

	private List<BindingParameter> bindingParameter;

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

	public String getAutomaticCoupling() {
		return automaticCoupling;
	}
	public void setAutomaticCoupling(String automaticCoupling) {
		this.automaticCoupling = automaticCoupling;
	}
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public InteractionFlowElement getTargetInteractionFlowElement() {
		return targetInteractionFlowElement;
	}

	public void setTargetInteractionFlowElement(InteractionFlowElement targetInteractionFlowElement) {
		this.targetInteractionFlowElement = targetInteractionFlowElement;
	}

	public InteractionFlowElement getSourceInteractionFlowElement() {
		return sourceInteractionFlowElement;
	}

	public void setSourceInteractionFlowElement(InteractionFlowElement sourceInteractionFlowElement) {
		this.sourceInteractionFlowElement = sourceInteractionFlowElement;
	}

	public List<BindingParameter> getBindingParameter() {
		return bindingParameter;
	}

	public void setBindingParameter(List<BindingParameter> bindingParameter) {
		this.bindingParameter = bindingParameter;
	}

}
