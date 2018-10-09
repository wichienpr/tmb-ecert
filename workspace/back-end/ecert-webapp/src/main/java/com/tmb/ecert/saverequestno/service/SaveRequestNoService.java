package com.tmb.ecert.saverequestno.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.saverequestno.persistence.dao.SaveRequestNoDao;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000FormVo;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000Vo;;

@Service
public class SaveRequestNoService {

	private Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private SaveRequestNoDao srn01000Dao;

	public List<Srn01000Vo> findReqByTmbReqNo(Srn01000FormVo formVo) {
		logger.info("findReqByTmbReqNo_Service");
		logger.info("TmbReqNo :: " + formVo.getTmbReqNo());
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		srn01000VoList = srn01000Dao.findReqByTmbReqNo(formVo);
		return srn01000VoList;
	}

	public List<Srn01000Vo> findReqByStatus(Srn01000FormVo formVo) {
		logger.info("findReqByStatus_Service");
		logger.info("status :: " + formVo.getStatus());
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		srn01000VoList = srn01000Dao.findReqByStatus(formVo);
		return srn01000VoList;
	}

}
