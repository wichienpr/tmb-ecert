package com.tmb.ecert.persistence.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rep02000Vo {

	public String custsegmentDesc;
	public String custsegmentCount;

	public BigDecimal amountDbd;
	public BigDecimal amountTmb;
	public BigDecimal totalAmount;

	public String getCustsegmentDesc() {
		return custsegmentDesc;
	}

	public void setCustsegmentDesc(String custsegmentDesc) {
		this.custsegmentDesc = custsegmentDesc;
	}

	public String getCustsegmentCount() {
		return custsegmentCount;
	}

	public void setCustsegmentCount(String custsegmentCount) {
		this.custsegmentCount = custsegmentCount;
	}

	public BigDecimal getAmountDbd() {
		return amountDbd;
	}

	public void setAmountDbd(BigDecimal amountDbd) {
		this.amountDbd = amountDbd;
	}

	public BigDecimal getAmountTmb() {
		return amountTmb;
	}

	public void setAmountTmb(BigDecimal amountTmb) {
		this.amountTmb = amountTmb;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
