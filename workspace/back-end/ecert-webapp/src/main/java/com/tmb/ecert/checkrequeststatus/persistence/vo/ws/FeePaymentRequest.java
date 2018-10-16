package com.tmb.ecert.checkrequeststatus.persistence.vo.ws;

import java.io.Serializable;
import java.math.BigDecimal;

public class FeePaymentRequest implements Serializable {

	private static final long serialVersionUID = -8344841511366279211L;

	private String rqUid;

	private String clientDt;
	private String custLangPref; // option
	private String spName;

	private String clientAppOrg;
	private String clientAppName;
	private String clientAppVersion;

	private String tranCode;

	private String fromAccountIdent; // FromAcctRef. AcctKeys. AcctIdent. AcctIdentValue
	private String fromAccountType; // FromAcctRef. AcctKeys. AcctIdent. AcctIdentValue

	private String toAccountIdent; // ToAcctRef.AcctKeys.AcctIdent.AcctIdentValue;
	private String toAccountType; // ToAcctRef.AcctKeys.AcctType.AcctIdentValue

	private BigDecimal transferAmount; // CurAmt.Amt
	private BigDecimal fee; // BillPmtFee

	private String ref1; // PmtRefIdent
	private String ref2; // InvoiceNum

	private String epayCode;
	private String branchIdent;
	private String compCode;

	private String postedDate; // PostedDt
	private String postedTime; //HHmmss

	public String getRqUid() {
		return rqUid;
	}

	public void setRqUid(String rqUid) {
		this.rqUid = rqUid;
	}

	public String getClientDt() {
		return clientDt;
	}

	public void setClientDt(String clientDt) {
		this.clientDt = clientDt;
	}

	public String getCustLangPref() {
		return custLangPref;
	}

	public void setCustLangPref(String custLangPref) {
		this.custLangPref = custLangPref;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getClientAppOrg() {
		return clientAppOrg;
	}

	public void setClientAppOrg(String clientAppOrg) {
		this.clientAppOrg = clientAppOrg;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public String getClientAppVersion() {
		return clientAppVersion;
	}

	public void setClientAppVersion(String clientAppVersion) {
		this.clientAppVersion = clientAppVersion;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getFromAccountIdent() {
		return fromAccountIdent;
	}

	public void setFromAccountIdent(String fromAccountIdent) {
		this.fromAccountIdent = fromAccountIdent;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getToAccountIdent() {
		return toAccountIdent;
	}

	public void setToAccountIdent(String toAccountIdent) {
		this.toAccountIdent = toAccountIdent;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getRef1() {
		return ref1;
	}

	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}

	public String getRef2() {
		return ref2;
	}

	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}

	public String getEpayCode() {
		return epayCode;
	}

	public void setEpayCode(String epayCode) {
		this.epayCode = epayCode;
	}

	public String getBranchIdent() {
		return branchIdent;
	}

	public void setBranchIdent(String branchIdent) {
		this.branchIdent = branchIdent;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(String postedTime) {
		this.postedTime = postedTime;
	}

}
