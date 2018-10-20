package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.util.List;

public class ImportDocumentRequest {
	
	private String caNumber;
	private String channelId;
	private List<FileImportRequest> files;
	private String reqId;
	private String reqUserId;
	private String segmentCode;
	
	public String getCaNumber() {
		return caNumber;
	}
	public void setCaNumber(String caNumber) {
		this.caNumber = caNumber;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public List<FileImportRequest> getFiles() {
		return files;
	}
	public void setFiles(List<FileImportRequest> files) {
		this.files = files;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getReqUserId() {
		return reqUserId;
	}
	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
	public String getSegmentCode() {
		return segmentCode;
	}
	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}
	
	
	

}
