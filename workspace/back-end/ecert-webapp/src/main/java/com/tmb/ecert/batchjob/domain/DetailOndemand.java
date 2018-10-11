package com.tmb.ecert.batchjob.domain;

import java.io.Serializable;

public class DetailOndemand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String requestDate;
	private String tmeReqNo;
	private String orgId;
	private String segment;
	private String paidType;
	private String accountNo;
	private String accName;
	private String amount;
	
	
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getTmeReqNo() {
		return tmeReqNo;
	}
	public void setTmeReqNo(String tmeReqNo) {
		this.tmeReqNo = tmeReqNo;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getPaidType() {
		return paidType;
	}
	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	
	
	
}
