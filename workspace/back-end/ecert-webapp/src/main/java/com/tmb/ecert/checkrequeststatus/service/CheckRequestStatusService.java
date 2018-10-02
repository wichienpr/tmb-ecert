package com.tmb.ecert.checkrequeststatus.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestStatusDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;


@Service
public class CheckRequestStatusService {

	private Logger logger = LoggerFactory.getLogger(CheckRequestStatusService.class);

	@Autowired
	private CheckRequestStatusDao crs01000Dao;



	public List<Crs01000Vo> findReq(Crs01000FormVo formVo){
		logger.info("findReq_Service");
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		crs01000VoList = crs01000Dao.findReq(formVo);
		return crs01000VoList;
	}
	
	public List<Crs01000Vo> findReqByStatus(Crs01000FormVo formVo){
		logger.info("findReqByStatus_Service");
		logger.info("status :: "+ formVo.getStatus());
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		crs01000VoList = crs01000Dao.findReqByStatus(formVo);
		return crs01000VoList;
	}
	
	
}
