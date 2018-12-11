package com.engine.domain.interactionflowelement.viewelement.viewcomponent;
import java.util.ArrayList;

import com.engine.domain.interactionflowelement.viewelement.viewcomponent.viewcomponentpart.field.Field;

public abstract class Form extends ViewComponent {

	private ArrayList<Field> fields;

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

}
