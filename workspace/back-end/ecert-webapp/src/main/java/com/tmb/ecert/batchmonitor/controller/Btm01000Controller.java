package com.tmb.ecert.batchmonitor.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.batchmonitor.service.Btm01000Service;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;
import th.co.baiwa.buckwaframework.preferences.constant.MessageConstants.MESSAGE_STATUS;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@RequestMapping("api/btm/btm01000")
@Controller
public class Btm01000Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_BATCHMONITORING);
	
	@Autowired
	private Btm01000Service service;
	
	@PostMapping("/getListBatch")
	@ResponseBody
	public DataTableResponse<Btm01000Vo> getListBatch(@RequestBody Btm01000FormVo form) {
		return service.getListBatch(form);
	}
	
	
	@PostMapping("/getListBatch2")
	@ResponseBody
	public DataTableResponse<Btm01000Vo> getListBatch2(@RequestBody Btm01000FormVo form) {
		return service.getListBatch(form);
	}
	
	@PostMapping("/reRunJob")
	@ResponseBody
	public CommonMessage<String> reRunJob(@RequestBody Btm01000Vo form) {
		CommonMessage<String> msg = new CommonMessage<>();
		try {
			UserDetails user = UserLoginUtils.getCurrentUserLogin();
			String fullName = user.getFirstName() + " " + user.getLastName();
			service.rerunJob(form,fullName,user.getUserId());
			
			msg.setData(MESSAGE_STATUS.SUCCEED);
			msg.setMessage(MESSAGE_STATUS.SUCCEED);
		} catch (Exception e) {
			logger.error("reRunJob",e);
			msg.setData(MESSAGE_STATUS.FAILED);
			msg.setMessage(MESSAGE_STATUS.FAILED);
		}
		return msg;
	}


}
