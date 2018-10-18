package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep03000Vo {

	public Long id;
	public String receiptNo;
	public String paymentDate;
	public String companyName;
	public String organizeId;
	public String address;
	public String branch;
	public BigDecimal amountTmbVat;
	public BigDecimal amountVat;
	public BigDecimal amountTmb;

	private String customerName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public BigDecimal getAmountTmbVat() {
		return amountTmbVat;
	}

	public void setAmountTmbVat(BigDecimal amountTmbVat) {
		this.amountTmbVat = amountTmbVat;
	}

	public BigDecimal getAmountVat() {
		return amountVat;
	}

	public void setAmountVat(BigDecimal amountVat) {
		this.amountVat = amountVat;
	}

	public BigDecimal getAmountTmb() {
		return amountTmb;
	}

	public void setAmountTmb(BigDecimal amountTmb) {
		this.amountTmb = amountTmb;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
