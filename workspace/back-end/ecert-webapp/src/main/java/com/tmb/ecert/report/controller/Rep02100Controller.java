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

import com.tmb.ecert.report.persistence.vo.Rep02100FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02100Vo;
import com.tmb.ecert.report.service.Rep02100tService;

@Controller
@RequestMapping("api/rep/rep02100")
public class Rep02100Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep02100tService rep02100tService;
	
	@PostMapping("/list")
	@ResponseBody
	public List<Rep02100Vo> list(@RequestBody Rep02100FormVo formVo){
		List<Rep02100Vo> rep02100VoList = new ArrayList<Rep02100Vo>();
		try {
			rep02100VoList = rep02100tService.findAll(formVo);
			
		} catch (Exception e) {
			log.error("Error ! ==> Rep02100Controller method list",e);
		}
		
		return rep02100VoList;
	}

}

 