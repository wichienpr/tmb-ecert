package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02000Vo {

	private Long id;
	private String custsegmentDesc;
	private String custsegmentCode;
	private int custsegmentCount;

	private int certificate;
	private int copyGuarantee;

	private BigDecimal amountDbd;
	private BigDecimal amountTmb;
	private BigDecimal totalAmount;

	private int paymentTypeCountDT;
	private int paymentTypeCountDNoT;
	private int paymentTypeCountDAll;
	private int paymentTypeCountECert;

	private String department;
	private int success;
	private int fail;

	public String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustsegmentDesc() {
		return custsegmentDesc;
	}

	public void setCustsegmentDesc(String custsegmentDesc) {
		this.custsegmentDesc = custsegmentDesc;
	}

	public String getCustsegmentCode() {
		return custsegmentCode;
	}

	public void setCustsegmentCode(String custsegmentCode) {
		this.custsegmentCode = custsegmentCode;
	}

	public int getCustsegmentCount() {
		return custsegmentCount;
	}

	public void setCustsegmentCount(int custsegmentCount) {
		this.custsegmentCount = custsegmentCount;
	}

	public int getCertificate() {
		return certificate;
	}

	public void setCertificate(int certificate) {
		this.certificate = certificate;
	}

	public int getCopyGuarantee() {
		return copyGuarantee;
	}

	public void setCopyGuarantee(int copyGuarantee) {
		this.copyGuarantee = copyGuarantee;
	}

	public BigDecimal getAmountDbd() {
		return amountDbd;
	}

	public void setAmountDbd(BigDecimal amountDbd) {
		this.amountDbd = amountDbd;
	}

	public BigDecimal getAmountTmb() {
		return amountTmb;
	}

	public void setAmountTmb(BigDecimal amountTmb) {
		this.amountTmb = amountTmb;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getPaymentTypeCountDT() {
		return paymentTypeCountDT;
	}

	public void setPaymentTypeCountDT(int paymentTypeCountDT) {
		this.paymentTypeCountDT = paymentTypeCountDT;
	}

	public int getPaymentTypeCountDNoT() {
		return paymentTypeCountDNoT;
	}

	public void setPaymentTypeCountDNoT(int paymentTypeCountDNoT) {
		this.paymentTypeCountDNoT = paymentTypeCountDNoT;
	}

	public int getPaymentTypeCountDAll() {
		return paymentTypeCountDAll;
	}

	public void setPaymentTypeCountDAll(int paymentTypeCountDAll) {
		this.paymentTypeCountDAll = paymentTypeCountDAll;
	}

	public int getPaymentTypeCountECert() {
		return paymentTypeCountECert;
	}

	public void setPaymentTypeCountECert(int paymentTypeCountECert) {
		this.paymentTypeCountECert = paymentTypeCountECert;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
