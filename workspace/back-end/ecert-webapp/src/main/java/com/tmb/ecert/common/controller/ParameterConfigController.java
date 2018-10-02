package com.tmb.ecert.common.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.ParameterConfig;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/parameter")
@Controller
public class ParameterConfigController {

	@GetMapping("/")
	@ResponseBody
	public List<ParameterConfig> all() {
		return ApplicationCache.getParamAll();
	}
}
