package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02100Vo {

	private Long id;
	private String custsegmentDesc;

	private String requestDate;
	private String tmbRequestno;
	private String ref1;
	private String ref2;
	private BigDecimal amount;

	private String certypeDesc;
	private String organizeId;
	private String companyName;
	private String status;
	private String reasonOther;
	private String reason;
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
	public String getReasonOther() {
		return reasonOther;
	}
	public void setReasonOther(String reasonOther) {
		this.reasonOther = reasonOther;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
