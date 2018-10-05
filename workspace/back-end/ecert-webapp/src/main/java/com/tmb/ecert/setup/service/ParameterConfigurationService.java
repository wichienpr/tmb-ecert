package com.tmb.ecert.setup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.dao.ParameterConfigurationDao;
import com.tmb.ecert.setup.vo.Sup01010FormVo;

@Service
public class ParameterConfigurationService {
	
	@Autowired
	private ParameterConfigurationDao paramDao;
	
	
	public List<ParameterConfig> getParmeter() {
		return paramDao.getParameter();
	}

}
