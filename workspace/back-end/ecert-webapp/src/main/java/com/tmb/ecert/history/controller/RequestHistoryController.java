package com.tmb.ecert.history.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.history.persistence.vo.RequestHistoryVo;
import com.tmb.ecert.history.service.RequestHistoryService;

@RequestMapping("api/history/")
@Controller
public class RequestHistoryController {
	
	@Autowired
	private RequestHistoryService reqHistory;
	
	@GetMapping("/")
	@ResponseBody
	public String requestHistory() {
		return "This is RequestHistory API";
	}
	
	@GetMapping("/list/num")
	@ResponseBody
	public List<String> numList() {
		return reqHistory.test();
	}
	
	@GetMapping("/list")
	@ResponseBody
	public List<RequestHistoryVo> list() {
		return reqHistory.findAll();
	}
	
	@GetMapping("/list/{id}")
	@ResponseBody
	public List<RequestHistoryVo> list(@PathVariable("id") String reqFormId) {
		return reqHistory.findByReqFormId(reqFormId);
	}
}
