package com.tmb.ecert.controller.rep;

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

import com.tmb.ecert.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.persistence.vo.Rep01000Vo;
import com.tmb.ecert.service.rep.Rep01000tService;

@Controller
@RequestMapping("rep/rep01000")
public class Rep01000Controller {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep01000tService rep01000tService;
	
	@PostMapping("/list")
	@ResponseBody
	public List<Rep01000Vo> list(@RequestBody Rep01000FormVo formVo){
		List<Rep01000Vo> rep01000Vo = new ArrayList<Rep01000Vo>();
		try {
			rep01000Vo = rep01000tService.findAll(formVo);
			
		} catch (Exception e) {
			log.error("Error ! ==> Rep01000Controller method list",e);
		}
		
		return rep01000Vo;
	}

}

 