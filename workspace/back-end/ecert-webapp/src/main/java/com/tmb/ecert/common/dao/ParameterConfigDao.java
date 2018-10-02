package com.tmb.ecert.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.ParameterConfig;

@Repository
public class ParameterConfigDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static String SQL = " SELECT P.* FROM ECERT_PARAMETER_CONFIG P WHERE 1=1 ";
	
	public List<ParameterConfig> findAll() {
		StringBuilder sql = new StringBuilder(SQL);		
		return jdbcTemplate.query(sql.toString(), mapping);
	}
	
	private RowMapper<ParameterConfig> mapping = new RowMapper<ParameterConfig> () {
		@Override
		public ParameterConfig mapRow(ResultSet rs, int arg1) throws SQLException {
			ParameterConfig row = new ParameterConfig();
			row.setParameterconfigId(rs.getLong("PARAMETERCONFIG_ID"));
			row.setPropertyName(rs.getString("PROPERTY_NAME"));
			row.setPropertyValue(rs.getString("PROPERTY_VALUE"));
			row.setUpdatedById(rs.getString("UPDATED_BY_ID"));
			row.setUpdatedByName(rs.getString("UPDATED_BY_NAME"));
			row.setUpdatedDatetime(rs.getDate("UPDATED_DATETIME"));
			return row;
		}
	};
}
