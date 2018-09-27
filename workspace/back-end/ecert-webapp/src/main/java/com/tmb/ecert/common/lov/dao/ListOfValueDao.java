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
import com.tmb.ecert.domain.ListOfValue;

@Repository
public class ListOfValueDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String template = " SELECT * FROM ECERT_LISTOFVALUE WHERE 1=1 ";

	public List<LabelValueBean> lov(String type) {
		String query = "SELECT  * FROM ECERT_LISTOFVALUE WHERE TYPE = ? ";

		List<Object> params = new ArrayList<>();
		params.add(type);

		StringBuilder sql = new StringBuilder(query);
		sql.append("ORDER BY SEQUENCE ASC");
		List<LabelValueBean> list = jdbcTemplate.query(sql.toString(), params.toArray(), lovTypeRowmapper);

		return list;

	}

	public List<ListOfValue> lovAllType() {
		String str = " SELECT DISTINCT TYPE FROM ECERT_LISTOFVALUE WHERE 1=1 ";
		StringBuilder sql = new StringBuilder(template);
		sql.append(" ORDER BY TYPE ASC ");
		List<ListOfValue> result = jdbcTemplate.query(sql.toString(), lovMapper);
		return result;
	}

	public List<ListOfValue> lovByType(Integer type) {
		StringBuilder sql = new StringBuilder(template);
		sql.append(" AND TYPE = ? ");
		sql.append(" ORDER BY SEQUENCE ASC ");

		List<Object> params = new ArrayList<>();
		params.add(type);

		List<ListOfValue> result = jdbcTemplate.query(sql.toString(), params.toArray(), lovMapper);

		return result;
	}

	private RowMapper<LabelValueBean> lovTypeRowmapper = new RowMapper<LabelValueBean>() {
		@Override
		public LabelValueBean mapRow(ResultSet rs, int arg1) throws SQLException {

			return new LabelValueBean(rs.getString("NAME"), rs.getString("CODE"));
		}
	};

	private RowMapper<ListOfValue> lovMapper = new RowMapper<ListOfValue>() {
		@Override
		public ListOfValue mapRow(ResultSet rs, int arg1) throws SQLException {
			ListOfValue list = new ListOfValue();
			list.setCode(rs.getString("CODE"));
			list.setType(rs.getInt("TYPE"));
			list.setTypeDesc(rs.getString("TYPE_DESC"));
			list.setName(rs.getString("NAME"));
			list.setSequence(rs.getInt("SEQUENCE"));
			return list;
		}
	};

}
