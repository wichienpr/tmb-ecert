package com.tmb.ecert.report.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.report.persistence.dao.RepDao;
import com.tmb.ecert.report.persistence.vo.Rep02200FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02200Vo;

@Service
public class Rep02200tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RepDao repDao;
	
	
	public List<Rep02200Vo> findAll(Rep02200FormVo formVo){
		List<Rep02200Vo> rep02200VoList = new ArrayList<Rep02200Vo>();
		rep02200VoList = repDao.getDataRep02200(formVo);
		return rep02200VoList;
	}
	
	
}


