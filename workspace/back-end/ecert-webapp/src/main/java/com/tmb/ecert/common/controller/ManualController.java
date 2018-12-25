package com.tmb.ecert.common.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.service.DownloadService;

@RequestMapping("api/manual")
@Controller
public class ManualController {
	
	@Autowired
	private DownloadService downloadService;
	
	@GetMapping("/pdf")
	@ResponseBody
	public void cerByTypeCode(HttpServletResponse response) {
		downloadService.manual(response);
	}

}
