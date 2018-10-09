package com.tmb.ecert.setup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.setup.dao.EmailTemplateDao;
import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class Sup03000Service {

	@Autowired
	private EmailTemplateDao emailDao;

	private static String STATUS_ALL = "90001";
	private static String STATUS_ACTIVE = "90002";
	private static String STATUS_INACTIVE = "90003";
	
	private static String MSG_SUCS = "ทำรายการสำเร็จ";
	private static String MSG_ERR = "ทำรายการล้มเหลว  ";

	public List<Sup03000Vo> getEmailTemplate(Sup03000Vo form) {
		form.setStatus(covertDropdownValueToInt(form.getStatus()));
		return emailDao.getEmailTemplate(form);
	}

	public List<Sup03100Vo> getEmailDetail(Sup03000Vo form) {
		return emailDao.getEmailDetail(form);
	}

	public CommonMessage<String> saveEmailDetail(Sup03100Vo form) {
		CommonMessage<String> message = new CommonMessage<>();
		try {
			UserDetails user = UserLoginUtils.getCurrentUserLogin();
			String fullName = user.getFirstName() + " " + user.getLastName();
			emailDao.updateEmailTemplate(form,  fullName, user.getUserId());
			emailDao.updateEmailDetail(form, fullName, user.getUserId());
			message.setMessage(MSG_SUCS);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage(MSG_ERR);
			return message;
		}

		
	}

	public int covertDropdownValueToInt(int status) {

		if (STATUS_ALL.equals(Integer.toString(status))) {
			return 2;

		} else if (STATUS_INACTIVE.equals(Integer.toString(status))) {
			return 1;

		} else if (STATUS_ACTIVE.equals(Integer.toString(status))) {
			return 0;
		}
		return 2;
	}

}
