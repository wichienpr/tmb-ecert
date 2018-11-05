package com.tmb.ecert.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/cache")
@Controller
public class CacheController {
	
	@GetMapping("/reload")
	@ResponseBody
	public void reloadCache() {
		ApplicationCache.reloadCacheNow();
	}

}