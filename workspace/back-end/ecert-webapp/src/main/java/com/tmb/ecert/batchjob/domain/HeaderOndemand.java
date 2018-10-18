package com.tmb.ecert.batchjob.domain;

import java.io.Serializable;

public class HeaderOndemand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String controlPage;
	private String bankName;
	private String branchCode;
	private String pageNo;
	private String branchName;
	private String systemName;
	private String asDate;
	private String reportId;
	private String reportName;
	private String runDate;
	private String paidType;
	
	public String getControlPage() {
		return controlPage;
	}
	public void setControlPage(String controlPage) {
		this.controlPage = controlPage;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getAsDate() {
		return asDate;
	}
	public void setAsDate(String asDate) {
		this.asDate = asDate;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getPaidType() {
		return paidType;
	}
	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}
	
	
	
}
