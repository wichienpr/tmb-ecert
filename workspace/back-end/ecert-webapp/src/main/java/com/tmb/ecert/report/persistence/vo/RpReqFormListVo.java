package com.tmb.ecert.report.persistence.vo;

public class RpReqFormListVo {
	private String seq;
	private Boolean box1;
	private Boolean box2;
	private Boolean box3;
	private Boolean box4;
	private int totalNum;
	private int totalNumCc;
	private int numSetCc;
	private int numEditCc;
	private int numOtherCc;
	private String other;
	private String statementYear;
	private String dateEditReg;
	private String dateOtherReg;
	private String dateAccepted;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public Boolean getBox1() {
		return box1;
	}

	public void setBox1(Boolean box1) {
		this.box1 = box1;
	}

	public Boolean getBox2() {
		return box2;
	}

	public void setBox2(Boolean box2) {
		this.box2 = box2;
	}

	public Boolean getBox3() {
		return box3;
	}

	public void setBox3(Boolean box3) {
		this.box3 = box3;
	}

	public Boolean getBox4() {
		return box4;
	}

	public void setBox4(Boolean box4) {
		this.box4 = box4;
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

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getStatementYear() {
		return statementYear;
	}

	public void setStatementYear(String statementYear) {
		this.statementYear = statementYear;
	}

	public String getDateEditReg() {
		return dateEditReg;
	}

	public void setDateEditReg(String dateEditReg) {
		this.dateEditReg = dateEditReg;
	}

	public String getDateOtherReg() {
		return dateOtherReg;
	}

	public void setDateOtherReg(String dateOtherReg) {
		this.dateOtherReg = dateOtherReg;
	}

	public String getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(String dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

}
