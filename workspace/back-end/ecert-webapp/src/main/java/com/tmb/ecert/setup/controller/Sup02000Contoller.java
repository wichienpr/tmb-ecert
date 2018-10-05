package com.tmb.ecert.setup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.service.Sup02000Service;
import com.tmb.ecert.setup.vo.Sup01100FormVo;

@RequestMapping("api/setup/sup02000")
@RestController
public class Sup02000Contoller {
	
	@Autowired
	private Sup02000Service service;
	
	@GetMapping("/getParameter")
	@ResponseBody
	public List<ParameterConfig> getParameter() {
//		System.out.println("save medthod");
		return service.getParmeter();
	}

}
