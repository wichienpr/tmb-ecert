package com.tmb.ecert.batchjob.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.EcertHROfficeCode;

@Repository
public class HROfficeCodeBatchDao {
	
//	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HROFFICECODE);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INSERT_ECERT_HR_OFFICE_CODE = " ( " +
		" EFFECTIVE_DATE , " +
		" TYPE           , " +
		" OFFICE_CODE1   , " +
		" OFFICE_CODE2   , " +
		" TDEPT_EN       , " +
		" DESCRSHORT_EN  , " +
		" TDEPT_TH       , " +
		" DESCRSHORT_TH  , " +
		" STATUS         , " +
		" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public Long insertEcertHROfficeCode(EcertHROfficeCode ecertHROfficeCode) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_ECERT_HR_OFFICE_CODE, Statement.RETURN_GENERATED_KEYS);
				ps.setDate(1, ecertHROfficeCode.getEffectiveDate() != null ? new Date(ecertHROfficeCode.getEffectiveDate().getTime()) : null);
				ps.setString(2, ecertHROfficeCode.getType());
				ps.setString(3, ecertHROfficeCode.getOfficeCode1());
				ps.setString(4, ecertHROfficeCode.getOfficeCode2());
				ps.setString(5, ecertHROfficeCode.getTdeptEn());
				ps.setString(6, ecertHROfficeCode.getDescrshortEn());
				ps.setString(7, ecertHROfficeCode.getTdeptTh());
				ps.setString(8, ecertHROfficeCode.getDescrshortTh());
				ps.setString(9, ecertHROfficeCode.getStatus());
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		return id;
	}
}
