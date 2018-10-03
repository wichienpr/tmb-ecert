package com.tmb.ecert.batchjob.domain;

import java.util.Date;

public class EcertJobMonitoring {
	
	private Long jobMonitoringId;
	private String jobTypeCode;
	private Date startDate;
	private Date stopDate;
	private String endOfDate;
	private String errorDesc;
	private Integer rerunNumber;
	private String rerunById;
	private String rerunByName;
	private Date rerunDatetime;
	
	public Long getJobMonitoringId() {
		return jobMonitoringId;
	}
	public void setJobMonitoringId(Long jobMonitoringId) {
		this.jobMonitoringId = jobMonitoringId;
	}
	public String getJobTypeCode() {
		return jobTypeCode;
	}
	public void setJobTypeCode(String jobTypeCode) {
		this.jobTypeCode = jobTypeCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	public String getEndOfDate() {
		return endOfDate;
	}
	public void setEndOfDate(String endOfDate) {
		this.endOfDate = endOfDate;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public Integer getRerunNumber() {
		return rerunNumber;
	}
	public void setRerunNumber(Integer rerunNumber) {
		this.rerunNumber = rerunNumber;
	}
	public String getRerunById() {
		return rerunById;
	}
	public void setRerunById(String rerunById) {
		this.rerunById = rerunById;
	}
	public String getRerunByName() {
		return rerunByName;
	}
	public void setRerunByName(String rerunByName) {
		this.rerunByName = rerunByName;
	}
	public Date getRerunDatetime() {
		return rerunDatetime;
	}
	public void setRerunDatetime(Date rerunDatetime) {
		this.rerunDatetime = rerunDatetime;
	}
	
}
