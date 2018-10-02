package com.tmb.ecert.CheckRequestStatus.persistence.dao;

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

import com.tmb.ecert.CheckRequestStatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.CheckRequestStatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.constant.DateConstant;

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
			vo.setReqDateStr(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));
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

	public List<Crs01000Vo> findReqFormByStatus(Crs01000FormVo FormVo) {
		logger.info("findReqFormByStatus_Dao");
		logger.info(FormVo.toString());
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT  H.REQFORM_ID ,H.REQUEST_DATE,TMB_REQUESTNO,H.REF1,H.REF2,H.AMOUNT,H.STATUS ,H.ORGANIZE_ID,H.COMPANY_NAME ,H.STATUS,C.TYPE_DESC AS TYPE_DESC,L.NAME AS STATUS_NAME ");
		sql.append(" FROM ECERT_REQUEST_FORM H ");
		sql.append(" INNER JOIN ECERT_LISTOFVALUE L ");
		sql.append(" ON H.STATUS = L.CODE ");
		sql.append(" INNER JOIN ECERT_CERTIFICATE C ");
		sql.append(" ON H.STATUS = C.CODE ");
		sql.append(" WHERE 1 = 1 ");

		if (StringUtils.isNotBlank(FormVo.getStatus())) {
			sql.append(" AND H.STATUS = ? ");
			valueList.add(FormVo.getStatus());
		}

		if (StringUtils.isNotBlank(FormVo.getReqDate()) && StringUtils.isNotBlank(FormVo.getToReqDate())) {
			sql.append(" AND H.REQUEST_DATE BETWEEN convert(datetime, ? , 103) AND convert(datetime, ? , 103) ");
			valueList.add(FormVo.getReqDate());
			valueList.add(FormVo.getToReqDate());
		}
		
		if (StringUtils.isNotBlank(FormVo.getCompanyName())) {
			sql.append(" AND H.COMPANY_NAME = ? ");
			valueList.add(FormVo.getCompanyName());
		}
		
		if (StringUtils.isNotBlank(FormVo.getOrganizeId())) {
			sql.append(" AND H.ORGANIZE_ID = ? ");
			valueList.add(FormVo.getOrganizeId());
		}
		
		if (StringUtils.isNotBlank(FormVo.getTmbReqNo())) {
			sql.append(" AND H.TMB_REQUESTNO = ? ");
			valueList.add(FormVo.getTmbReqNo());
		}
		
		crs01000VoList = jdbcTemplate.query(sql.toString(), valueList.toArray(), reqFormByStatusMapping);
		return crs01000VoList;
	}

	private RowMapper<Crs01000Vo> reqFormByStatusMapping = new RowMapper<Crs01000Vo>() {

		@Override
		public Crs01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Crs01000Vo vo = new Crs01000Vo();

			vo.setId(rs.getLong("REQFORM_ID"));
			vo.setReqDate(rs.getDate("REQUEST_DATE"));
			vo.setReqDateStr(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));
			vo.setTmbReqNo(rs.getString("TMB_REQUESTNO"));
			vo.setRef1(rs.getString("REF1"));
			vo.setRef2(rs.getString("REF2"));
			vo.setAmount(rs.getBigDecimal("AMOUNT"));
			vo.setTypeDesc(rs.getString("TYPE_DESC"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setStatusName(rs.getString("STATUS_NAME"));
			vo.setStatusCode(rs.getString("STATUS"));
			return vo;
		}

	};

}
