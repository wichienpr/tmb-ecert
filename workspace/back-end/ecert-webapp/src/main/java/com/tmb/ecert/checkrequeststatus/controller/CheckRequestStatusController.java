package com.tmb.ecert.checkrequeststatus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.checkrequeststatus.persistence.vo.CertificateVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.CountStatusVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.checkrequeststatus.service.CheckRequestCertificatService;
import com.tmb.ecert.checkrequeststatus.service.CheckRequestStatusService;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@RequestMapping("api/crs/crs01000")
@Controller
public class CheckRequestStatusController {

	private Logger log = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private CheckRequestStatusService crs01000Service;

	@Autowired
	private CheckRequestCertificatService checkRequestCerService;

	@PostMapping("/upLoadCertificate")
	@ResponseBody
	public CommonMessage<String> upLoadCertificate(@ModelAttribute CertificateVo certificateVo) {
		return checkRequestCerService.upLoadCertificateByCk(certificateVo);
	}

	@PostMapping("/list")
	@ResponseBody
	public DataTableResponse<Crs01000Vo> list(@RequestBody Crs01000FormVo formVo) {
		DataTableResponse<Crs01000Vo> dt = crs01000Service.findReqForm(formVo);
		return dt;
	}

	@PostMapping("/countStatus")
	@ResponseBody
	public CountStatusVo countStatus(CountStatusVo countStatusVo) {
		log.info("countStatus_C");
		CountStatusVo countStatus = new CountStatusVo();
		try {
			countStatus = crs01000Service.countStatus(countStatusVo);
		} catch (Exception e) {
			log.error("Error ! ==> CheckRequestStatusController method countStatus", e);
		}

		return countStatus;
	}

}
