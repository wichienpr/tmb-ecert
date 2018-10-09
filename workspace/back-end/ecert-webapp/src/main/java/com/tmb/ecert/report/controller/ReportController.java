package com.tmb.ecert.report.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.report.service.ReportService;

import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("api/report")
public class ReportController {
	
	private Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/pdf/{name}/file")
	@ResponseBody
	public void pdfSomething(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
		reportService.viewPdf(name, response);
	}
	
	
//	@PostMapping("/pdf/reqForm/{report}")
//	@ResponseBody // byte[]
//	public void pdfTs(@PathVariable("report") String name, @RequestBody String json) throws IOException, JRException { // byte[]
//		byte[] report = reportService.reqFormObjectToPDF(name, json); // null
//		//return report;
//	}
	
	@PostMapping("/pdf/reqForm/{report}")
	@ResponseBody // byte[]
	public void pdfReqForm(@PathVariable("report") String name, @RequestBody String json) throws IOException, JRException { // byte[]
		byte[] report = reportService.reqFormObjectToPDF(name, json); // null
		//return report;
	}
	
	
	@PostMapping("/pdf/receiptTax/{report}")
	@ResponseBody // byte[]
	public void pdfReceiptTax(@PathVariable("report") String name, @RequestBody String json) throws IOException, JRException { // byte[]
		byte[] report = reportService.receiptTaxFormObjectToPDF(name, json); // null
		//return report;
	}

	@PostMapping("/pdf/coverSheet/{report}")
	@ResponseBody // byte[]
	public void pdfCoverSheet(@PathVariable("report") String name, @RequestBody String json) throws IOException, JRException { // byte[]
		byte[] report = reportService.coverSheetObjectToPDF(name, json); // null
		//return report;
	}
}
