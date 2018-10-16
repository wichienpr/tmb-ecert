package com.tmb.ecert.report.service;





import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tmb.ecert.report.persistence.dao.RpDao;

public class ReceiptTexServiceTest {

	@Autowired
	private RpDao rpDao;
	
	@Test
	public void ReceiptTex()  {
		//int num1= 1500;
		
		//DecimalFormat formatter = new DecimalFormat("#,##0");
		//DecimalFormat formatTest = new DecimalFormat("#,###.##");
		//formatter.format(num1);
		 
		//System.out.println(formatter.format(num1));

		
		String name ="RequestForm"+DateFormatUtils.format(new java.util.Date(),"yyyyMMddHHmmss");
		
       System.out.println(name);
	}
}
