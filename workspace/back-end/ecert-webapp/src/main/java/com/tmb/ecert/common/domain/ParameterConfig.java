package com.tmb.ecert.common.domain;

import java.sql.Timestamp;

public class ParameterConfig {
	private Long parameterconfigId;
	private String propertyName;
	private String propertyValue;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDatetime;
	
	public Long getParameterconfigId() {
		return parameterconfigId;
	}
	public void setParameterconfigId(Long parameterconfigId) {
		this.parameterconfigId = parameterconfigId;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getUpdatedById() {
		return updatedById;
	}
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	public String getUpdatedByName() {
		return updatedByName;
	}
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	public Timestamp getUpdatedDatetime() {
		return updatedDatetime;
	}
	public void setUpdatedDatetime(Timestamp updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
}
