package com.tmb.ecert.batchjob.domain;

import java.util.Date;

public class EcertHROfficeCode {
	private Long officeCodeId;
	private Date effectiveDate;
	private String type;
	private String officeCode1;
	private String officeCode2;
	private String tdeptEn;
	private String descrshortEn;
	private String tdeptTh;
	private String descrshortTh;
	private String status;
	public Long getOfficeCodeId() {
		return officeCodeId;
	}
	public void setOfficeCodeId(Long officeCodeId) {
		this.officeCodeId = officeCodeId;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOfficeCode1() {
		return officeCode1;
	}
	public void setOfficeCode1(String officeCode1) {
		this.officeCode1 = officeCode1;
	}
	public String getOfficeCode2() {
		return officeCode2;
	}
	public void setOfficeCode2(String officeCode2) {
		this.officeCode2 = officeCode2;
	}
	public String getTdeptEn() {
		return tdeptEn;
	}
	public void setTdeptEn(String tdeptEn) {
		this.tdeptEn = tdeptEn;
	}
	public String getDescrshortEn() {
		return descrshortEn;
	}
	public void setDescrshortEn(String descrshortEn) {
		this.descrshortEn = descrshortEn;
	}
	public String getTdeptTh() {
		return tdeptTh;
	}
	public void setTdeptTh(String tdeptTh) {
		this.tdeptTh = tdeptTh;
	}
	public String getDescrshortTh() {
		return descrshortTh;
	}
	public void setDescrshortTh(String descrshortTh) {
		this.descrshortTh = descrshortTh;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
