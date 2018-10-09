package com.tmb.ecert.report.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.report.persistence.vo.Rep02200FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02200Vo;
import com.tmb.ecert.report.service.Rep02200tService;

@Controller
@RequestMapping("api/rep/rep02200")
public class Rep02200Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep02200tService rep02200tService;
	
	@PostMapping("/list")
	@ResponseBody
	public List<Rep02200Vo> list(@RequestBody Rep02200FormVo formVo){
		List<Rep02200Vo> rep02200VoList = new ArrayList<Rep02200Vo>();
		try {
			rep02200VoList = rep02200tService.findAll(formVo);
			
		} catch (Exception e) {
			log.error("Error ! ==> Rep02200Controller method list",e);
		}
		
		return rep02200VoList;
	}

}

 