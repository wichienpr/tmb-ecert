package com.tmb.ecert.batchjob.domain;

import java.util.Date;

public class AuditLog {
	private Long auditLogId;
	private String actionCode;
	private String description;
	private String createById;
	private String createByName;
	private Date createDatetime;
	
	public Long getAuditLogId() {
		return auditLogId;
	}
	public void setAuditLogId(Long auditLogId) {
		this.auditLogId = auditLogId;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public String getCreatedByName() {
		return createByName;
	}
	public void setCreatedByName(String createByName) {
		this.createByName = createByName;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
}
