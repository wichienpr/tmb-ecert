package com.tmb.ecert.report.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.report.persistence.vo.RpCoverSheetVo;
import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormVo;
import com.tmb.ecert.report.service.ReportPdfService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("api/report/pdf")
public class ReportPdfController {

	@Autowired
	private ReportPdfService reportPdfService;

	@Autowired
	private AuditLogService auditLogService;

	@GetMapping("/view/{name}/download")
	@ResponseBody
	public void viewPdfdownload(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
		reportPdfService.viewPdfToData(name, response);
	}

	/* receiptTax */
	@PostMapping("/createAndUpload/receiptTax")
	@ResponseBody
	public String pdfReceiptTax(@RequestBody RpReceiptTaxVo vo) throws IOException, JRException {
		return reportPdfService.receiptTaxToPdf(vo);
	}

	/* coverSheet */
	@PostMapping("/coverSheet")
	@ResponseBody // byte[]
	public String pdfCoverSheet(@RequestBody RpCoverSheetVo vo) throws IOException, JRException {
		return reportPdfService.coverSheetToPdf(vo);
	}

	/* reqForm original */
	@PostMapping("/reqFormOriginal")
	@ResponseBody
	public String pdfreqFormOriginal(@RequestBody RpReqFormVo vo) throws IOException, JRException {
//		return reportPdfService.reqFormOriginalToPdf(vo);
		return reportPdfService.reqFormWithoutCert(vo);
	}

	/* reqForm */
	@PostMapping("/reqForm")
	@ResponseBody
	public String pdfreqForm(@RequestBody RpReqFormVo vo) throws IOException, JRException {
//		return reportPdfService.reqFormToPdf(vo);
		return reportPdfService.reqFormToPdfOnepage(vo);
	}
	
	/* reprintreceiptTax */
	@PostMapping("/createAndUpload/reprintReceiptTax")
	@ResponseBody
	public String pdfReprintReceiptTax(@RequestBody RpReceiptTaxVo vo) throws IOException, JRException {
		return reportPdfService.reprintReceiptTax(vo);
	}

	/* cancelreceiptTax */
	@PostMapping("/createAndUpload/cancelReceiptTax")
	@ResponseBody
	public String pdfCancelReceiptTax(@RequestBody RpReceiptTaxVo vo) throws IOException, JRException {
		return reportPdfService.cancelReceiptTax(vo);
	}

}
