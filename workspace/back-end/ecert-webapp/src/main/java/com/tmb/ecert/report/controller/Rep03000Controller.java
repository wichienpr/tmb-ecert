package com.tmb.ecert.report.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.report.persistence.vo.Rep03000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep03000Vo;
import com.tmb.ecert.report.service.Rep03000tService;

@Controller
@RequestMapping("api/rep/rep03000")
public class Rep03000Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep03000tService rep03000tService;
	
	@PostMapping("/list")
	@ResponseBody
	public Rep03000FormVo list(@RequestBody Rep03000FormVo formVo){
		Rep03000FormVo formVofindAll = new Rep03000FormVo();
		
		try {
			formVofindAll = rep03000tService.findAll(formVo);
			
		} catch (Exception e) {
			log.error("Error ! ==> Rep03000Controller method list",e);
		}
		
		return formVofindAll;
	}
	@GetMapping("/exportFile")
	@ResponseBody
	public  void exportFile(@ModelAttribute Rep03000FormVo formVo, HttpServletResponse response) throws Exception {
		try {
			rep03000tService.exportFile(formVo, response);
		} catch (Exception e) {
			log.error("Error ! ==> Rep03000Controller method exportFile",e);
		}
		
	}

}

 