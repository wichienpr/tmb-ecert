package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep01000Vo {

	public Long id;
	public String requestDate;
	public String tmbRequestno;
	public String organizeId;
	public String companyName;
	public String custsegmentCode;
	public String custsegmentDesc;
	public String requestTypeCode;
	public String requestTypeDesc;
	public String certypeCode;
	public String certypeDesc;
	public String accountNo;
	public BigDecimal amountDbd;
	public BigDecimal amountTmb;
	public BigDecimal amount;
	public BigDecimal totalAmount;
	public String makerById;
	public String makerByName;
	public String checkerById;
	public String checkerByName;
	public String status;
	public String remark;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getTmbRequestno() {
		return tmbRequestno;
	}

	public void setTmbRequestno(String tmbRequestno) {
		this.tmbRequestno = tmbRequestno;
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

	public String getCustsegmentCode() {
		return custsegmentCode;
	}

	public void setCustsegmentCode(String custsegmentCode) {
		this.custsegmentCode = custsegmentCode;
	}

	public String getCustsegmentDesc() {
		return custsegmentDesc;
	}

	public void setCustsegmentDesc(String custsegmentDesc) {
		this.custsegmentDesc = custsegmentDesc;
	}

	public String getRequestTypeCode() {
		return requestTypeCode;
	}

	public void setRequestTypeCode(String requestTypeCode) {
		this.requestTypeCode = requestTypeCode;
	}

	public String getRequestTypeDesc() {
		return requestTypeDesc;
	}

	public void setRequestTypeDesc(String requestTypeDesc) {
		this.requestTypeDesc = requestTypeDesc;
	}

	public String getCertypeCode() {
		return certypeCode;
	}

	public void setCertypeCode(String certypeCode) {
		this.certypeCode = certypeCode;
	}

	public String getCertypeDesc() {
		return certypeDesc;
	}

	public void setCertypeDesc(String certypeDesc) {
		this.certypeDesc = certypeDesc;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getMakerById() {
		return makerById;
	}

	public void setMakerById(String makerById) {
		this.makerById = makerById;
	}

	public String getMakerByName() {
		return makerByName;
	}

	public void setMakerByName(String makerByName) {
		this.makerByName = makerByName;
	}

	public String getCheckerById() {
		return checkerById;
	}

	public void setCheckerById(String checkerById) {
		this.checkerById = checkerById;
	}

	public String getCheckerByName() {
		return checkerByName;
	}

	public void setCheckerByName(String checkerByName) {
		this.checkerByName = checkerByName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
