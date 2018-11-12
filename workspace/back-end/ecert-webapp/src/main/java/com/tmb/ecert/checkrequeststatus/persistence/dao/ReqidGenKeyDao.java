package com.tmb.ecert.checkrequeststatus.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReqidGenKeyDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Integer getRecNextKey(String keyYear) {
		String sql =" select ISNULL(max(RUNNING),0) from ECERT_REQID_GENKEY where YEAR= ? ";
		List<BigDecimal> res = jdbcTemplate.queryForList(sql,BigDecimal.class, NumberUtils.toInt(keyYear));
		if(res.isEmpty()) {
			return 0;
		}
		return res.get(0).intValue();
	}
	public void updateRecKeyrunning(int nextRunning, String keyYear) {
		jdbcTemplate.update(" update ECERT_REQID_GENKEY set RUNNING=? where YEAR=?",nextRunning,  NumberUtils.toInt(keyYear) );
	}

	public void insertRecKeyrunning(int nextRunning, String keyYear) {
		jdbcTemplate.update(" insert INTO ECERT_REQID_GENKEY (RUNNING,YEAR) VALUES( ?, ? ) ",nextRunning,  NumberUtils.toInt(keyYear) );
	}

}
