package com.tmb.ecert.setup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

@Repository
public class EmailTemplateDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String SELECT_EMAILTEMPLATE = " SELECT EMAILCONFIG_ID,NAME,STATUS FROM ECERT_EMAIL_CONFIG WHERE 1 = 1 ";
	
	private static String SELECT_EMAILDETAIL = "  SELECT [EMAILDETAIL_ID] ,[EMAILCONFIG_ID] ,[SUBJECT] ,[BODY],[FROM],[TO] ,[ATTACHFILE_FLAG]\r\n" + 
			" FROM [ecert].[dbo].[ECERT_EMAILCONFIG_DETAIL]  WHERE EMAILDETAIL_ID = ?  ";

	public List<Sup03000Vo> getEmailTemplate(Sup03000Vo form) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();

		List<Sup03000Vo> list = new ArrayList<>();

		sql.append(SELECT_EMAILTEMPLATE);

		if (StringUtils.isNotBlank(form.getName())) {
			sql.append(" AND  NAME LIKE ? ");
			params.add("%"+form.getName()+"%");
			
		}
		if (!(form.getStatus() == 2)) {
			sql.append(" AND  STATUS =  ? ");
			params.add(form.getStatus());
		}

		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup03000RowMapper);
		return list;

	}

	private RowMapper<Sup03000Vo> sup03000RowMapper = new RowMapper<Sup03000Vo>() {

		@Override
		public Sup03000Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sup03000Vo vo = new Sup03000Vo();
			vo.setEmailConfig_id(rs.getLong("EMAILCONFIG_ID"));
			vo.setName(rs.getString("NAME"));
			vo.setStatus(rs.getInt("STATUS"));
			return vo;
		}

	};
	
	public List<Sup03100Vo> getEmailDetail(Sup03000Vo form) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();

		List<Sup03100Vo> list = new ArrayList<>();

		sql.append(SELECT_EMAILDETAIL);
		params.add(form.getEmailConfig_id());

		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup03100RowMapper);
		return list;

	}

	private RowMapper<Sup03100Vo> sup03100RowMapper = new RowMapper<Sup03100Vo>() {

		@Override
		public Sup03100Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sup03100Vo vo = new Sup03100Vo();
			vo.setEmailDetail_id(rs.getInt("EMAILDETAIL_ID"));
			vo.setEmailConfig_id(rs.getInt("EMAILCONFIG_ID"));
			vo.setSubject(rs.getString("SUBJECT"));
			vo.setBody(rs.getString("BODY"));
			vo.setFrom(rs.getString("FROM"));
			vo.setTo(rs.getString("TO"));
			vo.setAttachFile_flag(rs.getInt("ATTACHFILE_FLAG"));
			
			return vo;
		}

	};

}
