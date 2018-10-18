package com.tmb.ecert.common.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.Certificate;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/cer")
@Controller
public class CertificateController {

	@GetMapping("/")
	@ResponseBody
	public List<Object> cerAll() {
		return ApplicationCache.getCerAll();
	}

	@PostMapping("/typeCode")
	@ResponseBody
	public List<Certificate> cerByTypeCode(@RequestBody Certificate cer) {
		return ApplicationCache.getCerByType(cer.getTypeCode());
	}

}
