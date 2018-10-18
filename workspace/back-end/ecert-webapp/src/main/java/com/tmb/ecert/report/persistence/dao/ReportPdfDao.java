package com.tmb.ecert.report.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.report.persistence.vo.RpCertificateVo;
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

	public List<RpCertificateVo> certificate(Long id) {
		List<RpCertificateVo> rpCertificateVoList = new ArrayList<RpCertificateVo>();
		List<Object> valueList = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT R.REQFORM_ID,R.CERTIFICATE_CODE,C.CERTIFICATE,R.OTHER,R.TOTALNUMBER  ");
		sql.append(" FROM ECERT_REQUEST_CERTIFICATE R ");
		sql.append(" INNER JOIN ECERT_CERTIFICATE C ");
		sql.append(" ON R.CERTIFICATE_CODE = C.CODE ");
		sql.append(" WHERE 1 = 1 ");

		if (BeanUtils.isNotEmpty(id)) {
			sql.append(" AND R.REQFORM_ID = ? ");
			valueList.add(id);
		}

		sql.append(" ORDER BY R.CERTIFICATE_CODE ASC ");

		rpCertificateVoList = jdbcTemplate.query(sql.toString(), certificateMapping);
		return rpCertificateVoList;
	}

	private RowMapper<RpCertificateVo> certificateMapping = new RowMapper<RpCertificateVo>() {
		@Override
		public RpCertificateVo mapRow(ResultSet rs, int arg1) throws SQLException {
			RpCertificateVo vo = new RpCertificateVo();
			vo.setReqId(rs.getLong("REQFORM_ID"));
			vo.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
			vo.setCertificate(rs.getString("CERTIFICATE"));
			vo.setOther(rs.getString("OTHER"));
			vo.setTotalNumber(rs.getInt("TOTALNUMBER"));
			return vo;
		}

	};

}
