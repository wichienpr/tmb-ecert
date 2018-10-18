package com.tmb.ecert.report.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.report.persistence.vo.RpVatVo;

@Repository
public class ReportPdfDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<RpVatVo> vat() {
		List<RpVatVo> rpVatVoList = new ArrayList<RpVatVo>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT P.PROPERTY_VALUE  ");
		sql.append(" FROM ECERT_PARAMETER_CONFIG P ");
		sql.append(" WHERE P.PROPERTY_NAME='vat.percent' ");
		rpVatVoList = jdbcTemplate.query(sql.toString(), vatMapping);
		return rpVatVoList;
	}
	
	
	private RowMapper<RpVatVo> vatMapping = new RowMapper<RpVatVo>() {
		@Override
		public RpVatVo mapRow(ResultSet rs, int arg1) throws SQLException {
			RpVatVo vo = new RpVatVo();
			vo.setVat(rs.getString("PROPERTY_VALUE"));
			return vo;
		}

	};

}
