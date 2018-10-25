package com.tmb.ecert.setup.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.common.service.ExcalService;
import com.tmb.ecert.report.persistence.vo.Rep02000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02000Vo;
import com.tmb.ecert.setup.dao.UserRoleDao;
import com.tmb.ecert.setup.vo.Sup01000FormVo;
import com.tmb.ecert.setup.vo.Sup01100FormVo;
import com.tmb.ecert.setup.vo.Sup01100Vo;
import com.tmb.ecert.setup.vo.Sup03000Vo;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;
import th.co.baiwa.buckwaframework.preferences.constant.MessageConstants.MESSAGE_STATUS;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class Sup01000Service {

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private ExcalService excalService;
	
	@Value("${app.datasource.path.report}")
	private String PATH_EXPORT;
	

	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_ROLEMANAGEMENT);

	private static String EXCEL_FILE_XLSX = "xlsx";
	private static String EXCEL_FILE_ACTIVE = "Active";
	private static String EXCEL_FILE_INACTIVE = "Inactive";
	private static String EXCEL_FILE_YES= "Yes";
	private static String EXCEL_FILE_NO= "No";
	private static String EXCEL_FILE_TEMPALTE_NAME = "RolePermission_Template"; 
	private static String EXCEL_FILE_EXPORT_NAME = "RolePermission_"; 
	private static String EXCEL_DATE_FORMAT =  "yyyyMMdd";
	private static String EXCEL_REPORT = "report/excel_template/";
	private static String EXCEL_TEMPALTE = "RolePermission_Template.xlsx";
	

	private static String[] headerTable = { "Role Name ", " สถานะ ", "ยินดีต้อนรับ \n (UI-00002)",
			"ตรวจสอบสถานะคำขอ  \n (UI-00003)", "", "รายละเอียดบันทึกคำขอและพิมพ์แบบฟอร์มให้ลูกค้าลงนาม (UI-00004)", "",
			"", "", "", "", "", "", "", "", "", " Request Form \n สำหรับลูกค้าทำรายการเอง \n (UI-00005)",
			"Request Form \n สำหรับทำรายการให้ลูกค้าลงนาม \n (UI-00006)",
			"รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล \n ( e-Certificate ) : End day(UI-00007)", "",
			"รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล \n  ( e-Certificate ) : Monthly(UI-00008)", "",
			"รายงาน Output VAT(UI-00009)", "", "Batch Monitoring(UI-00011)", "", "Audit Log(UI-00012)", "",
			" Role Manament(UI-00013)", "", "", "", "", " Parameter Configuration \n (UI-00014)", "",
			" Email Configuration \n (UI-00015)", ""," บันทึกข้อมูลจากเลขที่คำขอ (TMB Req No.) \n (UI-00016)" };

	private static String[] headerTableSub = { "", "", "", "เมนู", "แสดงรายละเอียด", "เมนู", "ดำเนินการชำระเงิน",
			"อนุมัติการชำระเงิน", "ปฏิเสธ", "พิมพ์ใบเสร็จ", "พิมพ์ Cover Sheet", "Upload Certificate",
			"ดาวน์โหลดใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก",
			"ดาวน์โหลดสำเนาบัตรประชาชน", "ดาวน์โหลดสำเนาใบเปลี่ยนชื่อหรือนามสกุล", "	ดาวน์โหลดเอกสาร Certificate",
			"", "", "เมนู", "	Export to Excel", "เมนู", "Export to Excel", "เมนู", "	Export to Excel", "เมนู",
			"Rerun", "เมนู", "Export to Excel", "เมนู", "เพิ่ม Role", "แก้ไข	", "Import Role", "Export Role", "เมนู",
			"บันทึก", "เมนู", "แก้ไข" , ""};
	
	private static String[] arrRolePermission = { 
			"0000200", "0000300", "0000301", "0000400", "0000401", 
			 "0000402", "0000403", "0000404", "0000405", "0000406", 
			 "0000407", "0000408", "0000409", "0000410", "0000500", 
			 "0000600", "0000700", "0000701", "0000800", "0000801", 
			 "0000900", "0000901", "0001100", "0001101", "0001200", 
			 "0001201", "0001300", "0001301", "0001302", "0001303", 
			 "0001304", "0001400", "0001401", "0001500", "0001501", "0001600"  };

	
	public CommonMessage<String> saveUserRole(Sup01100FormVo form) {
		CommonMessage<String> message = new CommonMessage<>();
		Long idRole = 0L;

		try {
			UserDetails user = UserLoginUtils.getCurrentUserLogin();
			String fullName = user.getFirstName() + " " + user.getLastName();
			List<Sup01100Vo> permissionList = new ArrayList<>();
			int countDup = userRoleDao.validateDuplicateRoleName(form);
			if (form.getRoleId() == null || form.getRoleId() == 0) {
				if (countDup == 0) {
					idRole = userRoleDao.createUserRole(form, fullName, user.getUserId());
				} else {
					message.setData(MESSAGE_STATUS.FAILED);
					message.setMessage(MESSAGE_STATUS.FAILED);
					return message;
				}
			} else {
				userRoleDao.updateRolePermissByRoleID(form, fullName, user.getUserId());
				int deleterow = userRoleDao.delectRolePermissByRoleID(form.getRoleId());
				idRole = form.getRoleId();
			}
			for (RoleVo roleVo : form.getRolePermission()) {
				Sup01100Vo vo = new Sup01100Vo();
				vo.setRoleId(idRole);
				vo.setFunctionCode(roleVo.getFunctionCode());
				vo.setStatus(roleVo.getStatus());

				permissionList.add(vo);
			}

			userRoleDao.createRolePermission(permissionList, fullName, user.getUserId());
			message.setData(MESSAGE_STATUS.SUCCEED);
			message.setMessage(MESSAGE_STATUS.SUCCEED);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			message.setData(MESSAGE_STATUS.FAILED);
			message.setMessage(MESSAGE_STATUS.FAILED);
			return message;
		}

	}

	public DataTableResponse<RoleVo> getRole(Sup01100FormVo form) {
		if (StatusConstant.ROLE_STATUS.STATUS_ALL.equals(Integer.toString(form.getStatus())) || form.getStatus() == 0 ) {
			form.setStatus(2);
			
		}else if (StatusConstant.ROLE_STATUS.STATUS_INACTIVE.equals(Integer.toString(form.getStatus()))) {
			form.setStatus(1);
			
		}else if (StatusConstant.ROLE_STATUS.STATUS_ACTIVE.equals(Integer.toString(form.getStatus()))) {
			form.setStatus(0);
		}
		
		DataTableResponse<RoleVo> list  = new DataTableResponse<>();
		List<RoleVo> sup01000Vo = userRoleDao.getRole(form);
		list.setData(sup01000Vo);
		return list;

	}

	public List<Sup01100Vo> getRolePermissionByRoleID(Long roleID) {

		List<Sup01100Vo> permissionRole = userRoleDao.getListPermissionByRoleID(roleID);
//		List<Sup01020Vo> listPermission  = generateCostanListPermission(permissionRole);
		return permissionRole;

	}

	public void exportFile(Sup01100FormVo form, HttpServletResponse response) throws IOException {

		if(form.getRoleName().equals("NULL")) {
			form.setRoleName(null);
		}
		
		if (StatusConstant.ROLE_STATUS.STATUS_ALL.equals(Integer.toString(form.getStatus()))) {
			form.setStatus(2);
			
		}else if (StatusConstant.ROLE_STATUS.STATUS_INACTIVE.equals(Integer.toString(form.getStatus()))) {
			form.setStatus(1);
			
		}else if (StatusConstant.ROLE_STATUS.STATUS_ACTIVE.equals(Integer.toString(form.getStatus()))) {
			form.setStatus(0);
		}
		
		List<RoleVo> roleList = userRoleDao.getRole(form);
		
		XSSFWorkbook workbook = excalService.setUpExcel();
		CellStyle thStyle = excalService.bgLightBule;
		CellStyle headerBlue = excalService.bgBule;
		Sheet sheet = workbook.createSheet();
		int rowNum = 0;
		int cellNum = 0;
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(cellNum);
		row.setHeight((short) 0x249);
		

		for (cellNum = 0; cellNum < headerTable.length; cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(headerTable[cellNum]);
			cell.setCellStyle(headerBlue);
		};

		row = sheet.createRow(1);
		row.setHeight((short) 0x249);
		int cellNumtbTH2 = 0;
		for (int i = 0; i < headerTableSub.length; i++) {
			cell = row.createCell(cellNumtbTH2);
			cell.setCellValue(headerTableSub[i]);
			cell.setCellStyle(thStyle);
			cellNumtbTH2++;
		}
		;
//		Merge header 
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 15));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 16, 16));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 17, 17));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 19));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 21));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 22, 23));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 24, 25));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 26, 27));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 28, 32));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 33, 34));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 35, 36));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 37, 37));

		List<Sup01100Vo> permissionRole;
		rowNum = 2;
		cellNum = 0;
		int order = 1;
		for (RoleVo detail : roleList) {
			row = sheet.createRow(rowNum);
			DataValidationHelper dvHelper = sheet.getDataValidationHelper();
			permissionRole = userRoleDao.getListPermissionByRoleID(detail.getRoldId());

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(detail.getRoleName());

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(detail.getStatus() == 0 ? "Active" : "Inactive");

			String[] arrDropdown = new String[] { "Active", "Inactive" };
			DataValidation validation = createDroupDown(rowNum , rowNum , cellNum - 1, cellNum - 1, arrDropdown,
					dvHelper);
			sheet.addValidationData(validation);

//			for (Sup01100Vo permissionVo : permissionRole) {
//
//				cell = row.createCell(cellNum++);
//				cell.setCellStyle(excalService.cellCenter);
//				cell.setCellValue(permissionVo.getStatus() == 0 ? "Yes" : "No");
//
//				String[] arrDropdownPerm = new String[] { "Yes", "No" };
//				DataValidation validationPerm = createDroupDown(rowNum, rowNum, cellNum - 1, cellNum - 1,
//						arrDropdownPerm, dvHelper);
//				sheet.addValidationData(validationPerm);
//
//			}
			for (int i = 0; i <arrRolePermission.length; i++) {
				cell = row.createCell(cellNum++);
				cell.setCellStyle(excalService.cellCenter);
				if (permissionRole.size() > i) {
					cell.setCellValue(permissionRole.get(i).getStatus() == 0 ? "Yes" : "No");
				}else {
					cell.setCellValue("Yes");
				}
				String[] arrDropdownPerm = new String[] { "Yes", "No" };
				DataValidation validationPerm = createDroupDown(rowNum, rowNum, cellNum - 1, cellNum - 1,
						arrDropdownPerm, dvHelper);
				sheet.addValidationData(validationPerm);
			}

			rowNum++;
			order++;
			cellNum = 0;
		}

		String fileName = EXCEL_FILE_EXPORT_NAME+DateFormatUtils.format(new Date(),EXCEL_DATE_FORMAT);
		logger.info(fileName);

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

		logger.info("create excel success file name :"+fileName);
	}

	public DataValidation createDroupDown(int startRow, int endRow, int startCol, int endCol, String[] arrDropdown,
			DataValidationHelper dvHelper) {
		CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, startCol, endCol);
		DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(arrDropdown);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		return validation;
	}

	public void ExportTemplate(HttpServletResponse response) throws IOException {

		XSSFWorkbook workbook = excalService.setUpExcel();
		CellStyle thStyle = excalService.bgLightBule;
		CellStyle headerBlue = excalService.bgBule;

		Sheet sheet = workbook.createSheet();
		int rowNum = 0;
		int cellNum = 0;
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(cellNum);
		row.setHeight((short) 0x249);

		for (cellNum = 0; cellNum < headerTable.length; cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(headerTable[cellNum]);
			cell.setCellStyle(headerBlue);

		}
		;

		row = sheet.createRow(1);
		row.setHeight((short) 0x249);
		int cellNumtbTH2 = 0;
		for (int i = 0; i < headerTableSub.length; i++) {
			cell = row.createCell(cellNumtbTH2);
			cell.setCellValue(headerTableSub[i]);
			cell.setCellStyle(thStyle);
			cellNumtbTH2++;
		}
		;

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 15));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 16, 16));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 17, 17));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 19));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 21));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 22, 23));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 24, 25));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 26, 27));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 28, 32));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 33, 34));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 35, 36));

		List<Sup01100Vo> permissionRole;
		rowNum = 2;
		cellNum = 0;
		int order = 1;
		for (int i = 0; i < 1; i++) {
			row = sheet.createRow(rowNum);
			DataValidationHelper dvHelper = sheet.getDataValidationHelper();

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue("XXXX");

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue("Active");

			String[] arrDropdown = new String[] { "Active", "Inactive" };
			DataValidation validation = createDroupDown(rowNum , rowNum , cellNum - 1, cellNum - 1, arrDropdown,
					dvHelper);
			sheet.addValidationData(validation);

			for (int j = 0; j < 35; j++) {

				cell = row.createCell(cellNum++);
				cell.setCellStyle(excalService.cellCenter);
				cell.setCellValue("Yes");

				String[] arrDropdownPerm = new String[] { "Yes", "No" };
				DataValidation validationPerm = createDroupDown(rowNum, rowNum, cellNum - 1, cellNum - 1,
						arrDropdownPerm, dvHelper);
				sheet.addValidationData(validationPerm);

			}

			rowNum++;
			order++;
			cellNum = 0;
		}

		String fileName = EXCEL_FILE_TEMPALTE_NAME;

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
		logger.info("create template excel success file name :"+fileName);

	}

	public CommonMessage<String> uploadFileRole(Sup01000FormVo file) {
		CommonMessage<String> message = new CommonMessage<>();
		if (!file.getFileUpload().isEmpty()) {
			String nameFile = FilenameUtils.getExtension(file.getFileUpload().getOriginalFilename());
			if (EXCEL_FILE_XLSX.equals(nameFile)) {
				try {	
					List<Sup01100FormVo> listRolePermission = new ArrayList<>();

					byte[] bytes = file.getFileUpload().getBytes();
					InputStream myInputStream = new ByteArrayInputStream(bytes); 
					XSSFWorkbook workbook = new XSSFWorkbook(myInputStream);
					Sheet datatypeSheet = workbook.getSheetAt(0);
					
					for (int i = 2; i <=  datatypeSheet.getLastRowNum() ; i++) {
						Row currentRow = datatypeSheet.getRow(i);
						Sup01100FormVo vo = new Sup01100FormVo();
						Cell currentCell = currentRow.getCell(0);
						vo.setRoleName(currentCell.getStringCellValue());
						currentCell = currentRow.getCell(1);
						int statusFlag = covertStatus(currentCell.getStringCellValue());
						if (statusFlag == 2) {
							logger.error("uploadFileRole","Upload Role Permission format error");
							message.setData(MESSAGE_STATUS.FAILED);
							message.setMessage(MESSAGE_STATUS.FAILED);
							return message;
						}else {
							vo.setStatus(statusFlag);
						}

						
						List<RoleVo> listRole = new ArrayList<>();
						for (int j = 2; j < currentRow.getLastCellNum() ; j++) {
							RoleVo roleVo = new RoleVo();
							currentCell = currentRow.getCell(j);
//							check format value
							int statusPermisFlag = covertStatus(currentCell.getStringCellValue());
							if (statusPermisFlag == 2) {
								logger.error("uploadFileRole","Upload Role Permission format error");
								message.setData(MESSAGE_STATUS.FAILED);
								message.setMessage(MESSAGE_STATUS.FAILED);
								return message;
							}else {
								roleVo.setStatus(statusPermisFlag);
							}
//							roleVo.setStatus(covertStatus(currentCell.getStringCellValue()));
							roleVo.setFunctionCode(arrRolePermission[j-2]);
							listRole.add(roleVo);
						}
						vo.setRolePermission(listRole);
						listRolePermission.add(vo);
					}
					
					//check duplicate role name in list
					for (int i = 0; i < listRolePermission.size(); i++) {
						if(listRolePermission.get(i).getRoleName().isEmpty()) {
							logger.error("uploadFileRole","Upload Role Permission role name is blank.");
							message.setData(MESSAGE_STATUS.FAILED);
							message.setMessage(MESSAGE_STATUS.FAILED);
							return message;
							
						}else {
							for (int j = 1; j < listRolePermission.size()-i; j++) {
								String roleName = listRolePermission.get(j+i).getRoleName();

								if(listRolePermission.get(i).getRoleName().equals(roleName)) {
									logger.error("uploadFileRole","Upload Role Permission role name is duplicate.");
									message.setData(MESSAGE_STATUS.FAILED);
									message.setMessage(MESSAGE_STATUS.FAILED);
									return message;
								}
							}
						}

					}
					logger.info("uploadFileRole","upload excel file success");
					saveListRolePermission(listRolePermission);
					message.setData(MESSAGE_STATUS.SUCCEED);
					message.setMessage(MESSAGE_STATUS.SUCCEED);
					return message;

				} catch (Exception e) {
					logger.info("uploadFileRole","upload excel file fail \n ");
					e.printStackTrace();
					message.setData(MESSAGE_STATUS.FAILED);
					message.setMessage(MESSAGE_STATUS.FAILED);
					return message;

				}
			}else {
				logger.info("uploadFileRole","upload excel file format not xlsx");
				message.setData(MESSAGE_STATUS.FAILED);
				message.setMessage(MESSAGE_STATUS.FAILED);
				return message;
			}

		} else {
			logger.info("uploadFileRole","upload excel file is empty");
			message.setData(MESSAGE_STATUS.FAILED);
			message.setMessage(MESSAGE_STATUS.FAILED);
			return message;
		}


	}
	
	public void saveListRolePermission(List<Sup01100FormVo> listRolePermission) {
		Long idRole = 0L;
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();
		List<Sup01100Vo> permissionList = new ArrayList<>();
		try {
			for (Sup01100FormVo rolePermiss : listRolePermission) {

				int countDup = userRoleDao.validateDuplicateRoleName(rolePermiss);
				if (countDup == 0) {
					idRole = userRoleDao.createUserRole(rolePermiss, fullName, user.getUserId());

				} else {
					idRole = userRoleDao.getRoleInfo(rolePermiss).get(0).getRoldId();
					userRoleDao.updateRolePermissByRoleID(rolePermiss, fullName, user.getUserId());
					int deleterow = userRoleDao.delectRolePermissByRoleID(idRole);
					
				}
				for (RoleVo roleVo : rolePermiss.getRolePermission()) {
					Sup01100Vo vo = new Sup01100Vo();
					vo.setRoleId(idRole);
					vo.setFunctionCode(roleVo.getFunctionCode());
					vo.setStatus(roleVo.getStatus());
					permissionList.add(vo);
				}

				userRoleDao.createRolePermission(permissionList, fullName, user.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	public void ExportRoleTemplate(HttpServletResponse response) throws IOException {
		ClassPathResource res = new ClassPathResource("report/excel_template/RolePermission_Template.xlsx");   
		File file = res.getFile();
		byte[] reportFile;
		OutputStream responseOutputStream = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			reportFile = IOUtils.toByteArray(inputStream);
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + EXCEL_TEMPALTE );
			response.setContentLength(reportFile.length);
			responseOutputStream = response.getOutputStream();
			responseOutputStream.write(reportFile);
			responseOutputStream.close();
			
		} catch (Exception e) {
			logger.error("ExportRoleTemplate",e);
		}finally {
			responseOutputStream.close();
			inputStream.close();
		}

		
	}
	public int covertStatus(String cellVal) {
		int val = 2 ;
		
		if (EXCEL_FILE_ACTIVE.equals(cellVal)) {
			val = 0;
		}else if(EXCEL_FILE_INACTIVE.equals(cellVal)) {
			val = 1;
		}else if (EXCEL_FILE_YES.equals(cellVal)) {
			val = 0;
		}else if (EXCEL_FILE_NO.equals(cellVal)) {
			val = 1;
		}
		return val;
		
	}
	

}
