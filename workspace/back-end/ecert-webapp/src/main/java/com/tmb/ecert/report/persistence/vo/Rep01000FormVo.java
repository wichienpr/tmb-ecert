package com.tmb.ecert.report.persistence.vo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Rep01000FormVo extends DatatableRequest {

	public String dateForm;
	public String dateTo;
	public String organizeId;
	public String companyName;
	public String requestTypeCode;
	public String paidtypeCode;
	
	public String paymentDateForm;
	public String paymentDateTo;

	public String getDateForm() {
		return dateForm;
	}

	public void setDateForm(String dateForm) {
		this.dateForm = dateForm;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
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

	public String getRequestTypeCode() {
		return requestTypeCode;
	}

	public void setRequestTypeCode(String requestTypeCode) {
		this.requestTypeCode = requestTypeCode;
	}

	public String getPaidtypeCode() {
		return paidtypeCode;
	}

	public void setPaidtypeCode(String paidtypeCode) {
		this.paidtypeCode = paidtypeCode;
	}

	public String getPaymentDateForm() {
		return paymentDateForm;
	}

	public void setPaymentDateForm(String paymentDateForm) {
		this.paymentDateForm = paymentDateForm;
	}

	public String getPaymentDateTo() {
		return paymentDateTo;
	}

	public void setPaymentDateTo(String paymentDateTo) {
		this.paymentDateTo = paymentDateTo;
	}
	

}
