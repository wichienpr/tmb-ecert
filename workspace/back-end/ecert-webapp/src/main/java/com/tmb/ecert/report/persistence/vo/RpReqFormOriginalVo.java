package com.tmb.ecert.report.persistence.vo;

public class RpReqFormOriginalVo {

	private Long id;
	private String typeCertificate;
	private String seq;
	private String tmpReqNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeCertificate() {
		return typeCertificate;
	}

	public void setTypeCertificate(String typeCertificate) {
		this.typeCertificate = typeCertificate;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getTmpReqNo() {
		return tmpReqNo;
	}

	public void setTmpReqNo(String tmpReqNo) {
		this.tmpReqNo = tmpReqNo;
	}

}
