package com.tmb.ecert.batchjob.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.AuditLog;

@Repository
public class AuditLogDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<AuditLog> findAuditLogWithDays(int days) {		
		return jdbcTemplate.query("SELECT * FROM ECERT_AUDIT_LOG WHERE CREATED_DATETIME < GETDATE()  - ?",new Object[] {days}, mapping);
	}
	
	public int deleteAuditlog(int days) {
		return jdbcTemplate.update("DELETE FROM ECERT_AUDIT_LOG WHERE CREATED_DATETIME < GETDATE()  - ?", days);
	}
	
	public List<AuditLog> findAuditLogByActionCode(String actionCode) {
		return jdbcTemplate.query("SELECT * FROM ECERT_AUDIT_LOG WHERE ACTION_CODE = ?",new Object[] {actionCode}, mapping);
	}
	
	private RowMapper<AuditLog> mapping = new RowMapper<AuditLog> () {
		@Override
		public AuditLog mapRow(ResultSet rs, int arg1) throws SQLException {
			AuditLog row = new AuditLog();
			row.setAuditLogId(rs.getLong("AUDITLOG_ID"));
			row.setActionCode(rs.getString("ACTION_CODE"));
			row.setDescription(rs.getString("DESCRIPTION "));
			row.setCreateById(rs.getString("CREATED_BY_ID"));
			row.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			row.setCreateDatetime(rs.getDate("CREATED_DATETIME"));
			return row;
		}
	};
}
