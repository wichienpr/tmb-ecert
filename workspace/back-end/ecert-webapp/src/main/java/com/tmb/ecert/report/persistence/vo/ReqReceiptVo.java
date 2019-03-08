package com.tmb.ecert.report.persistence.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReqReceiptVo {
	private Long receipt_id;
	private Long reqform_id;
	private String tmb_requestno;
	private String receipt_no;
	private Timestamp receipt_date;
	private String file_name;
	private String customer_name;
	private String organize_id;
	private String address;
	private String major_no;
	private BigDecimal amount;
	private BigDecimal amount_tmb;
	private BigDecimal amount_dbd;
	private BigDecimal amount_vat_tmb;
	private Integer print_count;
	private String reason;
	private String receipt_no_reference;
	private Integer cancel_flag;
	private Integer delete_flag;
	
	private String createdById;
	private String createdByName;
	private Timestamp createdDateTime;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDateTime;
	
	
	private String receiptStatus;
	private String statusName;
	private Integer ecm_flag;
	
	
	public Long getReceipt_id() {
		return receipt_id;
	}
	public void setReceipt_id(Long receipt_id) {
		this.receipt_id = receipt_id;
	}
	public Long getReqform_id() {
		return reqform_id;
	}
	public String getTmb_requestno() {
		return tmb_requestno;
	}
	public String getReceipt_no() {
		return receipt_no;
	}
	public Timestamp getReceipt_date() {
		return receipt_date;
	}
	public String getFile_name() {
		return file_name;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public String getOrganize_id() {
		return organize_id;
	}
	public String getAddress() {
		return address;
	}
	public String getMajor_no() {
		return major_no;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public BigDecimal getAmount_tmb() {
		return amount_tmb;
	}
	public BigDecimal getAmount_dbd() {
		return amount_dbd;
	}
	public BigDecimal getAmount_vat_tmb() {
		return amount_vat_tmb;
	}
	public Integer getPrint_count() {
		return print_count;
	}
	public String getReason() {
		return reason;
	}
	public String getReceipt_no_reference() {
		return receipt_no_reference;
	}
	public Integer getCancel_flag() {
		return cancel_flag;
	}
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setReqform_id(Long reqform_id) {
		this.reqform_id = reqform_id;
	}
	public void setTmb_requestno(String tmb_requestno) {
		this.tmb_requestno = tmb_requestno;
	}
	public void setReceipt_no(String receipt_no) {
		this.receipt_no = receipt_no;
	}
	public void setReceipt_date(Timestamp receipt_date) {
		this.receipt_date = receipt_date;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public void setOrganize_id(String organize_id) {
		this.organize_id = organize_id;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setMajor_no(String major_no) {
		this.major_no = major_no;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setAmount_tmb(BigDecimal amount_tmb) {
		this.amount_tmb = amount_tmb;
	}
	public void setAmount_dbd(BigDecimal amount_dbd) {
		this.amount_dbd = amount_dbd;
	}
	public void setAmount_vat_tmb(BigDecimal amount_vat_tmb) {
		this.amount_vat_tmb = amount_vat_tmb;
	}
	public void setPrint_count(Integer print_count) {
		this.print_count = print_count;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setReceipt_no_reference(String receipt_no_reference) {
		this.receipt_no_reference = receipt_no_reference;
	}
	public void setCancel_flag(Integer cancel_flag) {
		this.cancel_flag = cancel_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getCreatedById() {
		return createdById;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public String getUpdatedById() {
		return updatedById;
	}
	public String getUpdatedByName() {
		return updatedByName;
	}
	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public Integer getEcm_flag() {
		return ecm_flag;
	}
	public void setEcm_flag(Integer ecm_flag) {
		this.ecm_flag = ecm_flag;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
	
	

}
