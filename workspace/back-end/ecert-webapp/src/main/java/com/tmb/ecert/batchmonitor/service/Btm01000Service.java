package com.tmb.ecert.batchmonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchmonitor.persistence.dao.BatchMonitoringDao;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;

@Service
public class Btm01000Service {
	
	@Autowired
	private BatchMonitoringDao batchDao;
	
	
	public List<Btm01000Vo> getListBatch(Btm01000FormVo form) {

		return batchDao.getListBatch(form);

	}


}
