package com.tmb.ecert.report.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.service.ExcalService;
import com.tmb.ecert.report.persistence.dao.RepDao;
import com.tmb.ecert.report.persistence.vo.Rep03000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep03000Vo;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;

@Service
public class Rep03000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RepDao repDao;
	
	@Autowired
	private ExcalService excalService;
	
	
	public Rep03000FormVo findAll(Rep03000FormVo formVo){
		List<Rep03000Vo> rep03000VoList = new ArrayList<Rep03000Vo>();
		rep03000VoList = repDao.getDataRep03000(formVo);
	
		if(rep03000VoList.size()!=0) {
			formVo.setCustomerNameHead(rep03000VoList.get(0).getCustomerName());
			formVo.setOrganizeIdHead(rep03000VoList.get(0).getOrganizeId());
			formVo.setCompanyNameHead(rep03000VoList.get(0).getCompanyName());
			formVo.setBranchHead(rep03000VoList.get(0).getBranch());
			formVo.setAddressHead(rep03000VoList.get(0).getAddress());
		}
		
		formVo.setRep03000VoList(rep03000VoList);
	
		return formVo;
	}

	
	public void exportFile(Rep03000FormVo formVo, HttpServletResponse response) throws IOException {
		Rep03000FormVo formVofindAll = new Rep03000FormVo();
		List<Rep03000Vo> dataTestList = new ArrayList<Rep03000Vo>();
	
		formVofindAll= findAll(formVo);
		
		dataTestList = formVofindAll.getRep03000VoList();
//		dataTestList = formVo.getDataT();
		
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
//			String[] tbTH1 = formVo.getTrHtml1();
			
			row = sheet.createRow(rowNum);
			cell = row.createCell(4);
			cell.setCellValue("รายงานภาษีขาย");
			cell.setCellStyle(fontHeader);
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 9)); //tr colspan=10
			rowNum++;
			row = sheet.createRow(rowNum);
			cell = row.createCell(3);
			cell.setCellValue("เดือนภาษี");
			cell.setCellStyle(fontHeader);
			
			cell = row.createCell(4);
			cell.setCellValue(EcerDateUtils.formatMMM_TH(EcerDateUtils.parseDateMMYYYYEN(formVo.getPaymentDate())));
//			cell.setCellStyle(fontHeader);
			
			cell = row.createCell(5);
			cell.setCellValue("ปี");
			cell.setCellStyle(fontHeader);
			
			cell = row.createCell(6);
			cell.setCellValue(EcerDateUtils.formatYYYY_TH(EcerDateUtils.parseDateMMYYYYEN(formVo.getPaymentDate())));
//			cell.setCellStyle(fontHeader);
			
			rowNum++;
		
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue("ชื่อผู้ประกอบการ");
			cell.setCellStyle(fontHeader);
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 2)); //tr colspan=3
			cell = row.createCell(1);
//			cell.setCellValue((dataTestList.size()==0)?"-":dataTestList.get(0).getCustomerName());
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 3, 9)); //tr colspan=7
			cell.setCellValue("บมจ.ธนาคารทหารไทย จำกัด (มหาชน)");

//			row = sheet.createRow(rowNum);
			cell = row.createCell(6);
			cell.setCellStyle(fontHeader);
			cell.setCellValue("เลขประจำตัวผู้เสียภาษีอากร");
//			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2)); //tr colspan=3
			cell = row.createCell(7);
//			cell.setCellValue((dataTestList.size()==0)?"-":dataTestList.get(0).getOrganizeId());
//			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 9)); //tr colspan=7
			cell.setCellValue("0107537000017");
			rowNum++;
			
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue("ชื่อสถานประกอบการ");
			cell.setCellStyle(fontHeader);
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 2)); //tr colspan=3
			cell = row.createCell(1);
			cell.setCellValue("บมจ.ธนาคารทหารไทย จำกัด (มหาชน)");
//			cell.setCellValue((dataTestList.size()==0)?"-":dataTestList.get(0).getCompanyName());
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 3, 9)); //tr colspan=7
		
			cell = row.createCell(6);
			cell.setCellValue("สำนักงานใหญ่/สาขา");
			cell.setCellStyle(fontHeader);
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 2)); //tr colspan=3
			cell = row.createCell(7);
			cell.setCellValue("00000");
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 3, 9)); //tr colspan=7
			rowNum++;
			
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue("ที่อยู่สถานประกอบการ");
			cell.setCellStyle(fontHeader);
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 2)); //tr colspan=3
			cell = row.createCell(1);
			cell.setCellValue("3000 ถนนพหลโยธิน แขวงจอมพล เขตจตุจักร กรุงเทพฯ 10900");
//			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 3, 9)); //tr colspan=7
			rowNum++;rowNum++;
			

