package com.tmb.ecert.batchmonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.batchmonitor.service.Btm01000Service;

@RequestMapping("api/btm/btm01000")
@Controller
public class Btm01000Controller {
	
	@Autowired
	private Btm01000Service service;
	
	@PostMapping("/getListBatch")
	@ResponseBody
	public List<Btm01000Vo> getListBatch(@RequestBody Btm01000FormVo form) {
		return service.getListBatch(form);
	}
	

}
