package com.tmb.ecert.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.dao.ListOfValueDao;
import com.tmb.ecert.common.domain.ListOfValue;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class ListOfValueService {
	
	private static final Logger logger = LoggerFactory.getLogger(ListOfValueService.class);

	@Autowired
	private ListOfValueDao listOfValueDao;

	public List<ListOfValue> lovByType(Integer type) {
		logger.info("ListOfValueService::lovByType => params: [ type: {} ]", type);
		return listOfValueDao.lovByType(type);
	}

	public List<ListOfValue> lovAllType() {
		logger.info("ListOfValueService::lovAllType");
		return listOfValueDao.lovAllType();
	}
	
	public ListOfValue lovByCode(String code) {
		logger.info("ListOfValueService::lovByCode => params: [ code: {} ]", code);
		return listOfValueDao.lovByCode(code);
	}
}
