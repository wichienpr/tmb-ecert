package com.tmb.ecert.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
}
