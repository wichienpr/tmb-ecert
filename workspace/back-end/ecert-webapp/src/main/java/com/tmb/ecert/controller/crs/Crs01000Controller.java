package com.tmb.ecert.controller.crs;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.persistence.vo.Crs01000Vo;
import com.tmb.ecert.service.crs.Crs01000Service;





@RequestMapping("api/crs/crs01000")
@Controller
public class Crs01000Controller {

	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private Crs01000Service crs01000Service;
	

	
	@PostMapping("/findAll")
	@ResponseBody
	public List<Crs01000Vo> findAll(Crs01000Vo dataAll) {
		return  crs01000Service.findAllReqForm(dataAll);
	}
	

	
	@PostMapping("/findReqFormByStatus")
	@ResponseBody
	public List<Crs01000Vo> findReqFormByStatus(Crs01000FormVo FormVo) {
		log.info("findReqFormByStatus_C");
		return  crs01000Service.findReqFormByStatus(FormVo);
	}
}
