package com.tmb.ecert.report.persistence.vo;

import java.util.List;

public class RpReqFormVo {

	private Long id;
	private String typeCertificate;
	private String customerName;
	private String telephone;
	private String reqDate;
	private String organizeId;
	private String companyName;
	private String accountNo;
	private String accountName;
	private String tmpReqNo;

	private List<RpReqFormListVo> rpReqFormList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeCertificate() {
		return typeCertificate;
	}

	public void setTypeCertificate(String typeCertificate) {
		this.typeCertificate = typeCertificate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
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

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTmpReqNo() {
		return tmpReqNo;
	}

	public void setTmpReqNo(String tmpReqNo) {
		this.tmpReqNo = tmpReqNo;
	}

	
	public List<RpReqFormListVo> getRpReqFormList() {
		return rpReqFormList;
	}

	public void setRpReqFormList(List<RpReqFormListVo> rpReqFormList) {
		this.rpReqFormList = rpReqFormList;
	}

}
