package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.io.Serializable;
import java.math.BigDecimal;

public class RealtimePaymentRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6207185958956112492L;
	private String serviceCode;
	private String transactionNo;
	private String ref1;
	private String ref2;
	private String bankCode;
	private String branchCode;
	private String paymentType;
	private BigDecimal payAmount;
	private String paymentDate;
	private String paymentName;
	private String transactionDate;
	private String payloadTS;
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
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
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getPayloadTS() {
		return payloadTS;
	}
	public void setPayloadTS(String payloadTS) {
		this.payloadTS = payloadTS;
	}

	

}
