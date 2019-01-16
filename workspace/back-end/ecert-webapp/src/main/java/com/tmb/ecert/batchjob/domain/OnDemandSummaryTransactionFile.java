package com.tmb.ecert.batchjob.domain;

import org.apache.commons.lang3.StringUtils;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.ONDEMAND;


public class OnDemandSummaryTransactionFile {
	
	private Page page = new Page();
	
	public class Page{
		public StringBuilder details = new StringBuilder();

		public StringBuilder getDetails() {
			return details;
		}

		public void setDetails(StringBuilder details) {
			this.details = details;
		}
		
		
	}

	public String tranformHeader(HeaderOndemand header,int line){
		StringBuilder headerLine = new StringBuilder();
		switch(line) {
			case 1: 
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getControlPage(),StringUtils.EMPTY),1));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getBranchCode(),StringUtils.EMPTY),46));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getBankName(),StringUtils.EMPTY),60));
				headerLine.append(StringUtils.rightPad(ONDEMAND.PAGE,10));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getPageNo(),StringUtils.EMPTY),10));
				break;
			case 2: 
				headerLine.append(StringUtils.rightPad(StringUtils.EMPTY,1));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getBankName(),StringUtils.EMPTY),46));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getSystemName(),StringUtils.EMPTY),60));
				headerLine.append(StringUtils.rightPad(ONDEMAND.AS_DATE,10));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getAsDate(),StringUtils.EMPTY),10));
				break;
			case 3: 
				headerLine.append(StringUtils.rightPad(StringUtils.EMPTY,1));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getReportId(),StringUtils.EMPTY),43));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getReportName(),StringUtils.EMPTY),63));
				headerLine.append(StringUtils.rightPad(ONDEMAND.RUN_DATE,10));
				headerLine.append(StringUtils.rightPad(StringUtils.defaultString(header.getRunDate(),StringUtils.EMPTY),10));
				break;
			case 4: 
				headerLine.append(System.lineSeparator());
				break;
			case 5: 
				headerLine.append(StringUtils.rightPad(StringUtils.EMPTY,1));
				headerLine.append(StringUtils.rightPad(ONDEMAND.REQ_DATE,15));
				headerLine.append(StringUtils.rightPad(ONDEMAND.TMB_REQ_NO,17));
				headerLine.append(StringUtils.rightPad(ONDEMAND.ORG_ID,15));
				headerLine.append(StringUtils.rightPad(ONDEMAND.SEGMENT,10));
//				headerLine.append(StringUtils.rightPad(ONDEMAND.PAID_TYPE,41));
				headerLine.append(StringUtils.rightPad(ONDEMAND.ACCT_NO,10));
				headerLine.append(StringUtils.leftPad(ONDEMAND.AMOUNT,22));
				headerLine.append(StringUtils.rightPad("",5," ")); // add new 14 jan 2019
				headerLine.append(StringUtils.rightPad(ONDEMAND.PAID_TYPE,41));
				break;
		}
		return (headerLine!=null&& headerLine.length()>0 ? headerLine.toString(): StringUtils.EMPTY);
	}

	public String tranformDetail(DetailOndemand detail){
		StringBuilder obj= new StringBuilder();
		if(detail!=null) {
			obj.append(StringUtils.rightPad(StringUtils.EMPTY,1));
			obj.append(StringUtils.rightPad(detail.getRequestDate(),15));
			obj.append(StringUtils.rightPad(detail.getTmeReqNo(),17));
			obj.append(StringUtils.rightPad(detail.getOrgId(),15));
			obj.append(StringUtils.rightPad("",2," ")); // add new 14 jan 2019
			obj.append(StringUtils.rightPad(detail.getSegment(),8));
//			obj.append(StringUtils.rightPad(detail.getPaidType(),41));
			obj.append(StringUtils.rightPad(detail.getAccountNo(),10));
			obj.append(StringUtils.leftPad(detail.getAmount(),22));
			obj.append(StringUtils.rightPad("",5," ")); // add new 14 jan 2019
			obj.append(StringUtils.rightPad(detail.getPaidType(),41));
		}
		return (obj!=null&& obj.length()>0 ? obj.toString(): StringUtils.EMPTY);
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
