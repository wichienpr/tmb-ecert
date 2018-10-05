package com.tmb.ecert.setup.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.common.service.ExcalService;
import com.tmb.ecert.report.persistence.vo.Rep03000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep03000Vo;
import com.tmb.ecert.setup.dao.UserRoleDao;
import com.tmb.ecert.setup.vo.Sup01010FormVo;
import com.tmb.ecert.setup.vo.Sup01010Vo;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private ExcalService excalService;

	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_ROLEMANAGEMENT);

	public CommonMessage<String> saveUserRole(Sup01010FormVo form) {
		CommonMessage<String> message = new CommonMessage<>();
		Long idRole = 0L;

		try {
			UserDetails user = UserLoginUtils.getCurrentUserLogin();
			String fullName = user.getFirstName() + " " + user.getLastName();
			List<Sup01010Vo> permissionList = new ArrayList<>();
			if (form.getRoleId() == null || form.getRoleId() == 0) {
				idRole = userRoleDao.createUserRole(form, fullName, user.getUserId());
			} else {
				userRoleDao.updateRolePermissByRoleID(form, fullName, user.getUserId());
				int deleterow = userRoleDao.delectRolePermissByRoleID(form.getRoleId());
				idRole = form.getRoleId();
			}
			for (RoleVo roleVo : form.getRolePermission()) {
				Sup01010Vo vo = new Sup01010Vo();
				vo.setRoleId(idRole);
				vo.setFunctionCode(roleVo.getFunctionCode());
				vo.setStatus(roleVo.getStatus());

				permissionList.add(vo);
			}
			userRoleDao.createRolePermission(permissionList, "admin", "001");
			System.out.println("success get key " + idRole);
			message.setMessage("success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return message;
	}

	public List<RoleVo> getRole(Sup01010FormVo form) {
		return userRoleDao.getRole(form);
	}

	public List<Sup01010Vo> getRolePermissionByRoleID(Long roleID) {

		List<Sup01010Vo> permissionRole = userRoleDao.getListPermissionByRoleID(roleID);
//		List<Sup01020Vo> listPermission  = generateCostanListPermission(permissionRole);
		return permissionRole;

	}

	public void exportFile(Sup01010FormVo form, HttpServletResponse response) throws IOException {

		List<RoleVo> listRole = new ArrayList<RoleVo>();

		listRole = getRole(form);

		XSSFWorkbook workbook = excalService.setUpExcel();
		CellStyle thStyle = excalService.thStyle;
		Sheet sheet = workbook.createSheet();
		int rowNum = 0;
		int cellNum = 0;
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(cellNum);
		logger.info("Creating excel");
		
		String[] tbTH1           = {   "ลำดับ", "Role Name", "สถานะ" };
 
		for (cellNum = 0; cellNum < tbTH1.length; cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(tbTH1[cellNum]);
			cell.setCellStyle(thStyle);
		}
		;

		rowNum = 1;
		cellNum = 0;
		int order = 1;
		for (RoleVo detail : listRole) {
			row = sheet.createRow(rowNum);
			
			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(rowNum));

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(detail.getRoleName());

			cell = row.createCell(cellNum++);
			cell.setCellStyle(excalService.cellCenter);
			cell.setCellValue(String.valueOf(detail.getStatus()));

			rowNum++;
			order++;
			cellNum = 0;
		}

		String fileName = "List_Role_" + DateFormatUtils.format(new Date(), "yyyyMMdd");
		;
		logger.info(fileName);

		/* write it as an excel attachment */
//		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//		workbook.write(outByteStream);
//		byte[] outArray = outByteStream.toByteArray();
//		response.setContentType("application/vnd.ms-excel");
//		response.setContentLength(outArray.length);
//		response.setHeader("Expires:", "0"); // eliminates browser caching
//		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
//		OutputStream outStream = response.getOutputStream();
//		outStream.write(outArray);
//		outStream.flush();
//		outStream.close();
//		workbook.close();
		
		FileOutputStream fileOut = new FileOutputStream(fileName + ".xlsx");
		workbook.write(fileOut);

		fileOut.close();

		// Closing the workbook
		workbook.close();

		logger.info("Done {}", fileOut.getClass().getComponentType());
	}

}
