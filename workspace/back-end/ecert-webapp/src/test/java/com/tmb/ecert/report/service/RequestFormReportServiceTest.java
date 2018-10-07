package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tmb.ecert.Application;
import com.tmb.ecert.report.domain.ExampleBean;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import th.co.baiwa.buckwaframework.common.constant.ReportConstants.PATH;
import th.co.baiwa.buckwaframework.common.util.ReportUtils;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {Application.class})
public class RequestFormReportServiceTest {
	
	@Test
	public void test() throws Exception {
		System.out.println("Hello");
		
		// RP001 - 01
		String reportName01 = "RP001_REQ_FORM_01";
		
		Map<String, Object> params01 = new HashMap<>();
		params01.put("sendTo", " ข้อความทดสอบ");
		params01.put("logoDbd", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoDbd.png"));
		
		List<ExampleBean> exampleBeanList = new ArrayList<>();
		ExampleBean exampleBean = null;
		for (int i = 0; i < 5; i++) {
			exampleBean = new ExampleBean();
			exampleBean.setSeq(String.valueOf(i + 1));
			if (i < 3) {
				exampleBean.setOrgName("บริษัท นิติบุคคล");
			}
			exampleBeanList.add(exampleBean);
		}
		
		JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JRBeanCollectionDataSource(exampleBeanList));
//		JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JREmptyDataSource());
		
		
		// RP001 - 02
		String reportName02 = "RP001_REQ_FORM_02";
		
		Map<String, Object> params02 = new HashMap<>();
		params02.put("sendTo", " ข้อความทดสอบ");
		params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
		
		JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());
		
		
		List<ExporterInputItem> itemList = new ArrayList<>();
		itemList.add(new SimpleExporterInputItem(jasperPrint01));
		itemList.add(new SimpleExporterInputItem(jasperPrint02));
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(itemList));
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		exporter.exportReport();
		
//		byte[] reportFile = JasperExportManager.exportReportToPdf(jasperPrint);
		byte[] reportFile = os.toByteArray();
		IOUtils.write(reportFile, new FileOutputStream(new File("/tmp/tmb-ecert/report/" + "RequestForm.pdf")));
		
		ReportUtils.closeResourceFileInputStream(params02);
	}
	
}
