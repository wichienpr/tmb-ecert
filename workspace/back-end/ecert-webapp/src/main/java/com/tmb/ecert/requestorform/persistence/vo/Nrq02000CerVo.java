package com.tmb.ecert.requestorform.persistence.vo;

import java.util.Date;
import java.util.List;

import com.tmb.ecert.common.domain.Certificate;

public class Nrq02000CerVo extends Certificate {
	private Long reqcertificateId;
	private Boolean check;
	private Integer value;
	private Date registeredDate;
	private Integer statementYear;
	private Date acceptedDate;
	private String other;
	private List<Object> children;

	public Long getReqcertificateId() {
		return reqcertificateId;
	}

	public void setReqcertificateId(Long reqcertificateId) {
		this.reqcertificateId = reqcertificateId;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Integer getStatementYear() {
		return statementYear;
	}

	public void setStatementYear(Integer statementYear) {
		this.statementYear = statementYear;
	}

	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public List<Object> getChildren() {
		return children;
	}

	public void setChildren(List<Object> children) {
		this.children = children;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
