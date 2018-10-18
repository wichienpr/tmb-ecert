package com.tmb.ecert.report.persistence.vo;

import java.util.List;

public class Rep03000FormVo {

	private String paymentDate;
	private String organizeId;
	private String customerName;

	private String customerNameHead;
	private String organizeIdHead;
	private String companyNameHead;
	private String branchHead;
	private String addressHead;
	private List<Rep03000Vo> rep03000VoList;

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerNameHead() {
		return customerNameHead;
	}

	public void setCustomerNameHead(String customerNameHead) {
		this.customerNameHead = customerNameHead;
	}

	public String getOrganizeIdHead() {
		return organizeIdHead;
	}

	public void setOrganizeIdHead(String organizeIdHead) {
		this.organizeIdHead = organizeIdHead;
	}

	public String getCompanyNameHead() {
		return companyNameHead;
	}

	public void setCompanyNameHead(String companyNameHead) {
		this.companyNameHead = companyNameHead;
	}

	public String getBranchHead() {
		return branchHead;
	}

	public void setBranchHead(String branchHead) {
		this.branchHead = branchHead;
	}

	public String getAddressHead() {
		return addressHead;
	}

	public void setAddressHead(String addressHead) {
		this.addressHead = addressHead;
	}

	public List<Rep03000Vo> getRep03000VoList() {
		return rep03000VoList;
	}

	public void setRep03000VoList(List<Rep03000Vo> rep03000VoList) {
		this.rep03000VoList = rep03000VoList;
	}

}
