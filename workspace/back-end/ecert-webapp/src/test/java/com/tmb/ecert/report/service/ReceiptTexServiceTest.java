package com.tmb.ecert.report.service;





import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import org.junit.Test;

public class ReceiptTexServiceTest {


	
	@Test
	public void ReceiptTex()  {
		/*int num1= 1500;*/
		
		//DecimalFormat formatter = new DecimalFormat("#,##0");
		DecimalFormat format = new DecimalFormat("#,###.00");
		//formatter.format(num1);
		 
		//System.out.println(format.format(new BigDecimal("7.11")));

		//String tmbReqNo = "10Nov20180000";
		//String name ="RequestForm_"+tmbReqNo;
		//String name ="RequestForm_"+DateFormatUtils.format(new java.util.Date(),"yyyyMMddHHmmss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());


	
		System.out.println(format.format(new BigDecimal("7.115")));
		
		
	}
}
