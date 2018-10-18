package com.tmb.ecert.saverequestno.controller;

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

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@RequestMapping("api/srn/srn01000")
@Controller
public class SaveRequestNoController {

	private Logger log = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private SaveRequestNoService srn01000Service;


	@PostMapping("/list")
	@ResponseBody
	public DataTableResponse<Srn01000Vo> findReqByTmbReqNo(@RequestBody Srn01000FormVo formVo) {
		log.info("findReq_C");
		System.out.println("tmb :"+formVo.getTmbReqNo()+" /  Status"+ formVo.getStatus());
		
		DataTableResponse<Srn01000Vo> dt = srn01000Service.findReqByTmbReqNo(formVo);
		return dt;
	}

	
}
