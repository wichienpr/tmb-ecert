package com.tmb.ecert.auditlog.controller;

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

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@Controller
@RequestMapping("api/adl/adl01000")
public class Adl01000Controller {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Adl01000tService adl01000tService;

	@PostMapping("/list")
	@ResponseBody
	public DataTableResponse<Adl01000Vo> list(@RequestBody Adl01000FormVo formVo) {
		DataTableResponse<Adl01000Vo> dt = adl01000tService.findAll(formVo);
		return dt;
	}

	@GetMapping("/exportFile")
	@ResponseBody
	public void exportFile(@ModelAttribute Adl01000FormVo formVo, HttpServletResponse response) throws Exception {
		try {
			adl01000tService.exportFile(formVo, response);
		} catch (Exception e) {
			log.error("Error ! ==> adl01000Controller method exportFile", e);
		}

	}

}
