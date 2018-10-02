package com.tmb.ecert.NewRequest.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.NewRequest.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.NewRequest.persistence.vo.Nrq02000Vo;
import com.tmb.ecert.NewRequest.service.RequestorFormService;
import com.tmb.ecert.common.domain.CommonMessage;

@RequestMapping("api/nrq/nrq02000")
@Controller
public class RequestorFormController {
	
	@Autowired
	private RequestorFormService reqService;
	
	@GetMapping("/")
	@ResponseBody
	public String nrq02000() {
		return "NRQ02000";
	}
	
	@PostMapping("/")
	@ResponseBody
	public CommonMessage<Nrq02000Vo> save(@ModelAttribute Nrq02000FormVo form) {
		return reqService.save(form);
	}
	
	@GetMapping("/download/{filename}")
	@ResponseBody
	public void download(@PathVariable("filename") String fileName, HttpServletResponse response) {
		reqService.download(fileName, response);
	}
	
	@GetMapping("pdf/{name}")
	@ResponseBody
	public void pdf(@PathVariable("name") String name, HttpServletResponse response) {
		reqService.pdf(name, response);
	}
}
