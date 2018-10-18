package com.tmb.ecert.common.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.RoleVo;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/role")
@Controller
public class RoleController {

	@GetMapping("/")
	@ResponseBody
	public List<RoleVo> all() {
		return ApplicationCache.getRoleAll();
	}
	
}
