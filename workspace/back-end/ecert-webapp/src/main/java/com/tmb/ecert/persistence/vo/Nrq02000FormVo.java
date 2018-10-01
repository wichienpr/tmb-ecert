package com.tmb.ecert.persistence.vo;

import org.springframework.web.multipart.MultipartFile;

public class Nrq02000FormVo {
	private String reqTypeSelect;
	private String customSegSelect;
	private String payMethodSelect;
	private String subAccMethodSelect;
	private String accNo;
	private String accName;
	private String corpNo;
	private String corpName;
	private String corpName1;
	private String acceptNo;
	private String departmentName;
	private Boolean tmbReceiptChk;
	private String telReq;
	private String address;
	private String note;
	private MultipartFile requestFile;
	private MultipartFile copyFile;
	private MultipartFile changeNameFile;
	public String getReqTypeSelect() {
		return reqTypeSelect;
	}
	public void setReqTypeSelect(String reqTypeSelect) {
		this.reqTypeSelect = reqTypeSelect;
	}
	public String getCustomSegSelect() {
		return customSegSelect;
	}
	public void setCustomSegSelect(String customSegSelect) {
		this.customSegSelect = customSegSelect;
	}
	public String getPayMethodSelect() {
		return payMethodSelect;
	}
	public void setPayMethodSelect(String payMethodSelect) {
		this.payMethodSelect = payMethodSelect;
	}
	public String getSubAccMethodSelect() {
		return subAccMethodSelect;
	}
	public void setSubAccMethodSelect(String subAccMethodSelect) {
		this.subAccMethodSelect = subAccMethodSelect;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCorpNo() {
		return corpNo;
	}
	public void setCorpNo(String corpNo) {
		this.corpNo = corpNo;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpName1() {
		return corpName1;
	}
	public void setCorpName1(String corpName1) {
		this.corpName1 = corpName1;
	}
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Boolean getTmbReceiptChk() {
		return tmbReceiptChk;
	}
	public void setTmbReceiptChk(Boolean tmbReceiptChk) {
		this.tmbReceiptChk = tmbReceiptChk;
	}
	public String getTelReq() {
		return telReq;
	}
	public void setTelReq(String telReq) {
		this.telReq = telReq;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public MultipartFile getRequestFile() {
		return requestFile;
	}
	public void setRequestFile(MultipartFile requestFile) {
		this.requestFile = requestFile;
	}
	public MultipartFile getCopyFile() {
		return copyFile;
	}
	public void setCopyFile(MultipartFile copyFile) {
		this.copyFile = copyFile;
	}
	public MultipartFile getChangeNameFile() {
		return changeNameFile;
	}
	public void setChangeNameFile(MultipartFile changeNameFile) {
		this.changeNameFile = changeNameFile;
	}
	
}
