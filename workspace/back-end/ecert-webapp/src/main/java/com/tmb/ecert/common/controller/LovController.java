package com.tmb.ecert.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.ListOfValue;
import com.tmb.ecert.common.service.ListOfValueService;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/lov")
@Controller
public class LovController {
	
	@Autowired
	ListOfValueService lovService;

	@GetMapping("/")
	@ResponseBody
	List<Object> lovAll() {
		return ApplicationCache.getLovAll();
	}
	
	@PostMapping("/type")
	@ResponseBody
	List<ListOfValue> lovByType(@RequestBody ListOfValue lov) {
		return ApplicationCache.getLovByType(lov.getType());
	}
	
	@PostMapping("/code")
	@ResponseBody
	ListOfValue lovByCode(@RequestBody ListOfValue lov) {
		return lovService.lovByCode(lov.getCode());
	}

}
