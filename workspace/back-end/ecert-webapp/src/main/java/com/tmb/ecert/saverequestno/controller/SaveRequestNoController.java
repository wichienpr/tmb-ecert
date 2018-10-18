package com.tmb.ecert.saverequestno.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000FormVo;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000Vo;
import com.tmb.ecert.saverequestno.service.SaveRequestNoService;

@RequestMapping("api/srn/srn01000")
@Controller
public class SaveRequestNoController {

	private Logger log = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private SaveRequestNoService srn01000Service;

	@PostMapping("/findReqByTmbReqNo")
	@ResponseBody
	public List<Srn01000Vo> findReqByTmbReqNo(@RequestBody Srn01000FormVo formVo) {
		log.info("findReq_C");
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		srn01000VoList = srn01000Service.findReqByTmbReqNo(formVo);
		return srn01000VoList;
	}

	@PostMapping("/findReqByStatus")
	@ResponseBody
	public List<Srn01000Vo> findReqByStatus(@RequestBody Srn01000FormVo formVo) {
		log.info("findReqByStatus_C");
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		try {
			srn01000VoList = srn01000Service.findReqByStatus(formVo);
		} catch (Exception e) {
			log.error("Error ! ==> CheckRequestStatusController method findReqByStatus", e);
		}
		return srn01000VoList;
	}

}
