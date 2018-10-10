package com.tmb.ecert.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.ListOfValue;

@Repository
public class ListOfValueDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String template = " SELECT * FROM ECERT_LISTOFVALUE WHERE 1=1 AND STATUS=0 ";

	public List<ListOfValue> lovAllType() {
		StringBuilder sql = new StringBuilder(template);
		sql.append(" ORDER BY TYPE ASC ");
		List<ListOfValue> result = jdbcTemplate.query(sql.toString(), typeMapper);
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

	private RowMapper<ListOfValue> typeMapper = new RowMapper<ListOfValue>() {
		@Override
		public ListOfValue mapRow(ResultSet rs, int arg1) throws SQLException {
			ListOfValue list = new ListOfValue();
			list.setType(rs.getInt("TYPE"));
			return list;
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
			list.setGlType(rs.getString("GL_TYPE"));
			list.setTranCode(rs.getString("TRNCODE"));
			list.setAccountType(rs.getString("ACCTYPE"));
			list.setStatus(rs.getInt("STATUS"));
			list.setAccountNo(rs.getString("ACCTNO"));
			return list;
		}
	};

}
