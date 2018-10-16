package com.tmb.ecert.setup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.setup.service.Sup03000Service;
import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@RequestMapping("api/setup/sup03000")
@RestController
public class Sup03000Controller {
	
	@Autowired
	private Sup03000Service service;
	
	@PostMapping("/getEmailTemplate")
	@ResponseBody
	public DataTableResponse<Sup03000Vo> getEmailTemplate(@RequestBody Sup03000Vo form) {
		return service.getEmailTemplate(form);
	}
	
	@PostMapping("/getEmailDetail")
	@ResponseBody
	public Sup03100Vo getEmailDetail(@RequestBody Sup03000Vo form) {
		return service.getEmailDetail(form);
	}
	
	@PostMapping("/saveEmailDetail")
	@ResponseBody
	public  CommonMessage<String> saveEmailDetail(@RequestBody Sup03100Vo form) {
		return service.saveEmailDetail(form);
	}

}
