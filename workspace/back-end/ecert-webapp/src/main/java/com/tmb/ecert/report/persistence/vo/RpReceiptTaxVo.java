package com.tmb.ecert.report.persistence.vo;

public class RpReceiptTaxVo {
	private Long id;
	private String reason;
	private String customerName;
	private String barnchCode;
	private String address;
	private String organizeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getBarnchCode() {
		return barnchCode;
	}

	public String getAddress() {
		return address;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setBarnchCode(String barnchCode) {
		this.barnchCode = barnchCode;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}
	
	

}
