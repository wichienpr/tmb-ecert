package com.tmb.ecert.nrq.cer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CertificateDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static String template = " SELECT * FROM ECERT_CERTIFICATE WHERE 1=1 ";
	
	
	
}
