package com.tmb.ecert.common.service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcalService {

	private static final Logger log = LoggerFactory.getLogger(ExcalService.class);
	public CellStyle thStyle;
	public CellStyle cellCenter;
	public CellStyle cellRight;
	public CellStyle cellLeft;
	public CellStyle bgRed;
	public CellStyle bgYellow;
	public CellStyle bgGreen;
	public CellStyle topCenter;
	public CellStyle topRight;
	public CellStyle topLeft;
	public Font fontHeader;
	
	public XSSFWorkbook setUpExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		thStyle = workbook.createCellStyle();
		thStyle.setAlignment(HorizontalAlignment.CENTER);
		thStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		thStyle.setBorderBottom(BorderStyle.THIN);
		thStyle.setBorderLeft(BorderStyle.THIN);
		thStyle.setBorderRight(BorderStyle.THIN);
		thStyle.setBorderTop(BorderStyle.THIN);
		thStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		thStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellCenter = workbook.createCellStyle();
		cellCenter.setAlignment(HorizontalAlignment.CENTER);
		cellCenter.setBorderBottom(BorderStyle.THIN);
		cellCenter.setBorderLeft(BorderStyle.THIN);
		cellCenter.setBorderRight(BorderStyle.THIN);
		cellCenter.setBorderTop(BorderStyle.THIN);

		cellRight = workbook.createCellStyle();
		cellRight.setAlignment(HorizontalAlignment.RIGHT);
		cellRight.setBorderBottom(BorderStyle.THIN);
		cellRight.setBorderLeft(BorderStyle.THIN);
		cellRight.setBorderRight(BorderStyle.THIN);
		cellRight.setBorderTop(BorderStyle.THIN);

		cellLeft = workbook.createCellStyle();
		cellLeft.setAlignment(HorizontalAlignment.LEFT);
		cellLeft.setBorderBottom(BorderStyle.THIN);
		cellLeft.setBorderLeft(BorderStyle.THIN);
		cellLeft.setBorderRight(BorderStyle.THIN);
		cellLeft.setBorderTop(BorderStyle.THIN);

		bgRed = workbook.createCellStyle();
		bgRed.setAlignment(HorizontalAlignment.CENTER);
		bgRed.setBorderBottom(BorderStyle.THIN);
		bgRed.setBorderLeft(BorderStyle.THIN);
		bgRed.setBorderRight(BorderStyle.THIN);
		bgRed.setBorderTop(BorderStyle.THIN);
		bgRed.setFillForegroundColor(IndexedColors.RED.getIndex());
		bgRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		bgYellow = workbook.createCellStyle();
		bgYellow.setAlignment(HorizontalAlignment.CENTER);
		bgYellow.setBorderBottom(BorderStyle.THIN);
		bgYellow.setBorderLeft(BorderStyle.THIN);
		bgYellow.setBorderRight(BorderStyle.THIN);
		bgYellow.setBorderTop(BorderStyle.THIN);
		bgYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		bgYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		bgGreen = workbook.createCellStyle();
		bgGreen.setAlignment(HorizontalAlignment.CENTER);
		bgGreen.setBorderBottom(BorderStyle.THIN);
		bgGreen.setBorderLeft(BorderStyle.THIN);
		bgGreen.setBorderRight(BorderStyle.THIN);
		bgGreen.setBorderTop(BorderStyle.THIN);
		bgGreen.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		bgGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		fontHeader = workbook.createFont();
		fontHeader.setBold(true);

		topCenter = workbook.createCellStyle();
		topCenter.setAlignment(HorizontalAlignment.CENTER);
		topCenter.setFont(fontHeader);

		topRight = workbook.createCellStyle();
		topRight.setAlignment(HorizontalAlignment.RIGHT);

		topLeft = workbook.createCellStyle();
		topLeft.setAlignment(HorizontalAlignment.LEFT);
		return workbook;
	}

}
