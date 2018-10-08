package com.tmb.ecert.setup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.setup.dao.EmailTemplateDao;
import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

@Service
public class Sup03000Service {
	
	@Autowired
	private EmailTemplateDao emailDao;
	
	private static String STATUS_ALL = "90001";
	private static String STATUS_ACTIVE= "90002";
	private static String STATUS_INACTIVE = "90003";
	
	
	public List<Sup03000Vo> getEmailTemplate(Sup03000Vo form) {
		form.setStatus(covertDropdownValueToInt(form.getStatus()));
		return emailDao.getEmailTemplate(form);
	}
	
	public List<Sup03100Vo> getEmailDetail(Sup03000Vo form) {
//		form.setStatus(covertDropdownValueToInt(form.getStatus()));
		return emailDao.getEmailDetail(form);
	}
	
	
	public int covertDropdownValueToInt(int status) {
		
		if (STATUS_ALL.equals(Integer.toString(status))) {
			return 2;
			
		}else if (STATUS_INACTIVE.equals(Integer.toString(status))) {
			return 1;
			
		}else if (STATUS_ACTIVE.equals(Integer.toString(status))) {
			return 0;
		}
		return 2;
	}

}
