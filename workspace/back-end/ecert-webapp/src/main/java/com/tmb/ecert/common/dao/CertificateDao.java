package com.tmb.ecert.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.Certificate;

@Repository
public class CertificateDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static String template = " SELECT * FROM ECERT_CERTIFICATE WHERE 1=1 ";
	
	public List<Certificate> findAllTypeCode() {
		String str = " SELECT DISTINCT TYPE_CODE FROM ECERT_CERTIFICATE WHERE 1=1 ";
		StringBuilder sql = new StringBuilder(str);
		sql.append(" ORDER BY TYPE_CODE ASC ");
		
		List<Certificate> result = jdbcTemplate.query(sql.toString(), typeCodeMapper);
		return result;
	}
	
	public List<Certificate> findByTypeCode(String typeCode) {
		StringBuilder sql = new StringBuilder(template);
		sql.append(" AND TYPE_CODE = ? ");
		sql.append(" ORDER BY CODE ASC ");
		
		List<Object> params = new ArrayList<>();
		params.add(typeCode);
		
		List<Certificate> result = jdbcTemplate.query(sql.toString(), params.toArray(), cerMapper);
		
		return result;
	}
	
	private RowMapper<Certificate> typeCodeMapper = new RowMapper<Certificate>() {
		@Override
		public Certificate mapRow(ResultSet rs, int arg1) throws SQLException {
			Certificate list = new Certificate();
			list.setTypeCode(rs.getString("TYPE_CODE"));
			return list;
		}
	};
	
	private RowMapper<Certificate> cerMapper = new RowMapper<Certificate>() {
		@Override
		public Certificate mapRow(ResultSet rs, int arg1) throws SQLException {
			Certificate list = new Certificate();
			list.setCode(rs.getString("CODE"));
			list.setTypeCode(rs.getString("TYPE_CODE"));
			list.setTypeDesc(rs.getString("TYPE_DESC"));
			list.setCertificate(rs.getString("CERTIFICATE"));
			list.setFeeDbd(rs.getString("FEE_DBD"));
			list.setFeeTmb(rs.getString("FEE_TMB"));
			return list;
		}
	};
}
