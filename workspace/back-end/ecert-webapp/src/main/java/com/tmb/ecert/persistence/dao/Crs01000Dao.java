package com.tmb.ecert.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.persistence.vo.Crs01000Vo;

@Repository
public class Crs01000Dao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(Crs01000Dao.class);

	private final String SQL_ECERT_REQUEST_FORM = "SELECT * FROM ECERT_REQUEST_FORM;";

	public List<Crs01000Vo> findAllReqForm(Crs01000Vo dataAll) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		List<Object> valueList = new ArrayList<Object>();
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();

		crs01000VoList = jdbcTemplate.query(sql.toString(), valueList.toArray(), reqFormMapping);
		return crs01000VoList;
	}

	private RowMapper<Crs01000Vo> reqFormMapping = new RowMapper<Crs01000Vo>() {

		@Override
		public Crs01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Crs01000Vo vo = new Crs01000Vo();
			vo.setId(rs.getLong("REQFORM_ID"));
			vo.setReqDate(rs.getDate("REQUEST_DATE"));
			vo.setTmbReqNo(rs.getString("TMB_REQUESTNO"));
			vo.setRef1(rs.getString("REF1"));
			vo.setRef2(rs.getString("REF2"));
			vo.setAmount(rs.getBigDecimal("AMOUNT"));
			vo.setTypeDesc(rs.getString("STATUS"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setStatusName(rs.getString("STATUS"));
			vo.setStatusCode(rs.getString("STATUS"));

			return vo;
		}

	};

}
