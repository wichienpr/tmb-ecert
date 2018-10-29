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
import com.tmb.ecert.report.persistence.vo.RpReceiverVo;
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

		sql.append(" SELECT C.CERTIFICATE_CODE,C.OTHER ,C.TOTALNUMBER,E.CERTIFICATE AS CER ");
		sql.append(" FROM ECERT_REQUEST_CERTIFICATE c ");
		sql.append(" RIGHT JOIN ECERT_REQUEST_FORM R ");
		sql.append(" ON C.REQFORM_ID = R.REQFORM_ID ");
		sql.append(" LEFT JOIN ECERT_CERTIFICATE E ");
		sql.append(" ON C.CERTIFICATE_CODE = E.CODE ");
		if (BeanUtils.isNotEmpty(id)) {
			sql.append(" WHERE R.REQFORM_ID = ? AND R.DELETE_FLAG=0 ");
			valueList.add(id);
		}
		sql.append(" ORDER BY C.CERTIFICATE_CODE ASC ");

		rpCertificateVoList = jdbcTemplate.query(sql.toString(), valueList.toArray(), certificateMapping);

		return rpCertificateVoList;
	}

	private RowMapper<RpCertificateVo> certificateMapping = new RowMapper<RpCertificateVo>() {

		@Override
		public RpCertificateVo mapRow(ResultSet rs, int arg1) throws SQLException {
			RpCertificateVo vo = new RpCertificateVo();
			vo.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
			vo.setCertificate(rs.getString("CER"));
			vo.setOther(rs.getString("OTHER"));
			vo.setTotalNumber(rs.getInt("TOTALNUMBER"));
			return vo;
		}

	};
	
	
	public List<RpReceiverVo> receiver(Long id) {

		List<RpReceiverVo> rpReceiverVoList = new ArrayList<RpReceiverVo>();
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT  F.CREATED_BY_ID ,F.CREATED_BY_NAME,F.CREATED_BY_DEPARTMENT ,F.CREATED_BY_GROUP,F.CREATED_BY_BELONGTO,F.CREATED_BY_TEL,CREATED_BY_EMAIL ");
		sql.append(" FROM ECERT_REQUEST_FORM F ");

		if (BeanUtils.isNotEmpty(id)) {
			sql.append(" WHERE F.REQFORM_ID = ? AND F.DELETE_FLAG=0 ");
			valueList.add(id);
		}

		rpReceiverVoList = jdbcTemplate.query(sql.toString(), valueList.toArray(), receiverMapping);

		return rpReceiverVoList;
	}

	
	
	private RowMapper<RpReceiverVo> receiverMapping = new RowMapper<RpReceiverVo>() {

		@Override
		public RpReceiverVo mapRow(ResultSet rs, int arg1) throws SQLException {
			RpReceiverVo vo = new RpReceiverVo();
			
			vo.setCreatedById(rs.getString("CREATED_BY_ID"));
			vo.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			vo.setCreatedByDepartment(rs.getString("CREATED_BY_DEPARTMENT"));
			vo.setCreatedByGroup(rs.getString("CREATED_BY_GROUP"));
			vo.setCreatedByBelongto(rs.getString("CREATED_BY_BELONGTO"));
			vo.setCreatedByTel(rs.getString("CREATED_BY_TEL"));
			vo.setCreatedByEmail(rs.getString("CREATED_BY_EMAIL"));
			return vo;
		}

	};
	
	
	
}
