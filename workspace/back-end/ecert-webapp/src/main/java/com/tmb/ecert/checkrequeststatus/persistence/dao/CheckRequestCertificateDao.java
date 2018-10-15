package com.tmb.ecert.checkrequeststatus.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class CheckRequestCertificateDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String SQL_ECERT_REQUEST_FORM_UPDATE = "UPDATE ECERT_REQUEST_FORM SET";

	public boolean upDateCertificateByCk(RequestForm reqForm) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		sql.append(" CERTIFICATE_FILE =?, ");
		sql.append(" STATUS =? ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND REQFORM_ID = ? ");
		sql.append(" AND REQUESTFORM_FILE IS NOT NULL  ");
		sql.append(" AND RECEIPT_FILE IS NOT NULL   ");

		int row = jdbcTemplate.update(sql.toString(),
				new Object[] { reqForm.getCertificateFile(), reqForm.getStatus(), reqForm.getReqFormId() });

		return row > 0;
	}

}
