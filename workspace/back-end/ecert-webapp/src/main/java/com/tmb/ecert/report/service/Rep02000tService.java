package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.service.ExcalService;
import com.tmb.ecert.report.persistence.dao.RepDao;
import com.tmb.ecert.report.persistence.vo.Rep02000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02000Vo;

import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;

@Service
public class Rep02000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RepDao repDao;

	@Autowired
	private ExcalService excalService;

	public List<Rep02000Vo> findAll(Rep02000FormVo formVo) {
		List<Rep02000Vo> rep02000VoList = new ArrayList<Rep02000Vo>();
		List<Rep02000Vo> rep02000VoListReturn = new ArrayList<Rep02000Vo>();
		rep02000VoList = repDao.getDataRep02000(formVo);

		for (Rep02000Vo rep02000Vo : rep02000VoList) {

			List<Rep02000Vo> countCertificateList = repDao.getCountCertificateRep02000(formVo, rep02000Vo);
			rep02000Vo.setCertificate(
					(countCertificateList.size() == 0) ? 0 : countCertificateList.get(0).getCertificate());

			List<Rep02000Vo> countUnCertificateList = repDao.getCountUnCertificateRep02000(formVo, rep02000Vo);
			rep02000Vo.setCopyGuarantee(
					(countUnCertificateList.size() == 0) ? 0 : countUnCertificateList.get(0).getCopyGuarantee());

			// getPaymentTypeCountRep02000

			rep02000Vo.setPaymentTypeCountDT(repDao.getPaymentTypeCountRep02000(formVo, rep02000Vo, "30001"));
			rep02000Vo.setPaymentTypeCountDNoT(repDao.getPaymentTypeCountRep02000(formVo, rep02000Vo, "30002"));
			rep02000Vo.setPaymentTypeCountDAll(repDao.getPaymentTypeCountRep02000(formVo, rep02000Vo, "30003"));
			rep02000Vo.setPaymentTypeCountECert(repDao.getPaymentTypeCountRep02000(formVo, rep02000Vo, "30004"));

			int countStatus = 0;
			countStatus = repDao.getCountStatusRep02000(formVo, rep02000Vo);
			rep02000Vo.setSuccess(countStatus);
			rep02000Vo.setFail(rep02000Vo.getCustsegmentCount() - countStatus);

			rep02000VoListReturn.add(rep02000Vo);

		}

		return rep02000VoListReturn;
	}

	public void exportFile(Rep02000FormVo formVo, HttpServletResponse response) throws IOException {

		List<Rep02000Vo> dataTestList = new ArrayList<Rep02000Vo>();

		dataTestList = findAll(formVo);
		// dataTestList = formVo.getDataT();

		/* create spreadsheet */
		XSSFWorkbook workbook = excalService.setUpExcel();
		CellStyle thStyle = excalService.thStyle;
		CellStyle fontHeader = workbook.createCellStyle();
		fontHeader.setFont(excalService.fontHeader);
		Sheet sheet = workbook.createSheet();
		int rowNum = 0;
		int cellNum = 0;
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(cellNum);
		log.info("Creating excel");

		/* create data spreadsheet */

		/* Header */
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellValue("รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : Monthly");
		cell.setCellStyle(fontHeader);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 13)); // tr colspan=20
		rowNum++;
		rowNum++;

		// String[] tbTH1 = formVo.getTrHtml1();
		String[] tbTH1 = { "Segment", "จำนวนคำขอ","ประเภทการชำระเงิน", "", "", "", "ประเภทคำขอ : ราย", "", "จำนวนเงิน : บาท", "", "จำนวนเงิน",
				 "สถานะ : จำนวนราย", "", "สาเหตุ" };
		row = sheet.createRow(rowNum++);
		for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(tbTH1[cellNum]);
			cell.setCellStyle(thStyle);
		}
		;

		// String[] tbTH2 = formVo.getTrHtml2();
		String[] tbTH2 = { "ลูกค้าชำระค่าธรรมเนียม DBD,TMB",
				"ลูกค้าชำระค่าธรรมเนียม DBD ยกเว้น TMB", "TMB ชำระค่าธรรมเนียม  DBD ทั้งหมด",
				"ไม่ได้ดำเนินการชำระเงินผ่านระบบ E-Cert","หนังสือรับรอง", "รับรองสำเนา", "DBD", "TMB", "", "Success", "Fail" };
		row = sheet.createRow(rowNum);
		int cellNumtbTH2 = 2;
		for (int i = 0; i < tbTH2.length; i++) {
			cell = row.createCell(cellNumtbTH2);
			cell.setCellValue(tbTH2[i]);
			cell.setCellStyle(thStyle);
			cellNumtbTH2++;
		}
		;

		// merge(firstRow, lastRow, firstCol, lastCol)

		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, 0, 0));   //  rowspan=2
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, 1, 1));   //  rowspan=2
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, 10, 10)); //  rowspan=2
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, 13, 13)); //  rowspan=2

		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 5)); // tr colspan=4
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 6, 7)); // tr colspan=2
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 8, 9)); // tr colspan=2
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 11, 12)); // tr colspan=2

		/* Detail */
		// List<LicenseList6010> exportDataList = null;
		rowNum++;
		cellNum = 0;
		for (Rep02000Vo detail : dataTestList) {
			row = sheet.createRow(rowNum);
			// No.

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellLeft);
			cell.setCellValue((StringUtils.isNotBlank(detail.getCustsegmentDesc())) ? detail.getCustsegmentDesc() : "");
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getCustsegmentCount()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getPaymentTypeCountDT()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getPaymentTypeCountDNoT()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getPaymentTypeCountDAll()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getPaymentTypeCountECert()));

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getCertificate()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getCopyGuarantee()));

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue((StringUtils.isNotBlank(detail.getAmountDbd().toString())) ? detail.getAmountDbd().toString() : "");
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue((StringUtils.isNotBlank(detail.getAmountTmb().toString())) ? detail.getAmountTmb().toString() : "");
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue((StringUtils.isNotBlank(detail.getTotalAmount().toString())) ? detail.getTotalAmount().toString()	: "");

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getSuccess()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getFail()));
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue((StringUtils.isNotBlank(detail.getRemark())) ? detail.getRemark() : "");

			rowNum++;
			cellNum = 0;
		}

		/* set fileName */
		String fileName = "Monthly_Report_" + DateFormatUtils.format(new Date(), "yyyyMMdd");
		;
		log.info(fileName);

		/* write it as an excel attachment */
		ByteArrayOutputStream outByteStream = null;
		OutputStream outStream = null;
		try {
			outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
			outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			
		} finally {
			EcertFileUtils.closeStream(outByteStream);
			EcertFileUtils.closeStream(outStream);
		}

		log.info("Done");
	}

	public String convertAccountNo(String accountNo) {
		String accountNoReturn = "";
		accountNoReturn = accountNo.substring(0, 3) + "-" + accountNo.substring(3, 4) + "-" + accountNo.substring(4, 9)
				+ "-" + accountNo.substring(9);
		return accountNoReturn;
	}

}
