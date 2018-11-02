package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.util.List;

public class CheckStatusDocumentResponse {

	private String description;
	private List<IndexGuoupResponse> indexGroups;
	private String statusCode;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<IndexGuoupResponse> getIndexGroups() {
		return indexGroups;
	}
	public void setIndexGroups(List<IndexGuoupResponse> indexGroups) {
		this.indexGroups = indexGroups;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	



}
