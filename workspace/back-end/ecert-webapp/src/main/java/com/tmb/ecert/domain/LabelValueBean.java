package com.tmb.ecert.domain;

import java.io.Serializable;

public class LabelValueBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7841683277563910676L;
	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public LabelValueBean(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LabelValueBean [label=" + label + ", value=" + value + "]";
	}
	
}
