package com.tmb.ecert.batchjob.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;

@Repository
public class JobMonitoringDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String QUERY_INSERT_ECERT_JOB_MONITORING = " INSERT INTO ECERT_JOB_MONITORING ( " +
			" JOBTYPE_CODE,  	 	" + 
			" START_DATE,  	 		" + 
			" STOP_DATE, 	 		" + 
			" ENDOFDATE, 		 	" + 
			" STATUS,  		 		" + 
			" ERROR_DESC,  	 		" + 
			" RERUN_NUMBER, 	 	" + 
			" RERUN_BY_ID, 	 		" + 
			" RERUN_BY_NAME, 	 	" + 
			" RERUN_DATETIME ) 		" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	public Long insertEcertJobMonitoring(EcertJobMonitoring ecertJobMonitoring) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(QUERY_INSERT_ECERT_JOB_MONITORING, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, ecertJobMonitoring.getJobTypeCode());
				ps.setTimestamp(2, ecertJobMonitoring.getStartDate() != null ? new Timestamp(ecertJobMonitoring.getStartDate().getTime()) : null);
				ps.setTimestamp(3, ecertJobMonitoring.getStopDate() != null ? new Timestamp(ecertJobMonitoring.getStopDate().getTime()) : null);
				ps.setTimestamp(4, ecertJobMonitoring.getEndOfDate() != null ? new Timestamp(ecertJobMonitoring.getEndOfDate().getTime()) : null);
				ps.setString(5, ecertJobMonitoring.getStatus()!=null ? ecertJobMonitoring.getStatus() : null);
				ps.setString(6, ecertJobMonitoring.getErrorDesc()!=null ? ecertJobMonitoring.getErrorDesc() : null);
				ps.setInt(7, ecertJobMonitoring.getRerunNumber()!=null ? ecertJobMonitoring.getRerunNumber() : null);
				ps.setString(8, ecertJobMonitoring.getRerunById()!=null ? ecertJobMonitoring.getRerunById() : null);
				ps.setString(9, ecertJobMonitoring.getRerunByName()!=null ? ecertJobMonitoring.getRerunByName() : null);
				ps.setTimestamp(10, ecertJobMonitoring.getRerunDatetime() != null ? new Timestamp(ecertJobMonitoring.getRerunDatetime().getTime()) : null);
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		return id;
	}

}
