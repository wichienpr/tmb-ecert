package com.tmb.ecert.report.persistence.vo;

import java.util.Date;

public class RpReqFormListVo {
	private String seq;
	private int totalNum;
	private int totalNumCc;
	private int numSetCc;
	private int numEditCc;
	private int numOtherCc;
	private Date dateOtherReg;
	private Date dateEditReg;
	private String statementYear;
	private Date dateAccepted;

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

	public Date getDateOtherReg() {
		return dateOtherReg;
	}

	public void setDateOtherReg(Date dateOtherReg) {
		this.dateOtherReg = dateOtherReg;
	}

	public Date getDateEditReg() {
		return dateEditReg;
	}

	public void setDateEditReg(Date dateEditReg) {
		this.dateEditReg = dateEditReg;
	}

	public String getStatementYear() {
		return statementYear;
	}

	public void setStatementYear(String statementYear) {
		this.statementYear = statementYear;
	}

	public Date getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(Date dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

}
