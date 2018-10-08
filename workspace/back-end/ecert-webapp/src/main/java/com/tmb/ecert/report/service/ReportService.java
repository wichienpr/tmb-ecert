package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
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


@Service
public class ReportService {

	@Value("${app.datasource.path.report}")
	private String PATH_EXPORT;

	private Logger logger = LoggerFactory.getLogger(ReportService.class);

	public void initialService() {
		File f = new File(PATH_EXPORT); // initial file (folder)
		if (!f.exists()) { // check folder exists
			if (f.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.error("Failed to create directory!");
			}
		}
	}

	public byte[] reqFormObjectToPDF(String name, String param) throws IOException, JRException {
		// Folder Exist ??
		initialService();

		Gson gson = new Gson();
		Map<String, Object> params = new HashMap<String, Object>();
		params = (Map<String, Object>) gson.fromJson(param, params.getClass());

		List<Object> bean = new ArrayList<Object>();

		if (StringUtils.isNotBlank((CharSequence) params.get("Bean"))) {
			bean = (List<Object>) params.get("Bean");
		}

		JRDataSource dataSource = null;

		if (StringUtils.isNotBlank((CharSequence) params.get("logoDbd"))) {
			String logoDbd = params.get("logoDbd").toString();
			params.remove("logoDbd");
			params.put("logoDbd", ReportUtils.getResourceFile(PATH.IMAGE_PATH, logoDbd));
		}

		if (StringUtils.isNotBlank((CharSequence) params.get("logoTmb"))) {
			String logoTmb = params.get("logoTmb").toString();
			params.remove("logoTmb");
			params.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, logoTmb));
		}

		if (StringUtils.isNotBlank((CharSequence) bean)) {
			dataSource = new JREmptyDataSource();
		} else {
			dataSource = new JRBeanCollectionDataSource(bean);
		}

		JasperPrint jasperPrint01 = ReportUtils.exportReport(name, params, dataSource); // JRBeanCollectionDataSource(exampleList)
		String reportName02 = "RP001_REQ_FORM_02";
		JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params, dataSource);

		List<ExporterInputItem> itemList = new ArrayList<>();
		itemList.add(new SimpleExporterInputItem(jasperPrint01));
		itemList.add(new SimpleExporterInputItem(jasperPrint02));

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(itemList));

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		exporter.exportReport();

		byte[] reportFile = os.toByteArray();

		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_EXPORT + name + ".pdf"))); 
		ReportUtils.closeResourceFileInputStream(params);

		return reportFile;
	}

	
	
	
	public void viewPdf(String name, HttpServletResponse response) throws Exception {
		File file = new File(PATH_EXPORT + name + ".pdf");
		byte[] reportFile = IOUtils.toByteArray(new FileInputStream(file)); // null

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline;filename=" + name + ".pdf");
		response.setContentLength(reportFile.length);

		OutputStream responseOutputStream = response.getOutputStream();
		for (byte bytes : reportFile) {
			responseOutputStream.write(bytes);
		}
	}

}
