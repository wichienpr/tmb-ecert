package com.tmb.ecert.auditlog.persistence.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.auditlog.persistence.vo.Adl01000FormVo;
import com.tmb.ecert.auditlog.persistence.vo.Adl01000Vo;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.common.constant.DateConstant;

import th.co.baiwa.buckwaframework.common.bean.DatatableSort;
import th.co.baiwa.buckwaframework.common.util.DatatableUtils;

@Repository
public class AuditLogsDao {
	private Logger log = LoggerFactory.getLogger(AuditLogsDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String QUERY_INSERT_AUDITLOG = " INSERT INTO ECERT_AUDIT_LOG ( " +
			" ACTION_CODE," + 
			" DESCRIPTION," + 
			" CREATED_BY_ID," + 
			" CREATED_BY_NAME," + 
			" CREATED_DATETIME)" + 
			" VALUES (?, ?, ?, ?, ?) ";
	
	public Long insertAuditLog(AuditLog auditLog) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(QUERY_INSERT_AUDITLOG, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, auditLog.getActionCode());
				ps.setString(2, auditLog.getDescription());
				ps.setString(3, auditLog.getCreateById());
				ps.setString(4, auditLog.getCreatedByName());
				ps.setTimestamp(5,new Timestamp(new Date().getTime()));
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		return id;
	}
 
	
	
	public List<Adl01000Vo> getDataAdl01000(Adl01000FormVo formVo) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		List<Adl01000Vo> adl01000VoList = new ArrayList<Adl01000Vo>();
		
		    sql.append(" SELECT a.*,b.NAME AS ACTION_DESC FROM ECERT_AUDIT_LOG a LEFT JOIN ECERT_LISTOFVALUE b ON a.ACTION_CODE=b.CODE WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getCreatedById())) {
			sql.append(" AND a.CREATED_BY_ID = ?");
			params.add(formVo.getCreatedById());
		}
		if (StringUtils.isNotBlank(formVo.getActionCode())) {
			sql.append(" AND a.ACTION_CODE = ?");
			params.add(formVo.getActionCode());
		}
		
		if(!formVo.getSort().isEmpty()) {
			sql.append( "ORDER BY ");
			List<String> orders = new ArrayList<>();
			for(DatatableSort item : formVo.getSort()) {
				orders.add("a." + item.getColumn() + " " + item.getOrder());
			}
			sql.append(StringUtils.join(orders,", "));
		}else {
			// default order
			sql.append(" ORDER BY a.CREATED_DATETIME DESC ");			
		}
		
		
		log.info("sqladl01000 : {}",sql.toString());
		adl01000VoList = jdbcTemplate.query(DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()), params.toArray(), adl01000RowMapper);
		
		return adl01000VoList;
	}
	  

	 private RowMapper<Adl01000Vo> adl01000RowMapper = new RowMapper<Adl01000Vo>() {
	    	@Override
	    	public Adl01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
	    		Adl01000Vo vo = new Adl01000Vo();
	    		vo.setId(rs.getLong("AUDITLOG_ID"));
	    		vo.setCreatedDatetime(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getTimestamp("CREATED_DATETIME")));
	    		vo.setCreatedById(rs.getString("CREATED_BY_ID"));
	    		vo.setCreatedByName(rs.getString("CREATED_BY_NAME"));
	    		vo.setActionCode(rs.getString("ACTION_CODE"));
	    		vo.setActionDesc(rs.getString("ACTION_DESC"));
	    		vo.setDescription(rs.getString("DESCRIPTION"));
	    		
	    		return vo;                                                         
	    	}
	 };


	public int countAudit(Adl01000FormVo formVo) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		
		    sql.append(" SELECT a.*,b.NAME AS ACTION_DESC FROM ECERT_AUDIT_LOG a LEFT JOIN ECERT_LISTOFVALUE b ON a.ACTION_CODE=b.CODE WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getCreatedById())) {
			sql.append(" AND a.CREATED_BY_ID = ?");
			params.add(formVo.getCreatedById());
		}
		if (StringUtils.isNotBlank(formVo.getActionCode())) {
			sql.append(" AND a.ACTION_CODE = ?");
			params.add(formVo.getActionCode());
		}
		
		log.info("sqladl01000 : {}",sql.toString());
		BigDecimal rs = jdbcTemplate.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class, params.toArray());
		
		return rs.intValue();
	}


	public List<Adl01000Vo> getExportData(Adl01000FormVo formVo) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		List<Adl01000Vo> adl01000VoList = new ArrayList<Adl01000Vo>();
		
		    sql.append(" SELECT a.*,b.NAME AS ACTION_DESC FROM ECERT_AUDIT_LOG a LEFT JOIN ECERT_LISTOFVALUE b ON a.ACTION_CODE=b.CODE WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(formVo.getDateForm())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) >= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getDateTo())) {
			sql.append(" AND  CAST(a.CREATED_DATETIME as DATE) <= ? ");
			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
			params.add(date);
		}
		if (StringUtils.isNotBlank(formVo.getCreatedById())) {
			sql.append(" AND a.CREATED_BY_ID = ?");
			params.add(formVo.getCreatedById());
		}
		if (StringUtils.isNotBlank(formVo.getActionCode())) {
			sql.append(" AND a.ACTION_CODE = ?");
			params.add(formVo.getActionCode());
		}
		
		sql.append(" ORDER BY a.CREATED_DATETIME ");
		
		log.info("getExportData : {}",sql.toString());
		adl01000VoList = jdbcTemplate.query(sql.toString() , params.toArray(), adl01000RowMapper);
		
		return adl01000VoList;
	}
	
}





