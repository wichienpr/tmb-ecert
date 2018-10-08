package com.tmb.ecert.auditlog.controller;

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

import com.tmb.ecert.auditlog.persistence.vo.Adl01000FormVo;
import com.tmb.ecert.auditlog.persistence.vo.Adl01000Vo;
import com.tmb.ecert.auditlog.service.Adl01000tService;

@Controller
@RequestMapping("api/adl/adl01000")
public class Adl01000Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Adl01000tService adl01000tService;
	
	@PostMapping("/list")
	@ResponseBody
	public List<Adl01000Vo> list(@RequestBody Adl01000FormVo formVo){
		List<Adl01000Vo> adl01000VoList = new ArrayList<Adl01000Vo>();
		try {
			adl01000VoList = adl01000tService.findAll(formVo);
			
		} catch (Exception e) {
			log.error("Error ! ==> adl01000Controller method list",e);
		}
		
		return adl01000VoList;
	}
	@GetMapping("/exportFile")
	@ResponseBody
	public  void exportFile(@ModelAttribute Adl01000FormVo formVo, HttpServletResponse response) throws Exception {
		try {
			adl01000tService.exportFile(formVo, response);
		} catch (Exception e) {
			log.error("Error ! ==> adl01000Controller method exportFile",e);
		}
		
	}

}

 