package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class ECMUuploadRequest {
	private String repositoryId;
	private String objectTypeId;
	private String name;
	private String touchReferenceNumber;
	private String docType;
	private String documentCode;
	private String tmbSource;
	private String tmbCreatorId;
	private String tmbDocTypeCode;
	private String tmbIdentificationId;
	private String tmbIdentificationType;
	private int archival;
	private int disposal;
	private String channel;
	private String docLocation;
	private String applicationId;
	private String customerFirstNameEng;
	private String customerFirstNameThai;

	private byte[] file;
//	private MultipartFile file;
//	private File file;
	

	public String getRepositoryId() {
		return repositoryId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCustomerFirstNameEng() {
		return customerFirstNameEng;
	}

	public void setCustomerFirstNameEng(String customerFirstNameEng) {
		this.customerFirstNameEng = customerFirstNameEng;
	}

	public String getCustomerFirstNameThai() {
		return customerFirstNameThai;
	}

	public void setCustomerFirstNameThai(String customerFirstNameThai) {
		this.customerFirstNameThai = customerFirstNameThai;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getObjectTypeId() {
		return objectTypeId;
	}

	public void setObjectTypeId(String objectTypeId) {
		this.objectTypeId = objectTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTouchReferenceNumber() {
		return touchReferenceNumber;
	}

	public void setTouchReferenceNumber(String touchReferenceNumber) {
		this.touchReferenceNumber = touchReferenceNumber;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getTmbSource() {
		return tmbSource;
	}

	public void setTmbSource(String tmbSource) {
		this.tmbSource = tmbSource;
	}

	public String getTmbCreatorId() {
		return tmbCreatorId;
	}

	public void setTmbCreatorId(String tmbCreatorId) {
		this.tmbCreatorId = tmbCreatorId;
	}

	public String getTmbDocTypeCode() {
		return tmbDocTypeCode;
	}

	public void setTmbDocTypeCode(String tmbDocTypeCode) {
		this.tmbDocTypeCode = tmbDocTypeCode;
	}

	public String getTmbIdentificationId() {
		return tmbIdentificationId;
	}

	public void setTmbIdentificationId(String tmbIdentificationId) {
		this.tmbIdentificationId = tmbIdentificationId;
	}

	public String getTmbIdentificationType() {
		return tmbIdentificationType;
	}

	public void setTmbIdentificationType(String tmbIdentificationType) {
		this.tmbIdentificationType = tmbIdentificationType;
	}

	public int getArchival() {
		return archival;
	}

	public void setArchival(int archival) {
		this.archival = archival;
	}

	public int getDisposal() {
		return disposal;
	}

	public void setDisposal(int disposal) {
		this.disposal = disposal;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDocLocation() {
		return docLocation;
	}

	public void setDocLocation(String docLocation) {
		this.docLocation = docLocation;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

//	public File getFile() {
//		return file;
//	}
//
//	public void setFile(File file) {
//		this.file = file;
//	}

//	public MultipartFile getFile() {
//		return file;
//	}
//
//	public void setFile(MultipartFile file) {
//		this.file = file;
//	}
	
	


	
	

}
