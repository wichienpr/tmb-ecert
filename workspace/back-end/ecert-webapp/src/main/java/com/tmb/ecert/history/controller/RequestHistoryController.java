package com.tmb.ecert.history.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.history.persistence.vo.RequestHistoryVo;
import com.tmb.ecert.history.service.RequestHistoryService;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

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

	@GetMapping("/list")
	@ResponseBody
	public List<RequestHistoryVo> list() {
		return reqHistory.findAll();
	}

	@PostMapping("/list")
	@ResponseBody
	public DataTableResponse<RequestHistoryVo> listByVo(@RequestBody RequestHistoryVo vo) {
		return reqHistory.findByVo(vo);
	}

	@GetMapping("/list/{id}")
	@ResponseBody
	public List<RequestHistoryVo> list(@PathVariable("id") String reqFormId) {
		return reqHistory.findByReqFormId(reqFormId);
	}
}
