package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.report.persistence.vo.RpCoverSheetVo;
import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormListVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormOriginalVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormVo;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;

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
public class ReportPdfService {

	@Value("${app.datasource.path.report}")
	private String PATH_REPORT;

	@Autowired
	private UploadService upload;

	private static String SUB_PATH_UPLOAD = "tmb-requestor/";

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;

	@Autowired
	private RequestorDao upDateReqDetailDao;

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
		// Folder Exist ??
		initialService();

		RequestForm req = checkReqDetailDao.findReqFormById(vo.getId(), false).get(0);

		// RP001
		String reportName01 = "RP_RECEIPT_TAX";
		Map<String, Object> params01 = new HashMap<>();
		params01.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
		params01.put("docType", "ต้นฉบับ");
		params01.put("date", DateFormatUtils.format(req.getCreatedDateTime(), "dd MMMM yyyy", new Locale("th", "TH")));

		JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01, new JREmptyDataSource());

		// RP002
		String reportName02 = "RP_RECEIPT_TAX";
		Map<String, Object> params02 = new HashMap<>();
		params02.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
		params02.put("docType", "สำเนา");
		JasperPrint jasperPrint02 = ReportUtils.exportReport(reportName02, params02, new JREmptyDataSource());

		// merge doc
		List<ExporterInputItem> itemList = new ArrayList<>();
		itemList.add(new SimpleExporterInputItem(jasperPrint01));
		itemList.add(new SimpleExporterInputItem(jasperPrint02));

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(itemList));

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		exporter.exportReport();

		byte[] reportFile = os.toByteArray();

		// set_name
		String name = "RECEIPT_" + req.getTmbRequestNo() + ".pdf";

		// save to DB
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		req.setReceiptFile(name);
		req.setReceiptDate(timestamp);
		upDateReqDetailDao.update(req);

		// สร้าง ที่ พาท REPORT
		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT + name)));
		// สร้าง ที่ พาท upload
		String folder = SUB_PATH_UPLOAD;
		upload.createFile(reportFile, folder, name);

		ReportUtils.closeResourceFileInputStream(params02);
		return "RECEIPT_" + req.getTmbRequestNo();
	}

	/* coverSheetToPDF */
	public String coverSheetToPdf(RpCoverSheetVo vo) throws IOException, JRException {
		// Folder Exist ??
		initialService();

		String reportName = "RP_COVER_SHEET";

		Map<String, Object> params = new HashMap<>();
		params.put("logoTmbCover01", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmbCover01.png"));
		params.put("logoTmbCover02", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmbCover02.png"));

		JasperPrint jasperPrint = ReportUtils.exportReport(reportName, params, new JREmptyDataSource());

		// set_name
		RequestForm req = checkReqDetailDao.findReqFormById(vo.getId(), false).get(0);
		String name = "COVERSHEET_" + req.getTmbRequestNo() + ".pdf";

		byte[] reportFile = JasperExportManager.exportReportToPdf(jasperPrint);

		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT + name)));
		ReportUtils.closeResourceFileInputStream(params);

		return "COVERSHEET_" + req.getTmbRequestNo();
	}

	/* reqFormOriginalToPdf */
	public String reqFormOriginalToPdf(RpReqFormOriginalVo vo) throws IOException, JRException {

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
			rpReqFormListVoList.add(rpReqFormListVo);
		}

		JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01,
				new JRBeanCollectionDataSource(rpReqFormListVoList));

		// RP002
		String reportName02 = "RP001_REQ_FORM_02";

		Map<String, Object> params02 = new HashMap<>();
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

		byte[] reportFile = os.toByteArray();

		String name = "REQFORM_" + vo.getTmpReqNo() + ".pdf";
		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT + name)));
		ReportUtils.closeResourceFileInputStream(params02);

		return "REQFORM_" + vo.getTmpReqNo();
	}

	/* reqFormToPdf */
	public String reqFormToPdf(RpReqFormVo vo) throws IOException, JRException {

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

			if (BeanUtils.isNotEmpty(data.getBoxIndex())) {
				rpReqFormListVo.setBoxIndex(data.getBoxIndex());
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
				rpReqFormListVo.setStatementYear(String.valueOf(Integer.parseInt(data.getStatementYear()) + 543));
			}
			if (BeanUtils.isNotEmpty(data.getDateEditReg())) {
				Date dateEditReg = DateConstant.convertStrDDMMYYYYToDate(data.getDateEditReg());
				rpReqFormListVo.setDateEditReg(DateFormatUtils.format(dateEditReg, "dd MMMM yyyy", new Locale("th", "TH")));
			}
			if (BeanUtils.isNotEmpty(data.getDateOtherReg())) {
				Date dateOtherReg = DateConstant.convertStrDDMMYYYYToDate(data.getDateOtherReg());
				rpReqFormListVo.setDateOtherReg(DateFormatUtils.format(dateOtherReg, "dd MMMM yyyy", new Locale("th", "TH")));
			}
			if (BeanUtils.isNotEmpty(data.getDateAccepted())) {
				Date dateAccepted = DateConstant.convertStrDDMMYYYYToDate(data.getDateAccepted());
				rpReqFormListVo.setDateAccepted(DateFormatUtils.format(dateAccepted, "dd MMMM yyyy", new Locale("th", "TH")));
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
		params02.put("companyName", vo.getCompanyName());
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

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		exporter.exportReport();

		byte[] reportFile = os.toByteArray();

		String name = "REQFORM_" + vo.getTmpReqNo() + ".pdf";
		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT + name)));
		ReportUtils.closeResourceFileInputStream(params02);

		return "REQFORM_" + vo.getTmpReqNo();
	}

	/* viewPdfToData */
	public void viewPdfToData(String name, HttpServletResponse response) throws Exception {
		FileInputStream reportFile = null;
		try {
			File file = new File(PATH_REPORT + name + ".pdf");
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

}
