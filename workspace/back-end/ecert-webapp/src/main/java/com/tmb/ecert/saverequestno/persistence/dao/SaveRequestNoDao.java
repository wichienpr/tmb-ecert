package com.tmb.ecert.saverequestno.persistence.dao;

import java.math.BigDecimal;
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

import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000FormVo;
import com.tmb.ecert.saverequestno.persistence.vo.Srn01000Vo;

import th.co.baiwa.buckwaframework.common.util.DatatableUtils;

@Repository
public class SaveRequestNoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	public List<Srn01000Vo> findReqByTmbReqNo(Srn01000FormVo formVo) {
		logger.info("findReq_Dao");
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT  H.REQFORM_ID ,H.REQUEST_DATE,TMB_REQUESTNO,H.REF1,H.REF2,H.AMOUNT,H.STATUS ,H.ORGANIZE_ID,H.COMPANY_NAME ,C.NAME AS TYPE_DESC,L.NAME AS STATUS_NAME ");
		sql.append(" FROM ECERT_REQUEST_FORM H ");
		sql.append(" INNER JOIN ECERT_LISTOFVALUE L ");
		sql.append(" ON H.STATUS = L.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ");
		sql.append(" ON H.CERTYPE_CODE = C.CODE ");
		sql.append(" WHERE 1 = 1 AND H.DELETE_FLAG = 0  ");
		sql.append(" AND H.STATUS = "+StatusConstant.WAIT_SAVE_REQUEST);

		if (StringUtils.isNotBlank(formVo.getTmbReqNo())) {
			sql.append(" AND H.TMB_REQUESTNO LIKE ? ");
			valueList.add("%" + StringUtils.trim(formVo.getTmbReqNo().replaceAll("-", StringUtils.EMPTY)) + "%");
		}

		sql.append(" ORDER BY H.TMB_REQUESTNO DESC");

		// srn01000VoList = jdbcTemplate.query(sql.toString(), valueList.toArray(),
		// reqFormByStatusMapping);
		srn01000VoList = jdbcTemplate.query(
				DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()),
				valueList.toArray(), reqFormByStatusMapping);
		return srn01000VoList;
	}

	public int countFindReqByTmbReqNoDataTable(Srn01000FormVo formVo) {
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT  H.REQFORM_ID ,H.REQUEST_DATE,TMB_REQUESTNO,H.REF1,H.REF2,H.AMOUNT,H.STATUS ,H.ORGANIZE_ID,H.COMPANY_NAME ,C.NAME AS TYPE_DESC,L.NAME AS STATUS_NAME ");
		sql.append(" FROM ECERT_REQUEST_FORM H ");
		sql.append(" INNER JOIN ECERT_LISTOFVALUE L ");
		sql.append(" ON H.STATUS = L.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ");
		sql.append(" ON H.CERTYPE_CODE = C.CODE ");
		sql.append(" WHERE 1 = 1 AND H.DELETE_FLAG = 0  ");
		sql.append(" AND H.STATUS = "+StatusConstant.WAIT_SAVE_REQUEST);

		if (StringUtils.isNotBlank(formVo.getTmbReqNo())) {
			sql.append(" AND H.TMB_REQUESTNO LIKE ? ");
			valueList.add("%" + StringUtils.trim(formVo.getTmbReqNo().replaceAll("-", StringUtils.EMPTY)) + "%");
		}

		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class,
				valueList.toArray());

		return rs.intValue();
	}
	
	public List<Srn01000Vo> findReqByStatus(Srn01000FormVo formVo) {
		logger.info("findReqByStatus_Dao");
		logger.info(formVo.toString());
		List<Srn01000Vo> srn01000VoList = new ArrayList<Srn01000Vo>();
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT  H.REQFORM_ID ,H.REQUEST_DATE,TMB_REQUESTNO,H.REF1,H.REF2,H.AMOUNT,H.STATUS ,H.ORGANIZE_ID,H.COMPANY_NAME ,C.NAME AS TYPE_DESC,L.NAME AS STATUS_NAME ");
		sql.append(" FROM ECERT_REQUEST_FORM H ");
		sql.append(" INNER JOIN ECERT_LISTOFVALUE L ");
		sql.append(" ON H.STATUS = L.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ");
		sql.append(" ON H.CERTYPE_CODE = C.CODE ");
		sql.append(" WHERE 1 = 1 AND H.DELETE_FLAG = 0  ");

		if (StringUtils.isNotBlank(formVo.getStatus())) {
			sql.append(" AND H.STATUS = ? ");
			valueList.add(formVo.getStatus());
		}
		sql.append(" ORDER BY H.TMB_REQUESTNO DESC");
		srn01000VoList = jdbcTemplate.query(sql.toString(), valueList.toArray(), reqFormByStatusMapping);

		return srn01000VoList;
	}

	public int countFindReqByStatusDataTable(Srn01000FormVo formVo) {
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT  H.REQFORM_ID ,H.REQUEST_DATE,TMB_REQUESTNO,H.REF1,H.REF2,H.AMOUNT,H.STATUS ,H.ORGANIZE_ID,H.COMPANY_NAME ,C.NAME AS TYPE_DESC,L.NAME AS STATUS_NAME ");
		sql.append(" FROM ECERT_REQUEST_FORM H ");
		sql.append(" INNER JOIN ECERT_LISTOFVALUE L ");
		sql.append(" ON H.STATUS = L.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ");
		sql.append(" ON H.CERTYPE_CODE = C.CODE ");
		sql.append(" WHERE 1 = 1 AND H.DELETE_FLAG = 0 ");

		if (StringUtils.isNotBlank(formVo.getStatus())) {
			sql.append(" AND H.STATUS = ? ");
			valueList.add(formVo.getStatus());
		}

		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class,
				valueList.toArray());

		return rs.intValue();
	}

	private RowMapper<Srn01000Vo> reqFormByStatusMapping = new RowMapper<Srn01000Vo>() {

		@Override
		public Srn01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
			Srn01000Vo vo = new Srn01000Vo();

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
