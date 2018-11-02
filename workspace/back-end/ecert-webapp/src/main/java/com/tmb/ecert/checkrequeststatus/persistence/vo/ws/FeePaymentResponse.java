package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

public class FeePaymentResponse {
	
	private String postedDate;
	private String postedTime;
	private String statusCode;
	private String description;
	
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(String postedTime) {
		this.postedTime = postedTime;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
