package com.tmb.ecert.setup.vo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Sup03000Vo extends DatatableRequest {

	private static final long serialVersionUID = -4198445133352579189L;
	private Long emailConfig_id;
	private String name;
	private int status;
	
	public Long getEmailConfig_id() {
		return emailConfig_id;
	}
	public void setEmailConfig_id(Long emailConfig_id) {
		this.emailConfig_id = emailConfig_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
}
