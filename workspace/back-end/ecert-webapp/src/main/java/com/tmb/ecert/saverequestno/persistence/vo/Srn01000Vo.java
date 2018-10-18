package com.tmb.ecert.saverequestno.persistence.vo;

import java.math.BigDecimal;
import java.util.Date;

public class Srn01000Vo {

	private Long id;
	private Date reqDate;
	private String ReqDateStr;
	private String tmbReqNo;
	private String ref1;
	private String ref2;
	private BigDecimal amount;
	private String typeDesc;
	private String organizeId;
	private String companyName;
	private String statusName;
	private String statusCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	
	
	
	public String getReqDateStr() {
		return ReqDateStr;
	}

	public void setReqDateStr(String reqDateStr) {
		ReqDateStr = reqDateStr;
	}

	public String getTmbReqNo() {
		return tmbReqNo;
	}

	public void setTmbReqNo(String tmbReqNo) {
		this.tmbReqNo = tmbReqNo;
	}

	public String getRef1() {
		return ref1;
	}

	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}

	public String getRef2() {
		return ref2;
	}

	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
