package com.tmb.ecert.service.crs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.persistence.dao.Crs01000Dao;
import com.tmb.ecert.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.persistence.vo.Crs01000Vo;

@Service
public class Crs01000Service {

	private Logger logger = LoggerFactory.getLogger(Crs01000Service.class);

	@Autowired
	private Crs01000Dao crs01000Dao;

	public List<Crs01000Vo> findAllReqForm(Crs01000Vo dataAll) {
		return crs01000Dao.findAllReqForm(dataAll);
	}

	public List<Crs01000Vo> findReqFormByStatus(Crs01000FormVo FormVo) {
		logger.info("findReqFormByStatus_Service");
		FormVo.setReqDate("01/10/2018");
		FormVo.setToReqDate("05/10/2018");
		FormVo.setCompanyName("");
		FormVo.setOrganizeId("");
		FormVo.setTmbReqNo("");
		FormVo.setStatus("");

		return crs01000Dao.findReqFormByStatus(FormVo);
	}
}
