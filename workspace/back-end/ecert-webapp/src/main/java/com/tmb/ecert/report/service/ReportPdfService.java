package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.common.utils.ThaiBaht;
import com.tmb.ecert.report.persistence.dao.ReportPdfDao;
import com.tmb.ecert.report.persistence.vo.ReqReceiptVo;
import com.tmb.ecert.report.persistence.vo.RpCertificateVo;
import com.tmb.ecert.report.persistence.vo.RpCoverSheetVo;
import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;
import com.tmb.ecert.report.persistence.vo.RpReceiverVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormListVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormVo;
import com.tmb.ecert.report.persistence.vo.RpVatVo;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.service.ReceiptGenKeyService;

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
import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;
import th.co.baiwa.buckwaframework.common.util.ReportUtils;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class ReportPdfService {

	@Value("${app.datasource.path.report}")
	private String PATH_REPORT;

	@Autowired
	private UploadService upload;

	private static String SUB_PATH_UPLOAD = "";

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;

	@Autowired
	private RequestorDao upDateReqDetailDao;
	
	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private ReportPdfDao reportPdfDao;
	
	@Autowired
	private ReceiptGenKeyService receiptGenKeyService;
	
	@Autowired
	private EmailService emailService;

	private Logger logger = LoggerFactory.getLogger(ReportPdfService.class);

	// create folder for PATH_REPORT
	public void initialService() {
		File f = new File(PATH_REPORT); // initial file (folder)
		if (!f.exists()) { // check folder exists
			if (f.mkdirs()) {
				logger.info("Directory is created!");
			} else {
				logger.error("Failed to create directory!");
			}
		}
	}

	/* receiptTaxToPdf */
	public String receiptTaxToPdf(RpReceiptTaxVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		RequestForm req = null;
		ByteArrayOutputStream os =null;
		try {
			// Folder Exist ??
			initialService();
			DecimalFormat formatNumber = new DecimalFormat("#,##0.00");
			Double oneHundred = new Double(100);
			Double  feeAmount = new Double(0);
			Double vatAmount = new Double(0);
			
			req = checkReqDetailDao.findReqFormById(vo.getId(), false);
			RpVatVo vat = reportPdfDao.vat().get(0);
			if (StringUtils.isEmpty(req.getReceiptNo())) {
				String receiptNo = receiptGenKeyService.getNextKey();
				req.setReceiptNo(receiptNo);
			}
			
			// RP001
			String reportName01 = "RP_RECEIPT_TAX";
			Map<String, Object> params01 = new HashMap<>();
			params01.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params01.put("docType", "ต้นฉบับ");
			params01.put("receiptNo", req.getReceiptNo());
			params01.put("date", DateFormatUtils.format(req.getPaymentDate(), "dd MMMM yyyy", new Locale("th", "TH")));
			params01.put("time", DateFormatUtils.format(req.getPaymentDate(), "HH.mm", new Locale("th", "TH")));
			params01.put("customerNameReceipt", req.getCustomerNameReceipt());
			params01.put("organizeId", req.getOrganizeId());
			params01.put("address", req.getAddress());
			params01.put("pageActive", "1");
			params01.put("pageTotal", "2");
			params01.put("majorNo", req.getMajorNo());
//			params01.put("reprintHeader", "TEST REPRINT HEADER");
//			params01.put("reasonHeader", "สาเหตุการพิมพ์ใบแทน");
//			params01.put("reason", "testreson 00000000000000000000000000000000000000000000000");
			if (BeanUtils.isNotEmpty(req.getAmountTmb())) {
				//vat
				params01.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params01.put("amountTmb", formatNumber.format(req.getAmountTmb()));
				
				vatAmount = (req.getAmountTmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params01.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmountTmb().doubleValue() - vatAmount;
				params01.put("feeAmount",formatNumber.format(feeAmount));
				
				/*thaiBath*/
				params01.put("thaiBath", new ThaiBaht().getText(req.getAmountTmb()));
			} else {
				params01.put("vat", "0.00");
				params01.put("amountTmb", "0.00");
				params01.put("feeAmount", "0.00");
				params01.put("vatAmount", "0.00");
				params01.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params01.put("tmbRequestNo", req.getTmbRequestNo());

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JREmptyDataSource());

			// RP002
			String reportName02 = "RP_RECEIPT_TAX";
			Map<String, Object> params02 = new HashMap<>();
			params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params02.put("docType", "สำเนา");
			params02.put("receiptNo", req.getReceiptNo());
			params02.put("date", DateFormatUtils.format(req.getPaymentDate(), "dd MMMM yyyy", new Locale("th", "TH")));
			params02.put("time", DateFormatUtils.format(req.getPaymentDate(), "HH.mm", new Locale("th", "TH")));
			params02.put("customerNameReceipt", req.getCustomerNameReceipt());
			params02.put("organizeId", req.getOrganizeId());
			params02.put("address", req.getAddress());
			params02.put("pageActive", "2");
			params02.put("pageTotal", "2");
			params02.put("majorNo", req.getMajorNo());
//			params02.put("reprintHeader", "TEST REPRINT HEADER");
//			params02.put("reasonHeader", "สาเหตุการพิมพ์ใบแทน");
//			params02.put("reason", "testreson 00000000000000000000000000000000000000000000000");
			if (BeanUtils.isNotEmpty(req.getAmountTmb())) {
				//vat
				params02.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params02.put("amountTmb", formatNumber.format(req.getAmountTmb()));

				vatAmount = (req.getAmountTmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params02.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmountTmb().doubleValue() - vatAmount;
				params02.put("feeAmount",formatNumber.format(feeAmount));
	
				/*thaiBath*/
				params02.put("thaiBath", new ThaiBaht().getText(req.getAmountTmb()));
			} else {
				params02.put("vat", "0.00");
				params02.put("amountTmb", "0.00");
				params02.put("feeAmount", "0.00");
				params02.put("vatAmount", "0.00");
				params02.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params02.put("tmbRequestNo", req.getTmbRequestNo());
			JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

			// merge doc
			List<ExporterInputItem> itemList = new ArrayList<>();
			itemList.add(new SimpleExporterInputItem(jasperPrint01));
			itemList.add(new SimpleExporterInputItem(jasperPrint02));

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(itemList));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			// set_name
			String name = "RECEIPT_" + req.getTmbRequestNo() + ".pdf";

			// save to DB
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			req.setReceiptFile(name);
			req.setReceiptDate(timestamp);
//			upDateReqDetailDao.update(req);
			upDateReqDetailDao.updateAfterPrint(req);
			
//			UserDetails user = UserLoginUtils.getCurrentUserLogin();
//			ReqReceiptVo reqReceipt = addReqReceipt(req);
//			reqReceipt.setReqform_id(vo.getId());
//			reqReceipt.setFile_name(req.getReceiptFile());
//			reqReceipt.setCreatedById(user.getUserId());
//			reqReceipt.setCreatedByName(user.getFirstName()+" "+user.getLastName());
//			int dupReqid = upDateReqDetailDao.checkDuplicateReqID(vo.getId());
//			if (dupReqid > 0) {
//				upDateReqDetailDao.insertReqRecipt(reqReceipt);
//			}

			// สร้าง ที่ พาท REPORT
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			// สร้าง ที่ พาท upload
			String folder = SUB_PATH_UPLOAD;
			upload.createFile(reportFile, folder, name);

			ReportUtils.closeResourceFileInputStream(params02);
		}catch(Exception ex) {
			logger.error("ReportPdfService Error: {} ", ex);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.RECEIPT_CODE, ACTION_AUDITLOG_DESC.RECEIPT,
					(req!=null ? req.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), 
					currentDate);
		}
		
		return "RECEIPT_" + (req!=null ?  req.getTmbRequestNo() : null);
	}

	/* coverSheetToPDF */
	public String coverSheetToPdf(RpCoverSheetVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		RequestForm req = null;
		try {
			// Folder Exist ??
			initialService();
			req = checkReqDetailDao.findReqFormById(vo.getId(), false);
			RpReceiverVo receiver = reportPdfDao.receiver(vo.getId()).get(0);
			
			String reportName = "RP_COVER_SHEET";

			Map<String, Object> params01 = new HashMap<>();
			params01.put("logoTmbCover01", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmbCover01.png"));
			params01.put("logoTmbCover02", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmbCover02.png"));
			params01.put("tmbReqNo", req.getTmbRequestNo());
			
			params01.put("createdById", receiver.getCreatedById());
			params01.put("createdByName", receiver.getCreatedByName());
			params01.put("createdByDepartment", receiver.getCreatedByDepartment());
			params01.put("createdByGroup", receiver.getCreatedByGroup());
			params01.put("createdByBelongto", receiver.getCreatedByBelongto());
			params01.put("createdByTel", receiver.getCreatedByTel());
			params01.put("createdByEmail", receiver.getCreatedByEmail());
						
			params01.put("date", DateFormatUtils.format(new java.util.Date(),"dd/MM/yyyy",new Locale("th", "TH")));
			params01.put("time", DateFormatUtils.format(new java.util.Date(),"HH.mm",new Locale("th", "TH")));
			
			List<RpCertificateVo> rpCertificateList = new ArrayList<>();
			RpCertificateVo rpCertificate = null;
			int i =0;
			for(RpCertificateVo data: reportPdfDao.certificate(vo.getId())) {
				rpCertificate = new RpCertificateVo();
				rpCertificate.setSeq(String.valueOf(i + 1));
				if("10005".equals(data.getCertificateCode())) {
					rpCertificate.setCertificate(data.getOther());
				}else {
					rpCertificate.setCertificate(data.getCertificate());
				}
				rpCertificate.setTotalNumber(data.getTotalNumber());
				rpCertificateList.add(rpCertificate);
				i++;	
			}
		
			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName, params01,
					new JRBeanCollectionDataSource(rpCertificateList));

			// set_name
			String name = "COVERSHEET_" + req.getTmbRequestNo() + ".pdf";

			byte[] reportFile = JasperExportManager.exportReportToPdf(jasperPrint01);

			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			ReportUtils.closeResourceFileInputStream(params01);
		}catch(Exception  e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_PRINT_COVERSHEET, e.toString());
			logger.error("ReportPdfService Error: ", e);
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.COVERSHEET_CODE, ACTION_AUDITLOG_DESC.COVERSHEET,
					(req!=null ? req.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}

		return "COVERSHEET_" + (req !=null ? req.getTmbRequestNo() : null);
	}

	/* reqFormOriginalToPdf */
	public String reqFormOriginalToPdf(RpReqFormVo vo) throws IOException, JRException {
		
		Date currentDate = new Date();
		ByteArrayOutputStream os = null;
		try {

			initialService();
			// RP001
			String reportName01 = "RP001_REQ_FORM_01";

			Map<String, Object> params01 = new HashMap<>();

			params01.put("logoDbd", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoDbd.png"));
			params01.put("typeCertificate", "นิติบุคคล");
			params01.put("tmpReqNo", vo.getTmpReqNo());

			List<RpReqFormListVo> rpReqFormListVoList = new ArrayList<>();
			RpReqFormListVo rpReqFormListVo = null;
			
			for (int i = 0; i < 5; i++) {
				rpReqFormListVo = new RpReqFormListVo();
				rpReqFormListVo.setSeq(String.valueOf(i + 1));
				rpReqFormListVo.setBox1(false);
				rpReqFormListVo.setBox2(false);
				rpReqFormListVo.setBox3(false);
				rpReqFormListVo.setBox4(false);			
				
				rpReqFormListVoList.add(rpReqFormListVo);
			}

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01,
					new JRBeanCollectionDataSource(rpReqFormListVoList));

			// RP002
			String reportName02 = "RP001_REQ_FORM_02";

			Map<String, Object> params02 = new HashMap<>();
			params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params02.put("tmpReqNo", vo.getTmpReqNo());
			JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

			List<ExporterInputItem> itemList = new ArrayList<>();
			itemList.add(new SimpleExporterInputItem(jasperPrint01));
			itemList.add(new SimpleExporterInputItem(jasperPrint02));

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(itemList));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			String name = "REQFORM_" + vo.getTmpReqNo() + ".pdf";
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			ReportUtils.closeResourceFileInputStream(params02);
		}catch(Exception e) {
			logger.error("ReportPdfService Error: ", e);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.PRINT_FORM_CODE, ACTION_AUDITLOG_DESC.PRINT_FORM,
					(vo!=null ? vo.getTmpReqNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}

		return "REQFORM_" + (vo!=null ? vo.getTmpReqNo(): null);
	}
	
	/* NewFormWithoutCertificatesToPdf */
	public String reqFormWithoutCert(RpReqFormVo vo) throws IOException, JRException {
		
		Date currentDate = new Date();
		ByteArrayOutputStream os = null;
		try {

			initialService();
			// RP001
			String reportName01 = "REQUESTFORM_ONLY_1ST";

			Map<String, Object> params01 = new HashMap<>();

			params01.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params01.put("tmpReqNo", vo.getTmpReqNo());
			params01.put("reqDate", DateFormatUtils.format(new Date(), "dd MMMM yyyy", new Locale("th", "TH")));

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01,
					 new JREmptyDataSource());
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint01));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			String name = "REQFORM_" + vo.getTmpReqNo() + ".pdf";
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			ReportUtils.closeResourceFileInputStream(params01);
		}catch(Exception e) {
			logger.error("ReportPdfService Error: ", e);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.PRINT_FORM_CODE, ACTION_AUDITLOG_DESC.PRINT_FORM,
					(vo!=null ? vo.getTmpReqNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}

		return "REQFORM_" + (vo!=null ? vo.getTmpReqNo(): null);
	}

	/* reqFormToPdf */
	public String reqFormToPdf(RpReqFormVo vo) throws IOException, JRException {
		
		Date currentDate = new Date();
		ByteArrayOutputStream os = null;
		try{
			initialService();
			// RP001
			String reportName01 = "RP001_REQ_FORM_01";

			Map<String, Object> params01 = new HashMap<>();

			params01.put("logoDbd", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoDbd.png"));
			if ("50001".equals(vo.getTypeCertificate())) {
				params01.put("typeCertificate", "นิติบุคคล");
			} else if ("50002".equals(vo.getTypeCertificate())) {
				params01.put("typeCertificate", "ธุรกิจต่างด้าว");
			} else if ("50003".equals(vo.getTypeCertificate())) {
				params01.put("typeCertificate", "สมาคมและหอการค้า");
			} else {
				params01.put("typeCertificate", "   ");
			}
			params01.put("customerName", vo.getCustomerName());
			params01.put("telephone", vo.getTelephone());

			Date reqDate = DateConstant.convertStrDDMMYYYYToDate(vo.getReqDate());
			params01.put("reqDate", DateFormatUtils.format(reqDate, "dd MMMM yyyy", new Locale("th", "TH")));

			params01.put("tmpReqNo", vo.getTmpReqNo());

			List<RpReqFormListVo> rpReqFormListVoList = new ArrayList<>();
			RpReqFormListVo rpReqFormListVo = null;

			int i = 0;
			for (RpReqFormListVo data : vo.getRpReqFormList()) {
				rpReqFormListVo = new RpReqFormListVo();
				params01.put("organizeId", vo.getOrganizeId());
				params01.put("companyName", vo.getCompanyName());
				params01.put("accountName", vo.getAccountName());
				rpReqFormListVo.setSeq(String.valueOf(i + 1));

				if (BeanUtils.isNotEmpty(data.getBox1())) {
					rpReqFormListVo.setBox1(data.getBox1());
				}
				if (BeanUtils.isNotEmpty(data.getBox2())) {
					rpReqFormListVo.setBox2(data.getBox2());
				}
				if (BeanUtils.isNotEmpty(data.getBox3())) {
					rpReqFormListVo.setBox3(data.getBox3());
				}
				if (BeanUtils.isNotEmpty(data.getBox4())) {
					rpReqFormListVo.setBox4(data.getBox4());
				}
				
				if (BeanUtils.isNotEmpty(data.getTotalNum())) {
					rpReqFormListVo.setTotalNum(data.getTotalNum());
				}
				if (BeanUtils.isNotEmpty(data.getNumSetCc()) || BeanUtils.isNotEmpty(data.getNumEditCc())
						|| BeanUtils.isNotEmpty(data.getNumOtherCc())) {
					rpReqFormListVo.setNumSetCc(data.getNumSetCc());
					rpReqFormListVo.setNumEditCc(data.getNumEditCc());
					rpReqFormListVo.setNumOtherCc(data.getNumOtherCc());
					rpReqFormListVo.setTotalNumCc(data.getNumSetCc() + data.getNumEditCc() + data.getNumOtherCc());
				}

				if (BeanUtils.isNotEmpty(data.getOther())) {
					rpReqFormListVo.setOther(data.getOther());
				}
				if (BeanUtils.isNotEmpty(data.getStatementYear())) {
					rpReqFormListVo.setStatementYear(data.getStatementYear());
				}
				if (BeanUtils.isNotEmpty(data.getDateEditReg())) {
					Date dateEditReg = DateConstant.convertStrDDMMYYYYToDate(data.getDateEditReg());
					rpReqFormListVo
							.setDateEditReg(DateFormatUtils.format(dateEditReg, "dd MMMM yyyy", new Locale("th", "TH")));
				}
				if (BeanUtils.isNotEmpty(data.getDateOtherReg())) {
					Date dateOtherReg = DateConstant.convertStrDDMMYYYYToDate(data.getDateOtherReg());
					rpReqFormListVo
							.setDateOtherReg(DateFormatUtils.format(dateOtherReg, "dd MMMM yyyy", new Locale("th", "TH")));
				}
				if (BeanUtils.isNotEmpty(data.getDateAccepted())) {
					Date dateAccepted = DateConstant.convertStrDDMMYYYYToDate(data.getDateAccepted());
					rpReqFormListVo
							.setDateAccepted(DateFormatUtils.format(dateAccepted, "dd MMMM yyyy", new Locale("th", "TH")));
				}
				rpReqFormListVoList.add(rpReqFormListVo);
				i++;
			}

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01,
					new JRBeanCollectionDataSource(rpReqFormListVoList));

			// RP002
			String reportName02 = "RP001_REQ_FORM_02";

			Map<String, Object> params02 = new HashMap<>();
			params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params02.put("companyName", vo.getAccountName());
			params02.put("accountNo", vo.getAccountNo());

			Date reqDate2 = DateConstant.convertStrDDMMYYYYToDate(vo.getReqDate());
			params02.put("reqDate", DateFormatUtils.format(reqDate2, "dd MMMM yyyy", new Locale("th", "TH")));

			params02.put("tmpReqNo", vo.getTmpReqNo());

			JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

			List<ExporterInputItem> itemList = new ArrayList<>();
			itemList.add(new SimpleExporterInputItem(jasperPrint01));
			itemList.add(new SimpleExporterInputItem(jasperPrint02));

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(itemList));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			String name = "REQFORM_" + vo.getTmpReqNo() + ".pdf";
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			ReportUtils.closeResourceFileInputStream(params02);
		}catch(Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_PRINT_RECEIPT, e.toString());
			logger.error("ReportPdfService Error: ", e);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.PRINT_FORMREQ_CODE, ACTION_AUDITLOG_DESC.PRINT_FORMREQ,
					 (vo!=null ? vo.getTmpReqNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}

		return "REQFORM_" + (vo!=null ? vo.getTmpReqNo() : null);
	}

	/* viewPdfToData */
	public void viewPdfToData(String name, HttpServletResponse response) throws Exception {
		FileInputStream reportFile = null;
		try {
			File file = new File(PATH_REPORT+"/" + name + ".pdf");
			reportFile = new FileInputStream(file);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;filename=" + name + ".pdf");
			IOUtils.copy(reportFile, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			this.logger.error("viewPdfToData", e);
		} finally {
			if (reportFile != null) {
				reportFile.close();
			}
		}
	}
	public String reprintReceiptTax(RpReceiptTaxVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		ReqReceiptVo req = null;
		ByteArrayOutputStream os =null;
		try {
			// Folder Exist ??
			initialService();
			DecimalFormat formatNumber = new DecimalFormat("#,##0.00");
			Double oneHundred = new Double(100);
			Double  feeAmount = new Double(0);
			Double vatAmount = new Double(0);
			
			req = checkReqDetailDao.findRequestReceiptByReqID(vo.getId());
			RpVatVo vat = reportPdfDao.vat().get(0);
			req.setReqform_id(vo.getId());
			int print_count = 0;
			if (req.getPrint_count() != null ) {
				print_count = req.getPrint_count();
				print_count++;
			}

			// RP001
			String reportName01 = "RP_RECEIPT_TAX";
			Map<String, Object> params01 = new HashMap<>();
			params01.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params01.put("docType", "ใบแทน ( ต้นฉบับ )");
			params01.put("receiptNo", req.getReceipt_no());
//			params01.put("date", DateFormatUtils.format(req.getReceipt_date(), "dd MMMM yyyy", new Locale("th", "TH")));
//			params01.put("time", DateFormatUtils.format(req.getReceipt_date(), "HH.mm", new Locale("th", "TH")));
			params01.put("date", DateFormatUtils.format(new Date(), "dd MMMM yyyy", new Locale("th", "TH")));
			params01.put("time", DateFormatUtils.format(new Date(), "HH.mm", new Locale("th", "TH")));
			params01.put("customerNameReceipt", req.getCustomer_name());
			params01.put("organizeId", req.getOrganize_id());
			params01.put("address", req.getAddress());
			params01.put("pageActive", "1");
			params01.put("pageTotal", "2");
			params01.put("majorNo", req.getMajor_no());
			params01.put("reprintHeader", "พิมพ์ซ่อมครั้งที่ "+ Integer.toString(print_count));
			params01.put("reasonHeader", "สาเหตุการพิมพ์ใบแทน");
			params01.put("reason", vo.getReason());
			if (BeanUtils.isNotEmpty(req.getAmount_tmb())) {
				//vat
				params01.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params01.put("amountTmb", formatNumber.format(req.getAmount_tmb()));
				
				vatAmount = (req.getAmount_tmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params01.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmount_tmb().doubleValue() - vatAmount;
				params01.put("feeAmount",formatNumber.format(feeAmount));
				
				/*thaiBath*/
				params01.put("thaiBath", new ThaiBaht().getText(req.getAmount_tmb()));
			} else {
				params01.put("vat", "0.00");
				params01.put("amountTmb", "0.00");
				params01.put("feeAmount", "0.00");
				params01.put("vatAmount", "0.00");
				params01.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params01.put("tmbRequestNo", req.getTmb_requestno());
			String reportName02 = "RP_RECEIPT_TAX";
			Map<String, Object> params02 = new HashMap<>();
			params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params02.put("docType", "ใบแทน ( สำเนา )");
			params02.put("receiptNo", req.getReceipt_no());
//			params02.put("date", DateFormatUtils.format(req.getReceipt_date(), "dd MMMM yyyy", new Locale("th", "TH")));
//			params02.put("time", DateFormatUtils.format(req.getReceipt_date(), "HH.mm", new Locale("th", "TH")));
			params02.put("date", DateFormatUtils.format(new Date(), "dd MMMM yyyy", new Locale("th", "TH")));
			params02.put("time", DateFormatUtils.format(new Date(), "HH.mm", new Locale("th", "TH")));
			params02.put("customerNameReceipt", req.getCustomer_name());
			params02.put("organizeId", req.getOrganize_id());
			params02.put("address", req.getAddress());
			params02.put("pageActive", "2");
			params02.put("pageTotal", "2");
			params02.put("majorNo", req.getMajor_no());
			params02.put("reprintHeader", "พิมพ์ซ่อมครั้งที่ "+ Integer.toString(print_count));
			params02.put("reasonHeader", "สาเหตุการพิมพ์ใบแทน");
			params02.put("reason", vo.getReason());
			if (BeanUtils.isNotEmpty(req.getAmount_tmb())) {
				//vat
				params02.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params02.put("amountTmb", formatNumber.format(req.getAmount_tmb()));
				
				vatAmount = (req.getAmount_tmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params02.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmount_tmb().doubleValue() - vatAmount;
				params02.put("feeAmount",formatNumber.format(feeAmount));
				
				/*thaiBath*/
				params02.put("thaiBath", new ThaiBaht().getText(req.getAmount_tmb()));
			} else {
				params02.put("vat", "0.00");
				params02.put("amountTmb", "0.00");
				params02.put("feeAmount", "0.00");
				params02.put("vatAmount", "0.00");
				params02.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params02.put("tmbRequestNo", req.getTmb_requestno());

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JREmptyDataSource());
			JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

			// merge doc
			List<ExporterInputItem> itemList = new ArrayList<>();
			itemList.add(new SimpleExporterInputItem(jasperPrint01));
			itemList.add(new SimpleExporterInputItem(jasperPrint02));

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(itemList));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			// set_name
			String name = "RECEIPT_" + req.getTmb_requestno() + ".pdf";

			// save to DB
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//			req.setReceiptFile(name);
			req.setReceipt_date(new Timestamp(System.currentTimeMillis()));
			req.setPrint_count(print_count);
			req.setReason(vo.getReason());
//			req.setDelete_flag(0);
			upDateReqDetailDao.updateReqReceipt(req);
			
			// สร้าง ที่ พาท REPORT
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			// สร้าง ที่ พาท upload
			String folder = SUB_PATH_UPLOAD;
			upload.createFile(reportFile, folder, name);

			ReportUtils.closeResourceFileInputStream(params01);
		}catch(Exception ex) {
			logger.error("ReportPdfService Error: {} ", ex);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.RECEIPT_CODE, ACTION_AUDITLOG_DESC.RECEIPT,
					(req!=null ? req.getTmb_requestno() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), 
					currentDate);
		}
		
		return "RECEIPT_" + (req!=null ?  req.getTmb_requestno() : null);
	}
	
	
	public String cancelReceiptTax(RpReceiptTaxVo vo) throws IOException, JRException {
		Date currentDate = new Date();
		ReqReceiptVo req = null;
		ByteArrayOutputStream os =null;
		String cancelRemark = "เป็นการยกเลิกและออกใบกำกับภาษีฉบับใหม่แทนฉบับเดิมเลขที %s เนื่องจาก %s ";
		try {
			UserDetails user = UserLoginUtils.getCurrentUserLogin();
			// Folder Exist ??
			initialService();
			DecimalFormat formatNumber = new DecimalFormat("#,##0.00");
			Double oneHundred = new Double(100);
			Double  feeAmount = new Double(0);
			Double vatAmount = new Double(0);
			
			req = checkReqDetailDao.findRequestReceiptByReqID(vo.getId());
			RpVatVo vat = reportPdfDao.vat().get(0);
			
			String receiptNo = receiptGenKeyService.getNextKey();
			String oldReceiptNo = req.getReceipt_no();
//			req.setCancel_flag(0);
			req.setReqform_id(vo.getId());
			req.setReason("ถูกยกเลิกและออกใบกำกับภาษีฉบับใหม่ด้วย เลขที่ "+ receiptNo);
			req.setUpdatedById(user.getUserId());
			req.setUpdatedByName(user.getFirstName()+" "+user.getLastName());
			upDateReqDetailDao.updateCancelFlagReqReceipt(req);
			
			req.setReceipt_no_reference(oldReceiptNo);
			req.setReceipt_no(receiptNo);

			// RP001
			String reportName01 = "RP_RECEIPT_TAX";
			Map<String, Object> params01 = new HashMap<>();
			params01.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params01.put("docType", "ต้นฉบับ");
			params01.put("receiptNo", req.getReceipt_no());
			params01.put("date", DateFormatUtils.format(req.getReceipt_date(), "dd MMMM yyyy", new Locale("th", "TH")));
			params01.put("time", DateFormatUtils.format(req.getReceipt_date(), "HH.mm", new Locale("th", "TH")));
			params01.put("customerNameReceipt", vo.getCustomerName());
			params01.put("organizeId", vo.getOrganizeId());
			params01.put("address", vo.getAddress());
			params01.put("pageActive", "1");
			params01.put("pageTotal", "2");
			params01.put("majorNo", vo.getBarnchCode());
//			params01.put("reprintHeader", "พิมพ์ซ่อมครั้งที่ "+ Integer.toString(print_count));
			params01.put("reasonHeader", "สาเหตุการยกเลิก");
			params01.put("reason",  String.format(cancelRemark,req.getReceipt_no_reference(),vo.getReason()));
			if (BeanUtils.isNotEmpty(req.getAmount_tmb())) {
				//vat
				params01.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params01.put("amountTmb", formatNumber.format(req.getAmount_tmb()));
				
				vatAmount = (req.getAmount_tmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params01.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmount_tmb().doubleValue() - vatAmount;
				params01.put("feeAmount",formatNumber.format(feeAmount));
				
				/*thaiBath*/
				params01.put("thaiBath", new ThaiBaht().getText(req.getAmount_tmb()));
			} else {
				params01.put("vat", "0.00");
				params01.put("amountTmb", "0.00");
				params01.put("feeAmount", "0.00");
				params01.put("vatAmount", "0.00");
				params01.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params01.put("tmbRequestNo", req.getTmb_requestno());
			String reportName02 = "RP_RECEIPT_TAX";
			Map<String, Object> params02 = new HashMap<>();
			params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
			params02.put("docType", "สำเนา");
			params02.put("receiptNo", req.getReceipt_no());
			params02.put("date", DateFormatUtils.format(req.getReceipt_date(), "dd MMMM yyyy", new Locale("th", "TH")));
			params02.put("time", DateFormatUtils.format(req.getReceipt_date(), "HH.mm", new Locale("th", "TH")));
			params02.put("customerNameReceipt", vo.getCustomerName());
			params02.put("organizeId", vo.getOrganizeId());
			params02.put("address", vo.getAddress());
			params02.put("pageActive", "2");
			params02.put("pageTotal", "2");
			params02.put("majorNo", vo.getBarnchCode());
//			params02.put("reprintHeader", "พิมพ์ซ่อมครั้งที่ "+ Integer.toString(print_count));
			params02.put("reasonHeader", "สาเหตุการยกเลิก");
			params02.put("reason",  String.format(cancelRemark,req.getReceipt_no_reference(),vo.getReason()));
			if (BeanUtils.isNotEmpty(req.getAmount_tmb())) {
				//vat
				params02.put("vat",formatNumber.format(Double.parseDouble(vat.getVat())));
				//amountTmb
				params02.put("amountTmb", formatNumber.format(req.getAmount_tmb()));
				
				vatAmount = (req.getAmount_tmb().doubleValue() * Double.parseDouble(vat.getVat()) / ( 100+Double.parseDouble(vat.getVat()) ));
				params02.put("vatAmount",formatNumber.format(vatAmount));
				 
				feeAmount = req.getAmount_tmb().doubleValue() - vatAmount;
				params02.put("feeAmount",formatNumber.format(feeAmount));
				
				/*thaiBath*/
				params02.put("thaiBath", new ThaiBaht().getText(req.getAmount_tmb()));
			} else {
				params02.put("vat", "0.00");
				params02.put("amountTmb", "0.00");
				params02.put("feeAmount", "0.00");
				params02.put("vatAmount", "0.00");
				params02.put("thaiBath", "ศูนย์บาทถ้วน");
			}

			params02.put("tmbRequestNo", req.getTmb_requestno());

			JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JREmptyDataSource());
			JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

			// merge doc
			List<ExporterInputItem> itemList = new ArrayList<>();
			itemList.add(new SimpleExporterInputItem(jasperPrint01));
			itemList.add(new SimpleExporterInputItem(jasperPrint02));

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(itemList));

			os = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			exporter.exportReport();

			byte[] reportFile = os.toByteArray();

			// set_name
			String name = "RECEIPT_" + req.getTmb_requestno() + ".pdf";

			// save to DB
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			req.setReason(String.format(cancelRemark,req.getReceipt_no_reference(),vo.getReason()));
			req.setDelete_flag(0);
			req.setReqform_id(vo.getId());
			req.setAddress(vo.getAddress());
			req.setCustomer_name(vo.getCustomerName());
			req.setMajor_no(vo.getBarnchCode());
			req.setOrganize_id(vo.getOrganizeId());
			req.setFile_name(name);
			req.setCreatedById(user.getUserId());
			req.setCreatedByName(user.getFirstName()+" "+user.getLastName());
			req.setCancel_flag(1);
			upDateReqDetailDao.insertReqRecipt(req);
			
			// สร้าง ที่ พาท REPORT
			IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT+"/" + name)));
			// สร้าง ที่ พาท upload
			String folder = SUB_PATH_UPLOAD;
			upload.createFile(reportFile, folder, name);

			ReportUtils.closeResourceFileInputStream(params01);
		}catch(Exception ex) {
			logger.error("ReportPdfService Error: {} ", ex);
		}finally {
			EcertFileUtils.closeStream( os );
			auditLogService.insertAuditLog(ACTION_AUDITLOG.RECEIPT_CODE, ACTION_AUDITLOG_DESC.RECEIPT,
					(req!=null ? req.getTmb_requestno() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), 
					currentDate);
		}
		
		return "RECEIPT_" + (req!=null ?  req.getTmb_requestno() : null);
	}
	
	public ReqReceiptVo addReqReceipt(RequestForm req ) {
		
		ReqReceiptVo reqReceipt = new ReqReceiptVo();
		reqReceipt.setReceipt_no( req.getReceiptNo());
		reqReceipt.setReceipt_date(req.getPaymentDate());
		reqReceipt.setCustomer_name(req.getCustomerNameReceipt());
		reqReceipt.setOrganize_id(req.getOrganizeId());
		reqReceipt.setAddress(req.getAddress());
		reqReceipt.setMajor_no(req.getMajorNo());
		reqReceipt.setTmb_requestno(req.getTmbRequestNo());

		if (BeanUtils.isNotEmpty(req.getAmountTmb())) {
			reqReceipt.setAmount(req.getAmountTmb());
			reqReceipt.setAmount_vat_tmb(req.getAmountTmbVat());
			reqReceipt.setAmount_dbd(req.getAmountDbd());
			reqReceipt.setAmount_tmb(req.getAmountTmb());
		} else {
			reqReceipt.setAmount(BigDecimal.valueOf(0));
			reqReceipt.setAmount_vat_tmb(BigDecimal.valueOf(0));
			reqReceipt.setAmount_dbd(BigDecimal.valueOf(0));
			reqReceipt.setAmount_tmb(BigDecimal.valueOf(0));
		}

		return reqReceipt;
		
	}

}
