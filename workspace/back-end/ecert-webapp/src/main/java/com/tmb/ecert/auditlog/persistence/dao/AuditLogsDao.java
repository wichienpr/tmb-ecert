package com.tmb.ecert.auditlog.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.auditlog.persistence.vo.Adl01000FormVo;
import com.tmb.ecert.auditlog.persistence.vo.Adl01000Vo;
import com.tmb.ecert.common.constant.DateConstant;

@Repository
public class AuditLogsDao {
	private Logger log = LoggerFactory.getLogger(AuditLogsDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<Adl01000Vo> getDataAdl01000(Adl01000FormVo formVo) {
		StringBuilder sql = new StringBuilder("");
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
		
		
		log.info("sqladl01000 : {}",sql.toString());
		adl01000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), adl01000RowMapper);
		
		return adl01000VoList;
	}
	  

	 private RowMapper<Adl01000Vo> adl01000RowMapper = new RowMapper<Adl01000Vo>() {
	    	@Override
	    	public Adl01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
	    		Adl01000Vo vo = new Adl01000Vo();
	    		vo.setId(rs.getLong("AUDITLOG_ID"));
	    		vo.setCreatedDatetime(DateConstant.convertDateToStrDDMMYYYYHHmm(rs.getDate("CREATED_DATETIME")));
	    		vo.setCreatedById(rs.getString("CREATED_BY_ID"));
	    		vo.setCreatedByName(rs.getString("CREATED_BY_NAME"));
	    		vo.setActionCode(rs.getString("ACTION_CODE"));
	    		vo.setActionDesc(rs.getString("ACTION_DESC"));
	    		vo.setDescription(rs.getString("DESCRIPTION"));
	    		
	    		return vo;                                                         
	    	}
	 };
	
}





