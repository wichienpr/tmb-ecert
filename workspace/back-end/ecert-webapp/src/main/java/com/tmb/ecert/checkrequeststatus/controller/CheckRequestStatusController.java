package com.tmb.ecert.checkrequeststatus.controller;


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

import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.checkrequeststatus.service.CheckRequestStatusService;



@RequestMapping("api/crs/crs01000")
@Controller
public class CheckRequestStatusController {

	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private CheckRequestStatusService crs01000Service;
	

	
	@PostMapping("/findReq")
	@ResponseBody
	public List<Crs01000Vo> findReq(@RequestBody Crs01000FormVo formVo) {
		log.info("findReq_C");
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		try {
			crs01000VoList = crs01000Service.findReq(formVo);	
		} catch (Exception e) {
			log.error("Error ! ==> CheckRequestStatusController method findReq",e);
		}
		
		return  crs01000VoList;
	}
	
	
	@PostMapping("/findReqByStatus")
	@ResponseBody
	public List<Crs01000Vo> findReqByStatus(@RequestBody Crs01000FormVo formVo) {
		log.info("findReqFormByStatus_C");
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		try {
			crs01000VoList = crs01000Service.findReqByStatus(formVo);	
		} catch (Exception e) {
			log.error("Error ! ==> CheckRequestStatusController method findReqByStatus",e);
		}
		
		return  crs01000VoList;
	}
	
}