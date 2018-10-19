package com.tmb.ecert.report.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.report.persistence.vo.RpCoverSheetVo;
import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormVo;
import com.tmb.ecert.report.service.ReportPdfService;

import net.sf.jasperreports.engine.JRException;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;

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
		Date currentDate = new Date();
		String pdf = null;
		try {
			pdf = reportPdfService.receiptTaxToPdf(vo);
		}catch(Exception e) {
			
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.RECEIPT_CODE,
					ACTION_AUDITLOG_DESC.RECEIPT,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		
		return pdf;
	}

	/* coverSheet */
	@PostMapping("/coverSheet")
	@ResponseBody // byte[]
	public String pdfCoverSheet(@RequestBody RpCoverSheetVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		String pdf = null;
		try {
			pdf = reportPdfService.coverSheetToPdf(vo);
		}catch(Exception e) {
			
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.COVERSHEET_CODE,
					ACTION_AUDITLOG_DESC.COVERSHEET,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		
		return pdf;
	}

	/* reqForm original */
	@PostMapping("/reqFormOriginal")
	@ResponseBody
	public String pdfreqFormOriginal(@RequestBody RpReqFormVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		String pdf = null;
		try {
			pdf = reportPdfService.reqFormOriginalToPdf(vo);
		}catch(Exception e) {
			
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.PRINT_FORM_CODE,
					ACTION_AUDITLOG_DESC.PRINT_FORM,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		
		return pdf;
	}

	/* reqForm */
	@PostMapping("/reqForm")
	@ResponseBody
	public String pdfreqForm(@RequestBody RpReqFormVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		String pdf = null;
		try {
			pdf = reportPdfService.reqFormToPdf(vo);
		}catch(Exception e) {
			
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.PRINT_FORMREQ_CODE,
					ACTION_AUDITLOG_DESC.PRINT_FORMREQ,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		
		return pdf;
	}
	
}
