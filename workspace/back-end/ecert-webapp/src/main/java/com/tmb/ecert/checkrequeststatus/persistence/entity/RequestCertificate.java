package com.tmb.ecert.checkrequeststatus.persistence.entity;

import java.util.Date;

public class RequestCertificate {
	private Long reqCertificateId;
	private Long reqFormId;
	private String certificateCode;
	private Integer totalNumber;

	private String createdById;
	private String createdByName;
	private Date createdDateTime;
	private String updateById;
	private String updateByName;
	private Date updateDateTime;

	public Long getReqCertificateId() {
		return reqCertificateId;
	}

	public void setReqCertificateId(Long reqCertificateId) {
		this.reqCertificateId = reqCertificateId;
	}

	public Long getReqFormId() {
		return reqFormId;
	}

	public void setReqFormId(Long reqFormId) {
		this.reqFormId = reqFormId;
	}

	public String getCertificateCode() {
		return certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getUpdateById() {
		return updateById;
	}

	public void setUpdateById(String updateById) {
		this.updateById = updateById;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public Date getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

}
