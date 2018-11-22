package com.tmb.ecert.setup.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.setup.dao.ParameterConfigurationDao;
import com.tmb.ecert.setup.vo.Sup02000FormVo;

import th.co.baiwa.buckwaframework.preferences.constant.MessageConstants.MESSAGE_STATUS;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class Sup02000Service {
	
	@Autowired
	private ParameterConfigurationDao paramDao;
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_PARAMETERCONFIG);
	
	public List<ParameterConfig> getParmeter() {
		return paramDao.getParameter();
	}
	
	public CommonMessage<String> saveParameter(Sup02000FormVo form) {
		CommonMessage<String> message = new CommonMessage<String>();
		List<ParameterConfig> listParam = form.getListParameter();
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();
		try {
			paramDao.updateParameterConfig(listParam,fullName,user.getUserId());
			// ReloadCache
			logger.info("update parameter success.");
			message.setData(MESSAGE_STATUS.SUCCEED);
			message.setMessage(MESSAGE_STATUS.SUCCEED);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update parameter error.");
			message.setData(MESSAGE_STATUS.FAILED);
			message.setMessage(MESSAGE_STATUS.FAILED);
			return message;
		}
	}
}
