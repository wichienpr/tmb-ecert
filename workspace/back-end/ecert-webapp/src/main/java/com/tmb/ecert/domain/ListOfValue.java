package com.tmb.ecert.domain;

import java.io.Serializable;

public class ListOfValue implements Serializable {
	private static final long serialVersionUID = 3807386286525119881L;
	private String code;
	private Integer type;
	private String typeDesc;
	private String name;
	private Integer sequence;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}