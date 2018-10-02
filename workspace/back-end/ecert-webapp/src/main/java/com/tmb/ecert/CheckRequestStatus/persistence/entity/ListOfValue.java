package com.tmb.ecert.CheckRequestStatus.persistence.entity;

public class ListOfValue {
	private Long listOfValueId;
	private String code;
	private Integer type;
	private String typeDesc;
	private String name;
	private Integer sequence;

	public Long getListOfValueId() {
		return listOfValueId;
	}

	public void setListOfValueId(Long listOfValueId) {
		this.listOfValueId = listOfValueId;
	}

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
