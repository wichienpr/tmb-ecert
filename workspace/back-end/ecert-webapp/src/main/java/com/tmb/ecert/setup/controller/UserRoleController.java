package com.tmb.ecert.setup.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.service.UserRoleService;
import com.tmb.ecert.setup.vo.Sup01010FormVo;
import com.tmb.ecert.setup.vo.Sup01010Vo;
import com.tmb.ecert.setup.vo.Sup01020Vo;


@RequestMapping("api/setup")
@RestController
public class UserRoleController {
	
	@Autowired
	private UserRoleService service;
	
	
	@PostMapping("/saveUserRole")
	@ResponseBody
	public CommonMessage<String> save(@RequestBody Sup01010FormVo form) {
//		CommonMessage<String> message = new CommonMessage<>();
//		return message;
		return service.saveUserRole(form);
	}
	
	@PostMapping("/getRole")
	@ResponseBody
	public List<RoleVo> getRole(@RequestBody Sup01010FormVo form) {
//		System.out.println("save medthod");
		return service.getRole(form);
	}
	
	@GetMapping("/getRolePermission/{roleID}")
	@ResponseBody
	public List<Sup01010Vo> getRolePermissionByRoleID1(@PathVariable("roleID") Long roleID ,HttpServletResponse response) {
		return service.getRolePermissionByRoleID(roleID);
	}
	
	@PostMapping("/exportRole")
	@ResponseBody
	public void exportRole(@RequestBody Sup01010FormVo form,HttpServletResponse response) {
//		System.out.println("save medthod");
		try {
			service.exportFile(form,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
