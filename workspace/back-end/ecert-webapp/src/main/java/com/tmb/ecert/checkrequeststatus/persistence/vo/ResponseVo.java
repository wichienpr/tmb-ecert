package com.tmb.ecert.checkrequeststatus.persistence.vo;

public class ResponseVo {
	private String message;
	private String status;
	
	public ResponseVo(String description, String statusCode) {
		this.message = description;
		this.status = statusCode;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
