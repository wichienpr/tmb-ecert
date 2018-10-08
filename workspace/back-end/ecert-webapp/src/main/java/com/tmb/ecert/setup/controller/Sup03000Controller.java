package com.tmb.ecert.setup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmb.ecert.setup.service.Sup03000Service;
import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

@RequestMapping("api/setup/sup03000")
@RestController
public class Sup03000Controller {
	
	@Autowired
	private Sup03000Service service;
	
	@PostMapping("/getEmailTemplate")
	@ResponseBody
	public List<Sup03000Vo> getEmailTemplate(@RequestBody Sup03000Vo form) {
//		System.out.println("save medthod");
		return service.getEmailTemplate(form);
	}
	
	@PostMapping("/getEmailDetail")
	@ResponseBody
	public List<Sup03100Vo> getEmailDetail(@RequestBody Sup03000Vo form) {
		return service.getEmailDetail(form);
	}

}
