package com.tmb.ecert.checkrequeststatus.persistence.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUuploadRequest;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class CheckRequestCertificateDao {
	
	private Logger log = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String SQL_ECERT_REQUEST_FORM_UPDATE = "UPDATE ECERT_REQUEST_FORM SET";

	public boolean upDateCertificateByCk(RequestForm reqForm) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		sql.append(" CERTIFICATE_FILE = ? ");
//		sql.append(" STATUS =? ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND REQFORM_ID = ? ");
		sql.append(" AND REQUESTFORM_FILE IS NOT NULL  ");
		sql.append(" AND RECEIPT_FILE IS NOT NULL   ");
		
		log.info("id => {}", reqForm.getReqFormId());

		int row = jdbcTemplate.update(sql.toString(),
				new Object[] { reqForm.getCertificateFile(), reqForm.getReqFormId() });
		
		return row > 0;
	}
	
	public boolean upDateCertificateFile(RequestForm reqForm) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		sql.append(" CERTIFICATE_FILE = ? ");
//		sql.append(" STATUS =? ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND REQFORM_ID = ? ");
		sql.append(" AND REQUESTFORM_FILE IS NOT NULL  ");
//		sql.append(" AND RECEIPT_FILE IS NOT NULL   ");
		
		log.info("id => {}", reqForm.getReqFormId());

		int row = jdbcTemplate.update(sql.toString(),
				new Object[] { reqForm.getCertificateFile(), reqForm.getReqFormId() });
		
		return row > 0;
	}
	
	public int insertECMHistory(Long reqID,ECMUuploadRequest ecmReq,String objId,String userId) {
		Date current = new Date();
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" INSERT INTO ECERT_ECM_HISTORY (REQFORM_ID,FILE_NAME,FILE_TYPE,OBJECT_ID,IMPORT_DATE,IMPORT_BY_ID) "
				+ " VALUES (?,?,?,?,?,?) ");
		List<Object> params = new ArrayList<>();
		params.add(reqID);
		params.add(ecmReq.getName());
		params.add(ecmReq.getTmbDocTypeCode());
		params.add(objId);
		params.add(current);
		params.add(userId);

		int result = jdbcTemplate.update(sql.toString(), params.toArray());

		return result;
	}

}
