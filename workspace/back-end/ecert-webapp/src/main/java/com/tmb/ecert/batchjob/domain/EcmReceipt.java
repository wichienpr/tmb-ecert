package com.tmb.ecert.batchjob.domain;

public class EcmReceipt {
	
	private Long reqId;
	private Long receiptId;
	private String fileName;
	private String caNumber;
	private String customerSegment;
	private String tmbRequestNo;
	private String companyName;
	private String organizeId;
	private String makerById;
	
	
	public Long getReqId() {
		return reqId;
	}
	public Long getReceiptId() {
		return receiptId;
	}
	public String getFileName() {
		return fileName;
	}
	public String getCaNumber() {
		return caNumber;
	}
	public String getCustomerSegment() {
		return customerSegment;
	}
	public String getTmbRequestNo() {
		return tmbRequestNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getOrganizeId() {
		return organizeId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setCaNumber(String caNumber) {
		this.caNumber = caNumber;
	}
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
	public void setTmbRequestNo(String tmbRequestNo) {
		this.tmbRequestNo = tmbRequestNo;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}
	public String getMakerById() {
		return makerById;
	}
	public void setMakerById(String makerById) {
		this.makerById = makerById;
	}
	
	
	
	
	
	

}
