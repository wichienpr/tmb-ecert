package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02200Vo {

	public Long id;
	public String custsegmentDesc;

	public String requestDate;
	public String tmbRequestno;

	public String certypeDesc;
	public String organizeId;
	public String companyName;
	public String department;

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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
