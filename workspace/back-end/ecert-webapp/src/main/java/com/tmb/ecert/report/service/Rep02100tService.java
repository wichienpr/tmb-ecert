package com.tmb.ecert.report.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.report.persistence.dao.RepDao;
import com.tmb.ecert.report.persistence.vo.Rep02100FormVo;
import com.tmb.ecert.report.persistence.vo.Rep02100Vo;

@Service
public class Rep02100tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RepDao repDao;
	
	
	public List<Rep02100Vo> findAll(Rep02100FormVo formVo){
		List<Rep02100Vo> rep02100VoList = new ArrayList<Rep02100Vo>();
		rep02100VoList = repDao.getDataRep02100(formVo);
		return rep02100VoList;
	}
	
	
}


