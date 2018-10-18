package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.io.Serializable;
import java.math.BigDecimal;

public class ApproveBeforePayRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6346638152892879543L;
	private String bankCode;
	private String serviceCode;
	private String ref1;
	private String ref2;
	private BigDecimal amount;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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

}
