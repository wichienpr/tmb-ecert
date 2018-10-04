package com.tmb.ecert.batchjob.domain;

import java.util.Date;

public class EcertJobGLFailed {

	private Long jobGLFailedId;
	private Date paymentDate;
	public Long getJobGLFailedId() {
		return jobGLFailedId;
	}
	public void setJobGLFailedId(Long jobGLFailedId) {
		this.jobGLFailedId = jobGLFailedId;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
}
