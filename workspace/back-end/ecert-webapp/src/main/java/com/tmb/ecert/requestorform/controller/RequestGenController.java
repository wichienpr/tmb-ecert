package com.tmb.ecert.requestorform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.requestorform.service.RequestGenKeyService;

@RequestMapping("api/nrq")
@Controller
public class RequestGenController {
	@Autowired
	private RequestGenKeyService requestGenKeyService;

	@GetMapping("/getgenkey")
	@ResponseBody
	public String nrq() {
		return requestGenKeyService.getNextKey();
	}

}
