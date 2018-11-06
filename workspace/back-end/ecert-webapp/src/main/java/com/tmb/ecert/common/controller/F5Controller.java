package com.tmb.ecert.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class F5Controller {
	
	
	@GetMapping("/")
	public String f5Refresh() {
		return "redirect:/app/index.html";
	}
	
	@PostMapping("/")
	public String f5RefreshPost() {
		return "redirect:/app/index.html";
	}
	
	
}
