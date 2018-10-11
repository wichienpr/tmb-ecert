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
import com.tmb.ecert.report.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep01000Vo;
import com.tmb.ecert.report.persistence.vo.Rep02000Vo;

@Service
public class Rep01000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RepDao repDao;
	
	@Autowired
	private ExcalService excalService;
	
	
	public List<Rep01000Vo> findAll(Rep01000FormVo formVo){
		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();
		List<Rep01000Vo> requestTypeList = new ArrayList<Rep01000Vo>();
		List<Rep01000Vo> rep01000VoListReturn = new ArrayList<Rep01000Vo>();
		rep01000VoList = repDao.getDataRep01000(formVo);

		for (Rep01000Vo rep01000Vo : rep01000VoList) {
			
			requestTypeList = repDao.getRequestTypeRep01000(rep01000Vo.getId());
			String requestTypeString = "";
			String requestTypeStringExcel = "";
			for (Rep01000Vo requestType : requestTypeList) {
				requestTypeString += (StringUtils.isNoneBlank(requestTypeString))?","+requestType.getRequestTypeDesc():requestType.getRequestTypeDesc();
				requestTypeStringExcel += (StringUtils.isNoneBlank(requestTypeStringExcel))?","+requestType.getRequestTypeDesc():requestType.getRequestTypeDesc();
			}
			rep01000Vo.setRequestTypeDesc(requestTypeString);
			rep01000Vo.setRequestTypeExcel(requestTypeStringExcel);
			rep01000VoListReturn.add(rep01000Vo);
			
		}
		return rep01000VoListReturn;
	}
	
	public void exportFile(Rep01000FormVo formVo, HttpServletResponse response) throws IOException {
		
		List<Rep01000Vo> dataTestList = new ArrayList<Rep01000Vo>();
		dataTestList =findAll(formVo);
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
			
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue("รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : End day");
			cell.setCellStyle(fontHeader);
			sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum, 0, 19)); //tr colspan=20
			rowNum+=2;
			
//			String[] tbTH1 = formVo.getTrHtml1();
			
			String[] tbTH1 = { "ลำดับ","วันที่","เลขที่อ้างอิง (TMB Req No.)","เลขที่นิติบุคคล", "ชื่อ",
		             "Segment", "ประเภทคำขอ","รายละเอียดคำขอ","เลขที่บัญชี","จำนวนเงิน : บาท","","",
		             "รวม","ประเภทการชำระเงิน","Maker ID","Maker Name","Checker ID","Checker Name","สถานะ","หมายเหตุ"};
			row = sheet.createRow(rowNum++);
			for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
				cell = row.createCell(cellNum);
				cell.setCellValue(tbTH1[cellNum]);
				cell.setCellStyle(thStyle);
			};
			
			
//			String[] tbTH2 = formVo.getTrHtml2();
			String[] tbTH2 = { "DBD", "TMB","Tax"};
			row = sheet.createRow(rowNum);
			int cellNumtbTH2 = 9;
			for (int i = 0; i < tbTH2.length; i++) {
				cell = row.createCell(cellNumtbTH2);
				cell.setCellValue(tbTH2[i]);
				cell.setCellStyle(thStyle);
				cellNumtbTH2++;
			};
			
			// merge(firstRow, lastRow, firstCol, lastCol)
			
			for (int i = 0; i<=8; i++) {
				sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum, i, i)); //tr1-9 rowspan=2
			}
			for (int i = 12; i<=19; i++) {
				sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum, i, i)); //tr13-18 rowspan=2
			}
			
			
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 9, 11)); //tr colspan=3
			
			/* Detail */
//			List<LicenseList6010> exportDataList = null;
			rowNum++;
			cellNum = 0;
			int order = 1;
			for (Rep01000Vo detail : dataTestList) {
				row = sheet.createRow(rowNum);
				// No.
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue(order);
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getRequestDate()                 ))?detail.getRequestDate()                 : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getTmbRequestno()                ))?detail.getTmbRequestno()                : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getOrganizeId()                  ))?detail.getOrganizeId()                  : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getCompanyName()                 ))?detail.getCompanyName()                 : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getCustsegmentDesc()             ))?detail.getCustsegmentDesc()             : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getCertypeDesc()                 ))?detail.getCertypeDesc()                 : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getRequestTypeExcel()            ))?detail.getRequestTypeExcel()            : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(convertAccountNo(detail.getAccountNo()) ))?convertAccountNo(detail.getAccountNo()) : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getAmountDbd().toString()        ))?detail.getAmountDbd().toString()        : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getAmountTmb().toString()        ))?detail.getAmountTmb().toString()        : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getTotalAmountVat().toString()   ))?detail.getTotalAmountVat().toString()   : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getAmount().toString()           ))?detail.getAmount().toString()           : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getPaidtypeDesc()                ))?detail.getPaidtypeDesc()                : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getMakerById()                   ))?detail.getMakerById()                   : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getMakerByName()                 ))?detail.getMakerByName()                 : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue((StringUtils.isNotBlank(detail.getCheckerById()                 ))?detail.getCheckerById()                 : "" );
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getCheckerByName()               ))?detail.getCheckerByName()               : "" );
				String status = "";
				if(StringUtils.isNotBlank(detail.getStatus())&&(detail.getStatus().equals("10009") || detail.getStatus().equals("10010") )) {
					status = "สำเร็จ";
				}else {
					status = "ไม่สำเร็จ";
				}
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellCenter);cell.setCellValue(status);
				cell = row.createCell(cellNum++);cell.setCellStyle(excalService.cellLeft  );cell.setCellValue((StringUtils.isNotBlank(detail.getRemark()                      ))?detail.getRemark()                      : "" );
				order++;
				rowNum++;
				cellNum = 0;
			}
			
			
			
			/*set	fileName*/		
			String fileName ="End-day_Report_"+DateFormatUtils.format(new Date(),"yyyyMMdd");;
			log.info(fileName);
			
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
	
	public String convertAccountNo(String accountNo) {
		String accountNoReturn = "";
		accountNoReturn = accountNo.substring(0, 3)+"-"+accountNo.substring(3, 4)+"-"+accountNo.substring(4, 9)+"-"+accountNo.substring(9);
		return accountNoReturn;
	}
	
}

