package com.tmb.ecert.requestorform.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.requestorform.service.RequestorFormService;

@RequestMapping("api/nrq")
@Controller
public class RequestorFormController {
	
	@Autowired
	private RequestorFormService reqService;
	
	@GetMapping("/")
	@ResponseBody
	public String nrq() {
		return "NRQ";
	}
	
	@PostMapping("/")
	@ResponseBody
	public List<String> all() {
		return null;
	}
	
	@PostMapping("/save")
	@ResponseBody
	public CommonMessage<String> save(@ModelAttribute Nrq02000FormVo form) {
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
