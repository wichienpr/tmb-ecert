package com.tmb.ecert.setup.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.service.Sup01000Service;
import com.tmb.ecert.setup.vo.Sup01000FormVo;
import com.tmb.ecert.setup.vo.Sup01100FormVo;
import com.tmb.ecert.setup.vo.Sup01100Vo;
import com.tmb.ecert.setup.vo.Sup01101Vo;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;


@RequestMapping("api/setup/sup01000")
@Controller
public class Sup01000Controller {
	
	@Autowired
	private Sup01000Service service;
	
	
	@PostMapping("/saveUserRole")
	@ResponseBody
	public CommonMessage<String> save(@RequestBody Sup01100FormVo form) {
		return service.saveUserRole(form);
	}
	
	@PostMapping("/getRole")
	@ResponseBody
	public DataTableResponse<RoleVo> getRole(@RequestBody Sup01100FormVo form) {
		return service.getRole(form);
	}
	
	@GetMapping("/getRolePermission/{roleID}")
	@ResponseBody
	public List<Sup01100Vo> getRolePermissionByRoleID1(@PathVariable("roleID") Long roleID ,HttpServletResponse response) {
		return service.getRolePermissionByRoleID(roleID);
	}
	
	@GetMapping("/exportRole/{roleName}/{roleStatus}")
	@ResponseBody
	public void exportRole(@PathVariable("roleName") String roleName,@PathVariable("roleStatus") String roleStatus ,HttpServletResponse response) {
		Sup01100FormVo roleVo = new Sup01100FormVo();

		try {
			roleVo.setRoleName(roleName);
			if ("NULL".equals(roleStatus)) {
				roleVo.setStatus(2);
			}else {
				roleVo.setStatus(Integer.parseInt(roleStatus));
			}
			service.exportFile(roleVo,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/exportTemplate")
	@ResponseBody
	public void exportRole(HttpServletResponse response) {
		try {
			service.ExportRoleTemplate(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/uploadFile")
	@ResponseBody
	public CommonMessage<String> uploadFileHandler(@ModelAttribute Sup01000FormVo file) {

		return service.uploadFileRole(file);
	}
	

}
