package com.tmb.ecert.m1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExController {

	
	
	@GetMapping("/exHello")
	@ResponseBody
	public List<String> addRiskAssExcAreaHdr() {
		List<String> strList = new ArrayList<>();
		strList.add("Hello what is my name");
		return strList;
	}
}
