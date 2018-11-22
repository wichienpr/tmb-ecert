package com.tmb.ecert.saverequestno.persistence.vo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Srn01000FormVo extends DatatableRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6998117903775735072L;
	private String tmbReqNo;
	private String status;
	private Long createdById;

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public String getTmbReqNo() {
		return tmbReqNo;
	}

	public void setTmbReqNo(String tmbReqNo) {
		this.tmbReqNo = tmbReqNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
