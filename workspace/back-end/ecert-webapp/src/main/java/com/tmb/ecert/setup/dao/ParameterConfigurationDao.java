package com.tmb.ecert.setup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.ParameterConfig;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.setup.vo.Sup01100FormVo;

@Repository
public class ParameterConfigurationDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_ROLEMANAGEMENT);
	
	
	
	public List<ParameterConfig> getParameter(){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<ParameterConfig> list = new ArrayList<ParameterConfig>();
		
		sql.append(" SELECT PARAMETERCONFIG_ID ,PROPERTY_NAME,PROPERTY_VALUE FROM ECERT_PARAMETER_CONFIG");
		
		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup02000RowMapper);
		return list;
		
	}
	
	private  RowMapper<ParameterConfig> sup02000RowMapper = new RowMapper<ParameterConfig>() {

		@Override
		public ParameterConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			ParameterConfig vo = new ParameterConfig();
			vo.setParameterconfigId(rs.getLong("PARAMETERCONFIG_ID"));
			vo.setPropertyName(rs.getString("PROPERTY_NAME"));
			vo.setPropertyValue(rs.getString("PROPERTY_VALUE"));
			return vo;
		}
		
	};

}
