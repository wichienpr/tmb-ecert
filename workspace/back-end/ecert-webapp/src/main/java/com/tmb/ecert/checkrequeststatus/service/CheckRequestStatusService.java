package com.tmb.ecert.checkrequeststatus.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestStatusDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.CountStatusVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.StatusVo;



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
	
	public CountStatusVo countStatus(CountStatusVo countStatusVo){
		logger.info("countStatus_Service");
		List<StatusVo> statusVoList = crs01000Dao.countStatus();
		
		for (StatusVo data : statusVoList) {
			if("10001".equals(data.getStatus())) {
				countStatusVo.setCountStatus1(data.getCount());
			}else if("10002".equals(data.getStatus())) {
				countStatusVo.setCountStatus2(data.getCount());
			}else if("10003".equals(data.getStatus())) {
				countStatusVo.setCountStatus3(data.getCount());
			}else if("10004".equals(data.getStatus())) {
				countStatusVo.setCountStatus4(data.getCount());
			}else if("10005".equals(data.getStatus())) {
				countStatusVo.setCountStatus5(data.getCount());
			}else if("10006".equals(data.getStatus())) {
				countStatusVo.setCountStatus6(data.getCount());
			}else if("10007".equals(data.getStatus())) {
				countStatusVo.setCountStatus7(data.getCount());
			}else if("10008".equals(data.getStatus())) {
				countStatusVo.setCountStatus8(data.getCount());
			}else if("10009".equals(data.getStatus())) {
				countStatusVo.setCountStatus9(data.getCount());
			}else if("10010".equals(data.getStatus())) {
				countStatusVo.setCountStatus10(data.getCount());
			}else if("10011".equals(data.getStatus())) {
				countStatusVo.setCountStatus11(data.getCount());
			}	
			
		}
		
		return countStatusVo;
	}
}
