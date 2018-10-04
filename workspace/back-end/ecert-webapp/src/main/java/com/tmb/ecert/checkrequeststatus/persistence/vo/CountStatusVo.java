package com.tmb.ecert.checkrequeststatus.persistence.vo;

public class CountStatusVo {
	private int newrequest;
	private int paymentProcessing;
	private int refuseRequest;
	private int cancelRequest;
	private int waitPaymentApproval;
	private int paymentApprovals;
	private int chargeback;
	private int paymentfailed;
	private int waitUploadCertificate;
	private int succeed;
	private int waitSaveRequest;

	public int getNewrequest() {
		return newrequest;
	}

	public void setNewrequest(int newrequest) {
		this.newrequest = newrequest;
	}

	public int getPaymentProcessing() {
		return paymentProcessing;
	}

	public void setPaymentProcessing(int paymentProcessing) {
		this.paymentProcessing = paymentProcessing;
	}

	public int getRefuseRequest() {
		return refuseRequest;
	}

	public void setRefuseRequest(int refuseRequest) {
		this.refuseRequest = refuseRequest;
	}

	public int getCancelRequest() {
		return cancelRequest;
	}

	public void setCancelRequest(int cancelRequest) {
		this.cancelRequest = cancelRequest;
	}

	public int getWaitPaymentApproval() {
		return waitPaymentApproval;
	}

	public void setWaitPaymentApproval(int waitPaymentApproval) {
		this.waitPaymentApproval = waitPaymentApproval;
	}

	public int getPaymentApprovals() {
		return paymentApprovals;
	}

	public void setPaymentApprovals(int paymentApprovals) {
		this.paymentApprovals = paymentApprovals;
	}

	public int getChargeback() {
		return chargeback;
	}

	public void setChargeback(int chargeback) {
		this.chargeback = chargeback;
	}

	public int getPaymentfailed() {
		return paymentfailed;
	}

	public void setPaymentfailed(int paymentfailed) {
		this.paymentfailed = paymentfailed;
	}

	public int getWaitUploadCertificate() {
		return waitUploadCertificate;
	}

	public void setWaitUploadCertificate(int waitUploadCertificate) {
		this.waitUploadCertificate = waitUploadCertificate;
	}

	public int getSucceed() {
		return succeed;
	}

	public void setSucceed(int succeed) {
		this.succeed = succeed;
	}

	public int getWaitSaveRequest() {
		return waitSaveRequest;
	}

	public void setWaitSaveRequest(int waitSaveRequest) {
		this.waitSaveRequest = waitSaveRequest;
	}

}
