package com.tmb.ecert.batchmonitor.persistence.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.common.constant.DateConstant;

import th.co.baiwa.buckwaframework.common.bean.DatatableSort;
import th.co.baiwa.buckwaframework.common.util.DatatableUtils;
import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;

@Repository
public class BatchMonitoringDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<Btm01000Vo> getListBatch(Btm01000FormVo form){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<Btm01000Vo> list = new ArrayList<>();
		
		sql.append(" 	SELECT A.JOBMONITORING_ID , A.JOBTYPE_CODE , B.NAME JOBTYPE_NAME ,A.START_DATE, A.STOP_DATE,A.ENDOFDATE,A.STATUS STATUS ,C.NAME STATUS_DESC ,A.ERROR_DESC,   " + 
				"   	A.RERUN_NUMBER,A.RERUN_BY_ID,A.RERUN_BY_NAME,A.RERUN_DATETIME  FROM ECERT_JOB_MONITORING A  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE B  ON A.JOBTYPE_CODE = B.CODE  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE C ON A.STATUS = C.CODE  WHERE 1=1  ");
		
		if(StringUtils.isNotBlank(form.getDateFrom())) {
//			sql.append(" AND CONVERT(NVARCHAR, A.START_DATE, 103) >= ? ");
			sql.append(" AND CAST(A.START_DATE as DATE) >= ? "); 
			Date date = DateConstant.convertStrDDMMYYYYToDate(form.getDateFrom());
			params.add(date);
		}
		if(StringUtils.isNotBlank(form.getDateTo())) {
//			sql.append("  AND CONVERT(NVARCHAR, A.START_DATE, 103) <= ? ");
			sql.append(" AND CAST(A.START_DATE as DATE) <= ? "); 
			Date date = DateConstant.convertStrDDMMYYYYToDate(form.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNotBlank(form.getBatchType())) {
			sql.append(" AND A.JOBTYPE_CODE = ? ");
			params.add(form.getBatchType());
		}
		if (StringUtils.isNotBlank(form.getOperationType())) {
			sql.append(" AND A.STATUS = ? ");
			params.add(form.getOperationType());
		}
		
		if(!form.getSort().isEmpty()) {
			sql.append( "ORDER BY ");
			List<String> orders = new ArrayList<>();
			for(DatatableSort item : form.getSort()) {
				if ("NAME".equals(item.getColumn())) {
					orders.add("B." + item.getColumn() + " " + item.getOrder());
				}else {
					orders.add("A." + item.getColumn() + " " + item.getOrder());
				}

			}
			sql.append(StringUtils.join(orders,", "));
		}else {
			// default order
			sql.append(" ORDER BY A.START_DATE ");		
		}
		
//		sql.append(" ORDER BY A.START_DATE ");
		
		list = jdbcTemplate.query(DatatableUtils.limitForDataTable(sql.toString(), form.getStart(), form.getLength()), params.toArray(), btm01000RowMapper);
		
//		list = jdbcTemplate.query(sql.toString(), params.toArray(), btm01000RowMapper);
		return list;
		
	}
	
	public int conutListBatch(Btm01000FormVo form){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
	
		sql.append(" 	SELECT A.JOBMONITORING_ID ,B.NAME JOBTYPE_CODE, B.NAME JOBTYPE_NAME , A.START_DATE, A.STOP_DATE,A.ENDOFDATE,A.STATUS STATUS ,C.NAME STATUS_DESC ,A.ERROR_DESC,   " + 
				"   	A.RERUN_NUMBER,A.RERUN_BY_ID,A.RERUN_BY_NAME,A.RERUN_DATETIME  FROM ECERT_JOB_MONITORING A  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE B  ON A.JOBTYPE_CODE = B.CODE  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE C ON A.STATUS = C.CODE  WHERE 1=1  ");
		
		if(StringUtils.isNotBlank(form.getDateFrom())) {
//			sql.append(" AND CONVERT(NVARCHAR, A.START_DATE, 103) >= ? ");
			sql.append(" AND CAST(A.START_DATE as DATE) >= ? "); 
			Date date = DateConstant.convertStrDDMMYYYYToDate(form.getDateFrom());
			params.add(date);
		}
		if(StringUtils.isNotBlank(form.getDateTo())) {
//			sql.append("  AND CONVERT(NVARCHAR, A.START_DATE, 103) <= ? ");
			sql.append(" AND CAST(A.START_DATE as DATE) <= ? "); 
			Date date = DateConstant.convertStrDDMMYYYYToDate(form.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNotBlank(form.getBatchType())) {
			sql.append(" AND A.JOBTYPE_CODE = ? ");
			params.add(form.getBatchType());
		}
		if (StringUtils.isNotBlank(form.getOperationType())) {
			sql.append(" AND A.STATUS = ? ");
			params.add(form.getOperationType());
		}
		
		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class, params.toArray());
//		list = jdbcTemplate.query(sql.toString(), params.toArray(), btm01000RowMapper);
		return rs.intValue();
		
	}
	
	private  RowMapper<Btm01000Vo> btm01000RowMapper = new RowMapper<Btm01000Vo>() {

		@Override
		public Btm01000Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Btm01000Vo vo = new Btm01000Vo();
			vo.setJobmonitoringId(rs.getInt("JOBMONITORING_ID"));
			vo.setJobtypeCode(rs.getString("JOBTYPE_CODE"));
//			vo.setStartDate(DateFormatUtils.format(rs.getTimestamp("START_DATE"), "dd/MM/yyyy HH:mm:SS"));
//			vo.setStartDate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("START_DATE")));
			vo.setStartDate(EcerDateUtils.formatLogDate(rs.getTimestamp("START_DATE")));
			vo.setStopDate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getTimestamp("STOP_DATE")));
			vo.setEndofdate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getTimestamp("ENDOFDATE")));
			vo.setStatus(rs.getString("STATUS"));
			vo.setErrorDesc(rs.getString("ERROR_DESC"));
			vo.setRerunNumber(rs.getInt("RERUN_NUMBER"));
			vo.setRerunById(rs.getString("RERUN_BY_ID"));
			vo.setRerunByName(rs.getString("RERUN_BY_NAME"));
			vo.setRerunDatetime(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getTimestamp("RERUN_DATETIME")));
			vo.setStatusDesc(rs.getString("STATUS_DESC"));
			vo.setJobtypeName(rs.getString("JOBTYPE_NAME"));
			
			return vo;
			
		}
		
	};
	
	public void updateRerunJobById(Btm01000Vo form, String username, String userID) {
		
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		sql.append(" 	UPDATE ECERT_JOB_MONITORING SET RERUN_NUMBER =  ? ,RERUN_BY_ID =  ? ,RERUN_BY_NAME = ? ");
		sql.append(" 	,RERUN_DATETIME = ?  WHERE JOBMONITORING_ID = ?  ");
		
		params.add(1);
		params.add(userID);
		params.add(username);
		params.add(new Date());
		params.add(form.getJobmonitoringId());
		
		jdbcTemplate.update(sql.toString(), params.toArray());
		
	}

}
