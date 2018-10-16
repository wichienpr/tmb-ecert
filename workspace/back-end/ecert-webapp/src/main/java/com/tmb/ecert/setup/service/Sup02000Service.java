package com.tmb.ecert.setup.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.dao.ParameterConfigurationDao;
import com.tmb.ecert.setup.vo.Sup01100FormVo;
import com.tmb.ecert.setup.vo.Sup02000FormVo;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class Sup02000Service {
	
	@Autowired
	private ParameterConfigurationDao paramDao;
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_ROLEMANAGEMENT);
	
	private static String MSG_SUCS = "ทำรายการสำเร็จ";
	private static String MSG_ERR = "ทำรายการล้มเหลว  ";
	
	public List<ParameterConfig> getParmeter() {
		return paramDao.getParameter();
	}
	
	public CommonMessage<String> saveParameter(Sup02000FormVo form) {
		CommonMessage<String> message = new CommonMessage();
		List<ParameterConfig> listParam = form.getListParameter();
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();
		try {
			paramDao.updateParameterConfig(listParam,fullName,user.getUserId());
			logger.info("update parameter success.");
			message.setMessage(MSG_SUCS);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update parameter error.");
			message.setMessage(MSG_ERR);
			return message;
		}
	}
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

}