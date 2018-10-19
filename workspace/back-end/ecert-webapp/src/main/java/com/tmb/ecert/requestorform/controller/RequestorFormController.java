package com.tmb.ecert.requestorform.controller;

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
	
	@PostMapping("/save")
	@ResponseBody
	public CommonMessage<String> save(@ModelAttribute Nrq02000FormVo form) {
		return reqService.save(form);
	}
	
	@PostMapping("/update")
	@ResponseBody
	public CommonMessage<String> update(@ModelAttribute Nrq02000FormVo form) {
		return reqService.update(form);
	}
	
	@GetMapping("/data/{id}")
	@ResponseBody
	public RequestForm formFromId(@PathVariable("id") String id) {
		return reqService.findReqFormById(id);
	}
	
	@PostMapping("/save/by/self")
	@ResponseBody
	public CommonMessage<String> saveBySelf() {
		return reqService.saveBySelf();
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
