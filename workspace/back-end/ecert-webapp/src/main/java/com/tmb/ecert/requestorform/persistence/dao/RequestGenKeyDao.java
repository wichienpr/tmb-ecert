package com.tmb.ecert.requestorform.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequestGenKeyDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Integer getNextKey(String keyYear) {
		String sql =" select RUNNING from ECERT_REQUEST_GENKEY where YEAR=?";
		List<BigDecimal> res = jdbcTemplate.queryForList(sql,BigDecimal.class, NumberUtils.toInt(keyYear));
		if(res.isEmpty()) {
			return 0;
		}
		return res.get(0).intValue();
	}

	public void updateKeyrunning(int nextRunning, String keyYear) {
		jdbcTemplate.update(" update ECERT_REQUEST_GENKEY set RUNNING=? where YEAR=?",nextRunning,  NumberUtils.toInt(keyYear) );
	}

	public void insertKeyrunning(int nextRunning, String keyYear) {
		jdbcTemplate.update(" insert INTO ECERT_REQUEST_GENKEY (RUNNING,YEAR) VALUES( ?, ? ) ",nextRunning,  NumberUtils.toInt(keyYear) );
	}
	
}
