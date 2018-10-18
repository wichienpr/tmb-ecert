package com.tmb.ecert.batchjob.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class DBDSummaryTransactionFile {

	private Header header = new Header();
	private List<Detail> detail = new ArrayList<>();
	private Total total = new Total();

	public class Header {
		public String recordType;
		public String sequenceNumber;
		public String bankCode;
		public String companyAccountNumber;
		public String companyName;
		public String effectiveDate;
		public String serviceCode;
		public String spare;

	}

	public class Detail {
		public String recordType;
		public String sequenceNumber;
		public String bankCode;
		public String companyAccountNumber;
		public String paymentDate;
		public String paymentTime;
		public String customerName;
		public String reference1;
		public String reference2;
		public String reference3;
		public String branchCode;
		public String accountOwnerBranchCode;
		public String kindofTransaction;
		public String transactionCode;
		public String chequeNumber;
		public String amount;
		public String chequeBankCode;
		public String transactionDate;
		public String processDate;
		public String processTime;
		public String spare;
	}

	public class Total {
		public String recordType;
		public String sequenceNumber;
		public String bankCode;
		public String companyAccountNumber;
		public String totalAmont;
		public String totalTransaction;
		public String spare;
	}

	public StringBuilder tranformHeadfer(){
		StringBuilder str = new StringBuilder();
		str.append(this.header.recordType); //1
		str.append(StringUtils.leftPad(this.header.sequenceNumber,6,"0"));
		str.append(StringUtils.leftPad(this.header.bankCode,3));
		str.append(StringUtils.leftPad(this.header.companyAccountNumber,10));
		str.append(StringUtils.leftPad(this.header.companyName,40));
		str.append(StringUtils.leftPad(this.header.effectiveDate,8));
		str.append(StringUtils.leftPad(this.header.serviceCode,8));
		str.append(StringUtils.leftPad(this.header.spare,180));
		return str;
	}
	
	public List<StringBuilder> tranformDetail(){
		List<StringBuilder> list = new ArrayList<>();
		for (Detail d : detail) {
			StringBuilder str = new StringBuilder();
			str.append(d.recordType); //1
			str.append(StringUtils.leftPad(d.sequenceNumber,6,"0"));
			str.append(StringUtils.leftPad(d.bankCode,3));
			str.append(StringUtils.leftPad(d.companyAccountNumber,10));
			str.append(StringUtils.leftPad(d.paymentDate,8));
			str.append(StringUtils.leftPad(d.paymentTime,6));
			str.append(StringUtils.leftPad(d.customerName,50));
			str.append(StringUtils.leftPad(d.reference1,20));
			str.append(StringUtils.leftPad(d.reference2,20));
			str.append(StringUtils.leftPad(d.reference3,20));
			str.append(StringUtils.leftPad(d.branchCode,4));
			str.append(StringUtils.leftPad(d.accountOwnerBranchCode,4));
			str.append(StringUtils.leftPad(d.kindofTransaction,1));
			str.append(StringUtils.leftPad(d.transactionCode,3));
			str.append(StringUtils.leftPad(d.chequeNumber,7));
			str.append(StringUtils.leftPad(d.amount,13));
			str.append(StringUtils.leftPad(d.chequeBankCode,3));
			str.append(StringUtils.leftPad(d.transactionDate,8));
			str.append(StringUtils.leftPad(d.processDate,8));
			str.append(StringUtils.leftPad(d.processTime,6));
			str.append(StringUtils.leftPad(d.spare,55));
			list.add(str);
		}
		
		return list;
	}
	
	public StringBuilder tranformTotal(){
		StringBuilder str = new StringBuilder();
		str.append(this.total.recordType); //1
		str.append(StringUtils.leftPad(this.total.sequenceNumber,6,"0"));
		str.append(StringUtils.leftPad(this.total.bankCode,3));
		str.append(StringUtils.leftPad(this.total.companyAccountNumber,10));
		str.append(StringUtils.leftPad(this.total.totalAmont,13));
		str.append(StringUtils.leftPad(this.total.totalTransaction,6));
		str.append(StringUtils.leftPad(this.total.spare,217));
		return str;
	}
	
	public Detail createDetail(){
		return new Detail();
	}
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<Detail> getDetail() {
		return detail;
	}

	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}

	public Total getTotal() {
		return total;
	}

	public void setTotal(Total total) {
		this.total = total;
	}

}
