package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.sql.Timestamp;

public class RealtimePaymentResponse {

	
	private String statusCode;
	private String description;
	private Timestamp payLoadTs;
	
	public Timestamp getPayLoadTs() {
		return payLoadTs;
	}

	public void setPayLoadTs(Timestamp payLoadTs) {
		this.payLoadTs = payLoadTs;
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
