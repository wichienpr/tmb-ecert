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
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.requestorform.service.RequestGenKeyService;
import com.tmb.ecert.requestorform.service.RequestorFormService;

@RequestMapping("api/nrq")
@Controller
public class RequestorFormController {
	
	@Autowired
	private RequestorFormService reqService;
	
	@Autowired
	private RequestGenKeyService generateKey;
	
	@GetMapping("/generate/key")
	@ResponseBody
	public String gerenateKeys() {
		return generateKey.getNextKey();
	}
	
	@GetMapping("/data")
	@ResponseBody
	public List<RequestForm> all() {	
		return reqService.findAll();
	}
	
	@GetMapping("/data/{id}")
	@ResponseBody
	public List<RequestForm> fromId(@PathVariable("id") String id) {
		return reqService.findById(id);
	}
	
	@GetMapping("/cert/{id}")
	@ResponseBody
	public List<RequestCertificate> certFromId(@PathVariable("id") String id) {
		return reqService.findCertByReqFormId(id);
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
