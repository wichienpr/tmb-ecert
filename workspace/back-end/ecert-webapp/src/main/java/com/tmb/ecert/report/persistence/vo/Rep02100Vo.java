package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02100Vo {

	public Long id;
	public String custsegmentDesc;

	public String requestDate;
	public String tmbRequestno;
	public String ref1;
	public String ref2;
	public BigDecimal amount;

	public String certypeDesc;
	public String organizeId;
	public String companyName;
	public String status;
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

	public String getCertypeDesc() {
		return certypeDesc;
	}

	public void setCertypeDesc(String certypeDesc) {
		this.certypeDesc = certypeDesc;
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
