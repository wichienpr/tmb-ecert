package com.tmb.ecert.auditlog.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.tmb.ecert.auditlog.persistence.dao.AuditLogsDao;
import com.tmb.ecert.auditlog.persistence.vo.Adl01000FormVo;
import com.tmb.ecert.auditlog.persistence.vo.Adl01000Vo;
import com.tmb.ecert.common.service.ExcalService;

@Service
public class Adl01000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuditLogsDao auditLogDao;

	@Autowired
	private ExcalService excalService;

	public List<Adl01000Vo> findAll(Adl01000FormVo formVo) {
		List<Adl01000Vo> adl01000VoList = new ArrayList<Adl01000Vo>();
		adl01000VoList = auditLogDao.getDataAdl01000(formVo);
		return adl01000VoList;
	}

	public void exportFile(Adl01000FormVo formVo, HttpServletResponse response) throws IOException {

		List<Adl01000Vo> dataTestList = new ArrayList<Adl01000Vo>();

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
		cell.setCellValue("Audit Log");
		cell.setCellStyle(fontHeader);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 4)); // tr colspan=5
		rowNum++;
		rowNum++;
						
		// String[] tbTH1 = formVo.getTrHtml1();
		String[] tbTH1 = { "ลำดับ", "วันที่ดำเนินการ", "User ID", "Action", "รายละเอียด"};
		row = sheet.createRow(rowNum++);
		for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(tbTH1[cellNum]);
			cell.setCellStyle(thStyle);
		}
		;

		/* Detail */
		// List<LicenseList6010> exportDataList = null;
		cellNum = 0;
		int order = 1;
		for (Adl01000Vo detail : dataTestList) {
			row = sheet.createRow(rowNum);
			// No.
			cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue(order);
			cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getCreatedDatetime()    ))?detail.getCreatedDatetime()     : "" );
			cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getCreatedById()        ))?detail.getCreatedById()         : "" );
			cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getActionDesc()         ))?detail.getActionDesc()          : "" );
			cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getDescription()        ))?detail.getDescription()         : "" );
			
			rowNum++;
			order++;
			cellNum = 0;
		}

		/* set fileName */
		String fileName = "Audit_Log_" + DateFormatUtils.format(new Date(), "yyyyMMdd");
		;
		log.info(fileName);

		/* write it as an excel attachment */
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		response.setContentType("application/vnd.ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0"); // eliminates browser caching
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
		OutputStream outStream = response.getOutputStream();
		outStream.write(outArray);
		outStream.flush();
		outStream.close();

		log.info("Done");
	}

	public String convertAccountNo(String accountNo) {
		String accountNoReturn = "";
		accountNoReturn = accountNo.substring(0, 3) + "-" + accountNo.substring(3, 4) + "-" + accountNo.substring(4, 9)
				+ "-" + accountNo.substring(9);
		return accountNoReturn;
	}

}
