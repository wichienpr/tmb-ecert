package com.tmb.ecert.batchmonitor.persistence.vo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Btm01000Vo extends DatatableRequest {
	
	private int jobmonitoringId;
	private String jobtypeCode;
	private String startDate;
	private String stopDate;
	private String endofdate;
	private String status;
	private String errorDesc;
	private int rerunNumber;
	private String rerunById;
	private String rerunByName;
	private String rerunDatetime;
	
	private String statusDesc;
	private String jobtypeName;
	
	
	public int getJobmonitoringId() {
		return jobmonitoringId;
	}
	public void setJobmonitoringId(int jobmonitoringId) {
		this.jobmonitoringId = jobmonitoringId;
	}
	public String getJobtypeCode() {
		return jobtypeCode;
	}
	public void setJobtypeCode(String jobtypeCode) {
		this.jobtypeCode = jobtypeCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getEndofdate() {
		return endofdate;
	}
	public void setEndofdate(String endofdate) {
		this.endofdate = endofdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public int getRerunNumber() {
		return rerunNumber;
	}
	public void setRerunNumber(int rerunNumber) {
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
	public String getRerunDatetime() {
		return rerunDatetime;
	}
	public void setRerunDatetime(String rerunDatetime) {
		this.rerunDatetime = rerunDatetime;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getJobtypeName() {
		return jobtypeName;
	}
	public void setJobtypeName(String jobtypeName) {
		this.jobtypeName = jobtypeName;
	}
	
	
	
	
}
