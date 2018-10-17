package com.tmb.ecert.report.persistence.vo;

public class RpReqFormListVo {
	private String seq;
	private int totalNum;
	private int totalNumCc;
	private int totalNumFinance;
	private int totalNumShareholder;
	private int numSetCc;
	private int numEditCc;
	private int numOtherCc;
	private String other;
	private String dateOtherReg;
	private String dateEditReg;
	private String statementYear;
	private String dateAccepted;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalNumCc() {
		return totalNumCc;
	}

	public void setTotalNumCc(int totalNumCc) {
		this.totalNumCc = totalNumCc;
	}

	public int getTotalNumFinance() {
		return totalNumFinance;
	}

	public void setTotalNumFinance(int totalNumFinance) {
		this.totalNumFinance = totalNumFinance;
	}

	public int getTotalNumShareholder() {
		return totalNumShareholder;
	}

	public void setTotalNumShareholder(int totalNumShareholder) {
		this.totalNumShareholder = totalNumShareholder;
	}

	public int getNumSetCc() {
		return numSetCc;
	}

	public void setNumSetCc(int numSetCc) {
		this.numSetCc = numSetCc;
	}

	public int getNumEditCc() {
		return numEditCc;
	}

	public void setNumEditCc(int numEditCc) {
		this.numEditCc = numEditCc;
	}

	public int getNumOtherCc() {
		return numOtherCc;
	}

	public void setNumOtherCc(int numOtherCc) {
		this.numOtherCc = numOtherCc;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getDateOtherReg() {
		return dateOtherReg;
	}

	public void setDateOtherReg(String dateOtherReg) {
		this.dateOtherReg = dateOtherReg;
	}

	public String getDateEditReg() {
		return dateEditReg;
	}

	public void setDateEditReg(String dateEditReg) {
		this.dateEditReg = dateEditReg;
	}

	public String getStatementYear() {
		return statementYear;
	}

	public void setStatementYear(String statementYear) {
		this.statementYear = statementYear;
	}

	public String getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(String dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

}
