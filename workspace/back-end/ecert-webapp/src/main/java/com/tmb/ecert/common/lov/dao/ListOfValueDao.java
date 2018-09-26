package com.tmb.ecert.common.lov.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.domain.LabelValueBean;

@Repository
public class ListOfValueDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<LabelValueBean> lov(String type) {
		String query = "SELECT  * FROM ECERT_LISTOFVALUE WHERE TYPE = ? ";
					
		List<Object> params = new ArrayList<>();				
		params.add(type);
		
		StringBuilder sql = new StringBuilder(query);		
		sql.append("ORDER BY SEQUENCE ASC");
		List<LabelValueBean> list = jdbcTemplate.query(sql.toString(), params.toArray(), lovTypeRowmapper);
		
		return list;

	}
	private RowMapper<LabelValueBean> lovTypeRowmapper = new RowMapper<LabelValueBean>() {
		@Override
		public LabelValueBean mapRow(ResultSet rs, int arg1) throws SQLException {
			
			return new LabelValueBean(rs.getString("NAME"), rs.getString("CODE"));
		}
	};
	


}
