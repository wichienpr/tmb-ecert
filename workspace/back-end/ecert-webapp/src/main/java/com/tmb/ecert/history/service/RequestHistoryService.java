package com.tmb.ecert.history.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.history.persistence.vo.RequestHistoryVo;

@Service
public class RequestHistoryService {
	
	@Autowired
	private RequestHistoryDao requestHistoryDao;

	public List<String> test() {
		List<String> list = new ArrayList<String>();
		for(Integer i=0; i<100; i++) {
			list.add(i.toString());
		}
		return list;
	}
	
	public List<RequestHistoryVo> findAll() {
		return requestHistoryDao.findAll();
	}
	
	public List<RequestHistoryVo> findByReqFormId(String reqFormId) {
		Long id = Long.valueOf(reqFormId);
		return requestHistoryDao.findByReqFormId(id);
	}
}
