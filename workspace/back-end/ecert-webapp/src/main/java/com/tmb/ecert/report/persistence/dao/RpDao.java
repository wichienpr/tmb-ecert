package com.tmb.ecert.report.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.report.persistence.vo.RpReceiptTaxVo;

@Repository
public class RpDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	

}
