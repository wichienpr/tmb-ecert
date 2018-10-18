package com.tmb.ecert.checkrequeststatus.persistence.vo;

import org.springframework.web.multipart.MultipartFile;

public class CertificateVo {

	private Long id;
	private String status;
	private String certificates;
	private MultipartFile certificatesFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCertificates() {
		return certificates;
	}

	public void setCertificates(String certificates) {
		this.certificates = certificates;
	}

	public MultipartFile getCertificatesFile() {
		return certificatesFile;
	}

	public void setCertificatesFile(MultipartFile certificatesFile) {
		this.certificatesFile = certificatesFile;
	}

}
