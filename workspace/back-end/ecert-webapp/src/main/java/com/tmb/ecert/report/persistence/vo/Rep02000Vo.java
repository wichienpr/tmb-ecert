package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02000Vo {

	public String custsegmentDesc;
	public String custsegmentCode;
	public int custsegmentCount;

	public int certificate;
	public int copyGuarantee;

	public BigDecimal amountDbd;
	public BigDecimal amountTmb;
	public BigDecimal totalAmount;

	public String department;
	public int success;
	public int fail;

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

}
