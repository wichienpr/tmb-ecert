package com.tmb.ecert.batchmonitor.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

@Repository
public class BatchMonitoringDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<Btm01000Vo> getListBatch(Btm01000FormVo form){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<Btm01000Vo> list = new ArrayList<>();
		
		sql.append(" 	SELECT A.JOBMONITORING_ID ,B.NAME JOBTYPE_CODE,A.START_DATE, A.STOP_DATE,A.ENDOFDATE,A.STATUS STATUS ,C.NAME STATUS_DESC ,A.ERROR_DESC,   " + 
				"   	A.RERUN_NUMBER,A.RERUN_BY_ID,A.RERUN_BY_NAME,A.RERUN_DATETIME  FROM ECERT_JOB_MONITORING A  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE B  ON A.JOBTYPE_CODE = B.CODE  "
				+ "   	LEFT JOIN ECERT_LISTOFVALUE C ON A.STATUS = C.CODE  WHERE 1=1  ");
		
		if(StringUtils.isNotBlank(form.getDateFrom())) {
			sql.append(" AND CAST( A.START_DATE as DATE) >= ? ");
			params.add(form.getDateFrom());
		}
		if(StringUtils.isNotBlank(form.getDateTo())) {
			sql.append("  AND CAST( A.START_DATE as DATE) <= ? ");
			params.add(form.getDateTo());
		}
		if (StringUtils.isNotBlank(form.getBatchJobType())) {
			sql.append(" AND A.JOBTYPE_CODE = ? ");
			params.add(form.getBatchJobType());
		}
		if (StringUtils.isNotBlank(form.getBatchStatus())) {
			sql.append(" AND A.STATUS = ? ");
			params.add(form.getBatchStatus());
		}
	
		list = jdbcTemplate.query(sql.toString(), params.toArray(), btm01000RowMapper);
		return list;
		
	}
	
	private  RowMapper<Btm01000Vo> btm01000RowMapper = new RowMapper<Btm01000Vo>() {

		@Override
		public Btm01000Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Btm01000Vo vo = new Btm01000Vo();
			vo.setJobmonitoringId(rs.getInt("JOBMONITORING_ID"));
			vo.setJobtypeCode(rs.getString("JOBTYPE_CODE"));
			vo.setStartDate(DateFormatUtils.format(rs.getTimestamp("START_DATE"), "dd/MM/yyyy HH:mm:SS"));
//			vo.setStartDate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("START_DATE")));
			vo.setStopDate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("STOP_DATE")));
			vo.setEndofdate(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("ENDOFDATE")));
			vo.setStatus(rs.getString("STATUS"));
			vo.setErrorDesc(rs.getString("ERROR_DESC"));
			vo.setRerunNumber(rs.getInt("RERUN_NUMBER"));
			vo.setRerunById(rs.getString("RERUN_BY_ID"));
			vo.setRerunByName(rs.getString("RERUN_BY_NAME"));
			vo.setRerunDatetime(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("RERUN_DATETIME")));
			vo.setStatusDesc(rs.getString("STATUS_DESC"));
			
			return vo;
			
		}
		
	};

}
