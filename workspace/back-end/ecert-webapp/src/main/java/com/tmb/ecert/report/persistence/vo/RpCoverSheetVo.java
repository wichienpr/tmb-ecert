package com.tmb.ecert.report.persistence.vo;

public class RpCoverSheetVo {
	private Long id;
	private String tmpReqNo;
	private String certificateCode;
	private String certificate;
	private String other;
	private int totalNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTmpReqNo() {
		return tmpReqNo;
	}

	public void setTmpReqNo(String tmpReqNo) {
		this.tmpReqNo = tmpReqNo;
	}

	public String getCertificateCode() {
		return certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

}
