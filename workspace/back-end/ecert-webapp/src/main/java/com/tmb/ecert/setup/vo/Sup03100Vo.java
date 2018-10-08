package com.tmb.ecert.setup.vo;

public class Sup03100Vo {
	
	private int emailDetail_id;
	private int emailConfig_id;
	private String subject;
	private String body;
	private String from;
	private String to;
	private int attachFile_flag;
	
	public int getEmailDetail_id() {
		return emailDetail_id;
	}
	public void setEmailDetail_id(int emailDetail_id) {
		this.emailDetail_id = emailDetail_id;
	}
	public int getEmailConfig_id() {
		return emailConfig_id;
	}
	public void setEmailConfig_id(int emailConfig_id) {
		this.emailConfig_id = emailConfig_id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getAttachFile_flag() {
		return attachFile_flag;
	}
	public void setAttachFile_flag(int attachFile_flag) {
		this.attachFile_flag = attachFile_flag;
	}
	
	
	

}
