package com.tmb.ecert.checkrequeststatus.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.checkrequeststatus.persistence.vo.ResponseVo;
import com.tmb.ecert.checkrequeststatus.service.CheckRequestDetailService;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;

import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@RequestMapping("api/crs/crs02000")
@Controller
public class CheckRequestDetailController {

	@Autowired
	private CheckRequestDetailService crsService;

	@GetMapping("/data/{id}")
	@ResponseBody
	public RequestForm formFromId(@PathVariable("id") String id) {
		return crsService.findReqFormById(id);
	}

	@GetMapping("/cert/{id}")
	@ResponseBody
	public List<RequestCertificate> certFromId(@PathVariable("id") String id) {
		return crsService.findCertByReqFormId(id);
	}

	@GetMapping("/cert/list/{id}")
	@ResponseBody
	public List<Certificate> certListFromId(@PathVariable("id") String id) {
		return crsService.findCertListByReqFormId(id);
	}

	@GetMapping("/download/{filename}")
	@ResponseBody
	public void download(@PathVariable("filename") String fileName, HttpServletResponse response) {
		crsService.download(fileName, response);
	}

	@GetMapping("pdf/{name}")
	@ResponseBody
	public void pdf(@PathVariable("name") String name, HttpServletResponse response) {
		crsService.pdf(name, response);
	}

	@PostMapping("cert/reject")
	@ResponseBody
	public CommonMessage<String> rejectMak(@RequestBody RequestForm req) {
		return this.crsService.reject(req, UserLoginUtils.getCurrentUserLogin());
	}

	@GetMapping("cert/approve/{reqFormId}/{superchecker}")
	@ResponseBody
	public CommonMessage<ResponseVo> approve(@PathVariable("reqFormId") String reqFormId,
			@PathVariable("superchecker") String authed) {
		return this.crsService.approve(reqFormId, UserLoginUtils.getCurrentUserLogin(), authed);
	}
	
	@GetMapping("retry/{reqFormId}")
	@ResponseBody
	public CommonMessage<ResponseVo> retry(@PathVariable("reqFormId") String reqid, HttpServletResponse response) {
		return this.crsService.retryPayment(reqid, UserLoginUtils.getCurrentUserLogin());
	}

}
