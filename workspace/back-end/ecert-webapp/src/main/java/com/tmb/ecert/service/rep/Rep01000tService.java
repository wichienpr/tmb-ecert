package com.tmb.ecert.service.rep;

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
import com.tmb.ecert.persistence.dao.Rep01000Repository;
import com.tmb.ecert.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.persistence.vo.Rep01000Vo;

@Service
public class Rep01000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep01000Repository rep01000Repository;
	
	@Autowired
	private ExcalService excalService;
	
	
	public List<Rep01000Vo> findAll(Rep01000FormVo formVo){
		List<Rep01000Vo> rep01000Vo = new ArrayList<Rep01000Vo>();
		rep01000Vo = rep01000Repository.getData(formVo);
		return rep01000Vo;
	}
	
	public void exportFile(Rep01000FormVo formVo, HttpServletResponse response) throws IOException {
		
		List<Rep01000Vo> dataTestList = new ArrayList<Rep01000Vo>();
		Rep01000Vo dataTest = new Rep01000Vo();
		dataTest.setId(15l);
			
		dataTestList.add(dataTest);
		
//		dataTestList = formVo.getDataT();
		
			/* create spreadsheet */
			XSSFWorkbook workbook = excalService.setUpExcel();
			CellStyle thStyle = excalService.thStyle;
			Sheet sheet = workbook.createSheet();
			int rowNum = 0;
			int cellNum = 0;
			Row row = sheet.createRow(rowNum);
			Cell cell = row.createCell(cellNum);
			System.out.println("Creating excel");
			
			 
			/* create data spreadsheet */
			
			/* Header */
//			String[] tbTH1 = formVo.getTrHtml1();
			String[] tbTH1 = { "ลำดับ","วันที่","เลขที่อ้างอิง (TMB Req No.)","เลขที่นิติบุคคล", "ชื่อ",
		             "Segment", "ประเภทคำขอ","รายละเอียดคำขอ","เลขที่บัญชี","จำนวนเงิน : บาท",
		             "รวม","Marker","Checker","สถานะ","หมายเหตุ"};
			int nextColspan = 0;
			for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
				cell = row.createCell(nextColspan);
				cell.setCellValue(tbTH1[cellNum]);
				cell.setCellStyle(thStyle);
				if(nextColspan>=2) {nextColspan+=2;}else{nextColspan++;}
			};

//			String[] tbTH2 = formVo.getTrHtml2();
			String[] tbTH2 = { "DBD", "TMB","Tax Amount","","","","Success","Fail"};
			row = sheet.createRow(1);
			int cellNumtbTH2 = 9;
			for (int i = 0; i < tbTH2.length; i++) {
				cell = row.createCell(cellNumtbTH2);
				cell.setCellValue(tbTH2[i]);
				cell.setCellStyle(thStyle);
				cellNumtbTH2++;
			};
			
			// merge(firstRow, lastRow, firstCol, lastCol)
			
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); //tr1 rowspan=2
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1)); //tr2 rowspan=2
			
			int firstCol = 2;
			int lastCol  = 3;
			for (firstCol = 2; firstCol <= (tbTH2.length);firstCol += 2) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, firstCol, lastCol));//tr?? rowspan=1 colspan=2
				lastCol += 2;
			}
			
			
			
			/* Detail */
//			List<LicenseList6010> exportDataList = null;
			
			rowNum = 2;
			cellNum = 0;
			for (Rep01000Vo detail : dataTestList) {
				row = sheet.createRow(rowNum);
				// No.
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				cell = row.createCell(cellNum++);cell.setCellValue(detail.getId());cell.setCellStyle(excalService.cellCenter);
				rowNum++;
				cellNum = 0;
			}
			
			
			/*set	fileName*/		
			String fileName ="test_"+DateFormatUtils.format(new Date(),"yyyyMMdd", new Locale("th", "TH"));;
			System.out.println(fileName);
			
			/* write it as an excel attachment */
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte [] outArray = outByteStream.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
			
			log.info("Done");
		}
	
}


