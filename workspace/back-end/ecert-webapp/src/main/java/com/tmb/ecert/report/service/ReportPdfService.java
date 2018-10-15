package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.report.persistence.vo.RpCoverSheetVo;
import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;
import com.tmb.ecert.report.persistence.vo.RpReqFormOriginalVo;
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

	private static String SUB_PATH_UPLOAD = "requestor/";

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

		String reportName = "RP_RECEIPT_TAX";
		Map<String, Object> params = new HashMap<>();
		params.put("logoTmb", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoTmb.png"));
		JasperPrint jasperPrint = ReportUtils.exportReport(reportName, params, new JREmptyDataSource());

		// set_name
		RequestForm req = checkReqDetailDao.findReqFormById(vo.getId(), false).get(0);
		String name = "RECEIPT_" + req.getTmbRequestNo() + ".pdf";

		byte[] reportFile = JasperExportManager.exportReportToPdf(jasperPrint);

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

		ReportUtils.closeResourceFileInputStream(params);
		return name;
	}

	
	/*coverSheetToPDF*/
	public  String coverSheetToPdf(RpCoverSheetVo vo) throws IOException, JRException {
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

		return name;
	}
	
	
	
	/*reqFormOriginalToPdf*/
	public String reqFormOriginalToPdf(RpReqFormOriginalVo vo) throws IOException, JRException {

		initialService();
		// RP001
		String reportName01 = "RP001_REQ_FORM_01";

		Map<String, Object> params01 = new HashMap<>();

		params01.put("logoDbd", ReportUtils.getResourceFile(PATH.IMAGE_PATH, "logoDbd.png"));
		params01.put("typeCertificate",vo.getTypeCertificate());
		params01.put("tmpReqNo", vo.getTmpReqNo());

		List<RpReqFormOriginalVo>rpReqFormOriginalVoList = new ArrayList<>();
		RpReqFormOriginalVo rpReqFormOriginalVo = null;

		for (int i = 0; i < 5; i++) {
			rpReqFormOriginalVo = new RpReqFormOriginalVo();
			rpReqFormOriginalVo.setSeqNo(String.valueOf(i + 1));
			rpReqFormOriginalVoList.add(rpReqFormOriginalVo);
		}

		JasperPrint jasperPrint01 = ReportUtils.exportReport(reportName01, params01,
				new JRBeanCollectionDataSource(rpReqFormOriginalVoList));

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

		String name = "REQFORM_"+vo.getTmpReqNo()+ ".pdf";
		IOUtils.write(reportFile, new FileOutputStream(new File(PATH_REPORT + name)));
		ReportUtils.closeResourceFileInputStream(params02);

		return name;
	}


	
	/* viewPdfToData */
	public void viewPdfToData(String name, HttpServletResponse response) throws Exception {
		File file = new File(PATH_REPORT + name);
		byte[] reportFile = IOUtils.toByteArray(new FileInputStream(file)); 
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline;filename=" + name);
		response.setContentLength(reportFile.length);

		OutputStream responseOutputStream = response.getOutputStream();
		for (byte bytes : reportFile) {
			responseOutputStream.write(bytes);
		}
	}

}
