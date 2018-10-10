package com.tmb.ecert.batchmonitor.persistence.vo;

public class Btm01000FormVo {
	
	private String dateFrom;
	private String dateTo;
	private String batchJobType;
	private String batchStatus;
	
	
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getBatchJobType() {
		return batchJobType;
	}
	public void setBatchJobType(String batchJobType) {
		this.batchJobType = batchJobType;
	}
	public String getBatchStatus() {
		return batchStatus;
	}
	public void setBatchStatus(String batchStatus) {
		this.batchStatus = batchStatus;
	}

}
