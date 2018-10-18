package com.tmb.ecert.saverequestno.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.saverequestno.persistence.dao.SaveRequestNoDao;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000FormVo;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000Vo;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;;

@Service
public class SaveRequestNoService {

	private Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private SaveRequestNoDao srn01000Dao;


	public DataTableResponse<Srn01000Vo> findReqByTmbReqNo(Srn01000FormVo formVo) {
		logger.info("findReqByTmbReqNo_Service");
		
		DataTableResponse<Srn01000Vo> dt = new DataTableResponse<>();
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		
		if(StringUtils.isNotBlank(formVo.getTmbReqNo())) {
			srn01000VoList = srn01000Dao.findReqByTmbReqNo(formVo);
		}else if(StringUtils.isNotBlank(formVo.getStatus())) {
			srn01000VoList = srn01000Dao.findReqByStatus(formVo);
		}
		 
		dt.setData(srn01000VoList);
		int count = srn01000Dao.countDataTable(formVo);
		dt.setRecordsTotal(count);
		return dt;
	}


}
