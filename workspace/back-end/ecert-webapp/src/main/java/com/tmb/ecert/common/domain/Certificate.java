package com.tmb.ecert.common.domain;

public class Certificate {
	private Long certificateId;
	private String code;
	private String typeCode;
	private String typeDesc;
	private String certificate;
	private String feeDbd;
	private String feeTmb;
	
	

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getFeeDbd() {
		return feeDbd;
	}

	public void setFeeDbd(String feeDbd) {
		this.feeDbd = feeDbd;
	}

	public String getFeeTmb() {
		return feeTmb;
	}

	public void setFeeTmb(String feeTmb) {
		this.feeTmb = feeTmb;
	}
}
