package com.tmb.ecert.report.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.report.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.report.persistence.vo.Rep01000Vo;
import com.tmb.ecert.report.service.Rep01000tService;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@Controller
@RequestMapping("api/rep/rep01000")
public class Rep01000Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep01000tService rep01000tService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/list")
	@ResponseBody
	public DataTableResponse<Rep01000Vo> list(@RequestBody Rep01000FormVo formVo){
//		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();
		DataTableResponse<Rep01000Vo> rep01000VoListReturn = new DataTableResponse<>();
		try {
			rep01000VoListReturn = rep01000tService.findAllDatatable(formVo);
			
		} catch (Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_EXPORT_EXCEL, e.toString());
			log.error("Error ! ==> Rep01000Controller method list",e);
		}
		
		return rep01000VoListReturn;
	}
	
	@GetMapping("/exportFile")
	@ResponseBody
	public  void exportFile(@ModelAttribute Rep01000FormVo formVo, HttpServletResponse response) throws Exception {
		try {
			rep01000tService.exportFile(formVo, response);
		} catch (Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_EXPORT_EXCEL, e.toString());
			log.error("Error ! ==> Rep01000Controller method exportFile",e);
		}
		
	}

}

 