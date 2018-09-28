package com.tmb.ecert.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.persistence.vo.Rep01000Vo;

@Repository
public class Rep01000Repository {
	private Logger log = LoggerFactory.getLogger(Rep01000Repository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String SQL_ECERT_REQUEST_FORM = "SELECT * FROM SQL_ECERT_REQUEST_FORM ";
	
	public List<Rep01000Vo> getData(Rep01000FormVo formVo) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		List<Object> params = new ArrayList<>();
		List<Rep01000Vo> rep01000Vo = new ArrayList<Rep01000Vo>();
		
		rep01000Vo = jdbcTemplate.query(sql.toString(), params.toArray(), rep01000RowMapper);
		
		return rep01000Vo;
	}
	
	 private RowMapper<Rep01000Vo> rep01000RowMapper = new RowMapper<Rep01000Vo>() {
	    	@Override
	    	public Rep01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
	    		Rep01000Vo vo = new Rep01000Vo();
	    		return vo;
	    	}
	 };
}
