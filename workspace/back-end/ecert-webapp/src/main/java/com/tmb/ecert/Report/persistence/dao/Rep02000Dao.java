package com.tmb.ecert.Report.persistence.dao;

import java.math.BigDecimal;
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

import com.tmb.ecert.Report.persistence.vo.Rep02000FormVo;
import com.tmb.ecert.Report.persistence.vo.Rep02000Vo;
import com.tmb.ecert.constant.DateConstant;

@Repository
public class Rep02000Dao {
	private Logger log = LoggerFactory.getLogger(Rep02000Dao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String SQL_ECERT_REQUEST_FORM = " SELECT " + 
			"			 c.NAME AS CUSTSEGMENT_DESC,c.CODE AS CUSTSEGMENT_CODE,a.DEPARTMENT AS DEPARTMENT,count(*) AS CUSTSEGMENT_COUNT ," + 
			"			 sum(a.AMOUNT_DBD) AS AMOUNT_DBD  ," + 
			"			 sum(a.AMOUNT_TMB) AS AMOUNT_TMB  ," + 
			"			 sum(a.AMOUNT_TMB+a.AMOUNT_DBD) AS AMOUNT_TATOL  " + 
			"			 FROM ECERT_REQUEST_FORM a   " + 
			"			 INNER JOIN ECERT_LISTOFVALUE c " + 
			"			 on a.CUSTSEGMENT_CODE = c.CODE " +
			"            WHERE (a.STATUS = '10003' OR a.STATUS = '10004' OR a.STATUS = '10007' " + 
			"            OR a.STATUS = '10008' OR a.STATUS = '10009' OR a.STATUS = '10010')" ;
	
	public int getCountCertificate(Rep02000FormVo formVo) {
		StringBuilder sql = new StringBuilder(" SELECT count(*) AS COUNT FROM ECERT_REQUEST_FORM a WHERE (a.STATUS = '10009' OR a.STATUS = '10010') AND a.CUSTSEGMENT_CODE = '20003' ");
		int count = 0;
		count = jdbcTemplate.queryForObject(sql.toString(), int.class);
		return count;
	}	

	
	public List<Rep02000Vo> getData(Rep02000FormVo formVo) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		List<Object> params = new ArrayList<>();
		List<Rep02000Vo> rep02000VoList = new ArrayList<Rep02000Vo>();
		
//		if (StringUtils.isNotBlank(formVo.getDateForm())) {
//			sql.append(" AND  a.REQUEST_DATE >= ? ");
//			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateForm());
//			params.add(date);
//		}
//		if (StringUtils.isNotBlank(formVo.getDateTo())) {
//			sql.append(" AND  a.REQUEST_DATE <= ? ");
//			Date date = DateConstant.convertStrDDMMYYYYToDate(formVo.getDateTo());
//			params.add(date);
//		}
		
		sql.append("  GROUP BY c.NAME,c.CODE,a.DEPARTMENT ORDER BY c.CODE ");
		rep02000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep02000RowMapper);
		
		return rep02000VoList;
	}
	

	 private RowMapper<Rep02000Vo> rep02000RowMapper = new RowMapper<Rep02000Vo>() {
	    	@Override
	    	public Rep02000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
	    		Rep02000Vo vo = new Rep02000Vo();
	    		
	    		vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC")); 
	    		vo.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE"));
	    		vo.setCustsegmentCount(rs.getInt("CUSTSEGMENT_COUNT")); 
	    		
	    		vo.setCertificate(0);
	    		vo.setCopyGuarantee(0);
	    		
	    		vo.setAmountDbd(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_DBD")));  
	    		vo.setAmountTmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));  
	    		vo.setTotalAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TATOL"))); 
	    		
	    		vo.setDepartment("DEPARTMENT");
	    		vo.setSuccess(0);
	    		vo.setFail(0);
	    		                
	    		return vo;                                                         
	    	}
	 };
	 
	 public Float convertBigDecimalToLong(BigDecimal bigdecimal) {
		 return (bigdecimal!=null)?bigdecimal.floatValue():0f;
	 }
	 
	 public BigDecimal convertBigDecimalToZero(BigDecimal bigdecimal) {
		 return (bigdecimal!=null)?bigdecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN):BigDecimal.ZERO;
	 }

}