/*			String[] tbTH1 = { "ลำดับ","ใบกำกับภาษี","","ชื่อผู้ซื้อสินค้า/ผู้รับบริการ","เลขประจำตัวผู้เสียภาษีอากรของผู้ซื้อสินค้า/ผู้รับบริการ","สถานประกอบการ","สำนักงานใหญ่/สาขา", "มูลค่าสินค้า/บริการ",
		             "จำนวนเงินภาษีมูลค่าเพิ่ม","จำนวนเงินรวม"};*/
			String[] tbTH1 = { "ลำดับ","ใบกำกับภาษี","","ชื่อผู้ซื้อสินค้า/ผู้รับบริการ","เลขประจำตัวผู้เสียภาษีอากร \n ของผู้ซื้อสินค้า/ผู้รับบริการ","สถานประกอบการ", "มูลค่าสินค้า/บริการ",
		             "จำนวนเงิน \n ภาษีมูลค่าเพิ่ม","จำนวนเงินรวม"};
			row = sheet.createRow(rowNum);
			for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
				cell = row.createCell(cellNum);
				cell.setCellValue(tbTH1[cellNum]);
				cell.setCellStyle(thStyle);
			};
			rowNum++;
			
//			String[] tbTH2 = formVo.getTrHtml2();
			String[] tbTH2 = { "เลขที่", "วันที่","","","สำนักงานใหญ่/สาขา","","",""};
			row = sheet.createRow(rowNum);
			int cellNumtbTH2 = 1;
			for (int i = 0; i < tbTH2.length; i++) {
				cell = row.createCell(cellNumtbTH2);
				cell.setCellValue(tbTH2[i]);
				cell.setCellStyle(thStyle);
				cellNumtbTH2++;
			};
			
			// merge(firstRow, lastRow, firstCol, lastCol)
			
			for (int i = 3; i<=9; i++) {
				if(i != 5) {
					sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum, i, i)); //tr1-9 rowspan=2
				}
			}
		
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum, 0, 0)); //tr1 rowspan=2
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1, 1, 2)); //tr colspan=2
	
			
			/* Detail */
//			List<LicenseList6010> exportDataList = null;
			rowNum++;
			cellNum = 0;
			int order = 1;

			Double sumVat = new Double(0);
			Double sumAmountTMBvat = new Double(0);
			Double sumAmountTMB = new Double(0);
			for (Rep03000Vo detail : dataTestList) {
				row = sheet.createRow(rowNum);
				// No.
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue(String.valueOf(order));
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getReceiptNo()))?detail.getReceiptNo(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getPaymentDate()))?detail.getPaymentDate(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft);cell.setCellValue((StringUtils.isNotBlank(detail.getCompanyName()))?detail.getCompanyName(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellTextCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getOrganizeId()))?detail.getOrganizeId(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getAddress()))?detail.getAddress(): "" );
//				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getBranch()))?detail.getBranch(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellRight);cell.setCellValue((StringUtils.isNotBlank(detail.getAmountTmbVat().toString()))?detail.getAmountTmbVat().toString(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellRight);cell.setCellValue((StringUtils.isNotBlank(detail.getAmountVat().toString()))?detail.getAmountVat().toString(): "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellRight);cell.setCellValue((StringUtils.isNotBlank(detail.getAmountTmb().toString()))?detail.getAmountTmb().toString(): "" );
				
				sumAmountTMB = sumAmountTMB + convertBigDecimalToLong(detail.getAmountTmb());
				sumVat = sumVat + convertBigDecimalToLong(detail.getAmountVat());
				sumAmountTMBvat = sumAmountTMBvat + + convertBigDecimalToLong(detail.getAmountTmbVat());
				
				rowNum++;
				order++;
				cellNum = 0;
			}
			
			row = sheet.createRow(rowNum);

			cell = row.createCell(0);
			cell.setCellValue("รวม" );
			cell.setCellStyle(excalService.cellRight);
			
			for (int i = 1; i <= 5; i++) {
				cell = row.createCell(i);
				cell.setCellValue("");
				cell.setCellStyle(excalService.cellRight);
			}
			
			cell = row.createCell(6);
			cell.setCellValue((new BigDecimal(sumAmountTMBvat).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
			cell.setCellStyle(excalService.cellRight);
			cell = row.createCell(7);
			cell.setCellValue((new BigDecimal(sumVat).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
			cell.setCellStyle(excalService.cellRight);
			cell = row.createCell(8);
			cell.setCellValue((new BigDecimal(sumAmountTMB).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
			cell.setCellStyle(excalService.cellRight);
			
			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 5));
			
			

			/*set	fileName*/		
			String fileName ="Output-VAT_Report_"+DateFormatUtils.format(new Date(),"yyyyMMdd");;
			log.info(fileName);
			
			/* write it as an excel attachment */
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			OutputStream outStream = response.getOutputStream();
			try {
				outByteStream = new ByteArrayOutputStream();
				workbook.write(outByteStream);
				byte [] outArray = outByteStream.toByteArray();
				response.setContentType("application/vnd.ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");
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
		accountNoReturn = accountNo.substring(0, 3)+"-"+accountNo.substring(3, 4)+"-"+accountNo.substring(4, 9)+"-"+accountNo.substring(9);
		return accountNoReturn;
	}
	 public Float convertBigDecimalToLong(BigDecimal bigdecimal) {
		 return (bigdecimal!=null)?bigdecimal.floatValue():0f;
	 }
	
}


