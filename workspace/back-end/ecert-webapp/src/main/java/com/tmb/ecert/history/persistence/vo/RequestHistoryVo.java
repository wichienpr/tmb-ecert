package com.tmb.ecert.history.persistence.vo;

import com.tmb.ecert.common.domain.RequestForm;

public class RequestHistoryVo extends RequestForm {
	private Long reqhistoryId;

	public Long getReqhistoryId() {
		return reqhistoryId;
	}

	public void setReqhistoryId(Long reqhistoryId) {
		this.reqhistoryId = reqhistoryId;
	}
	
}
