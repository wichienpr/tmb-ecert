package com.tmb.ecert.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.dao.ParameterConfigDao;
import com.tmb.ecert.common.domain.ParameterConfig;

@Service
public class ParameterConfigService {
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterConfigService.class);

	@Autowired
	private ParameterConfigDao parameterConfigDao;
	
	public List<ParameterConfig> findAll() {
		logger.info("ParameterConfigService::findAll");
		return parameterConfigDao.findAll();
	}
	
}
