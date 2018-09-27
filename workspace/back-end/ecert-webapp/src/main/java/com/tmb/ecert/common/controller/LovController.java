package com.tmb.ecert.common.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.ListOfValue;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("lov")
@Controller
public class LovController {

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

}
