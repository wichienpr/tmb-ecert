package com.tmb.ecert.checkrequeststatus.service;

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

	public List<Crs01000Vo> findAllReqForm(Crs01000Vo dataAll) {
		return crs01000Dao.findAllReqForm(dataAll);
	}

	public List<Crs01000Vo> findReqFormByStatus(Crs01000FormVo FormVo) {
		logger.info("findReqFormByStatus_Service");
		FormVo.setReqDate("01/10/2018");
		FormVo.setToReqDate("02/10/2018");
		FormVo.setCompanyName("บริษัทมหารวย");
		FormVo.setOrganizeId("1234543234567");
		FormVo.setTmbReqNo("20181000001");
		FormVo.setStatus("10001");

		return crs01000Dao.findReqFormByStatus(FormVo);
	}
}
