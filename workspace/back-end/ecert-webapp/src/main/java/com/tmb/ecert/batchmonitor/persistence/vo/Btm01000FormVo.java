package com.tmb.ecert.batchmonitor.persistence.vo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Btm01000FormVo extends DatatableRequest  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8939020944065248006L;
	private String dateFrom;
	private String dateTo;
	private String batchType;
	private String operationType;
	private String batchTypeCode;
	private String operationTypeCode;
	
	
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
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getBatchTypeCode() {
		return batchTypeCode;
	}
	public void setBatchTypeCode(String batchTypeCode) {
		this.batchTypeCode = batchTypeCode;
	}
	public String getOperationTypeCode() {
		return operationTypeCode;
	}
	public void setOperationTypeCode(String operationTypeCode) {
		this.operationTypeCode = operationTypeCode;
	}
	


}
