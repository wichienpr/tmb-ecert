package com.tmb.ecert.controller.nrq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("api/nrq/nrq02000")
@Controller
public class Nrq02000Controller {
	
	@GetMapping("/")
	@ResponseBody
	public String nrq02000() {
		return "NRQ02000";
	}
	
}
