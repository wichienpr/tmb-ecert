package com.tmb.ecert.controller.nrq;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.persistence.vo.Nrq02000Vo;
import com.tmb.ecert.service.nrq.Nrq02000Service;

@RequestMapping("api/nrq/nrq02000")
@Controller
public class Nrq02000Controller {
	
	@Autowired
	private Nrq02000Service nqr02000Service;
	
	@GetMapping("/")
	@ResponseBody
	public String nrq02000() {
		return "NRQ02000";
	}
	
	@PostMapping("/")
	@ResponseBody
	public CommonMessage<Nrq02000Vo> save(@ModelAttribute Nrq02000FormVo form) {
		try {
			return nqr02000Service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonMessage<Nrq02000Vo> msg = new CommonMessage<Nrq02000Vo>();
		msg.setMessage("ERROR");
		return msg;
	}
	
	@GetMapping("/download/{filename}")
	@ResponseBody
	public void download(@PathVariable("filename") String fileName, HttpServletResponse response) {
		nqr02000Service.download(fileName, response);
	}
}
