package com.tmb.ecert.report.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.PATH;
import th.co.baiwa.buckwaframework.common.util.ReportUtils;

public class ReceiptTexServiceTest {

	@Test
	public void ReceiptTex() throws Exception {
		System.out.println("ทดสอบรายงาน  ReceiptTex");

		String reportName = "RP_RECEIPT_TAX";

		Map<String, Object> params = new HashMap<>();
		params.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
		params.put("sendTo", " ข้อความทดสอบ");
		
		
		JasperPrint jasperPrint = ReportUtils.exportReport(reportName, params, new JREmptyDataSource());


	}
}
