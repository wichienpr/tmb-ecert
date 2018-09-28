package com.tmb.ecert.persistence.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.constant.DateConstant;
import com.tmb.ecert.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.persistence.vo.Rep01000Vo;

@Repository
public class Rep01000Repository {
	private Logger log = LoggerFactory.getLogger(Rep01000Repository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String SQL_ECERT_REQUEST_FORM = "SELECT a.*,b.CERTIFICATE AS CERTYPE_DESC," +
			" b.TYPE_CODE AS REQUEST_TYPE_CODE," +
			" b.TYPE_DESC AS REQUEST_TYPE_DESC," +        
			" c.NAME AS CUSTSEGMENT_DESC " +   
			" FROM ECERT_REQUEST_FORM a " + 
			" INNER JOIN ECERT_CERTIFICATE b on a.CERTYPE_CODE = b.CODE " + 
			" INNER JOIN ECERT_LISTOFVALUE c on a.CUSTSEGMENT_CODE = c.CODE ";
	
	
	public List<Rep01000Vo> getData(Rep01000FormVo formVo) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		List<Object> params = new ArrayList<>();
		List<Rep01000Vo> rep01000VoList = new ArrayList<Rep01000Vo>();
		sql.append(" ORDER BY a.REQUEST_DATE DESC ");
		rep01000VoList = jdbcTemplate.query(sql.toString(), params.toArray(), rep01000RowMapper);
		
		return rep01000VoList;
	}
	

	 private RowMapper<Rep01000Vo> rep01000RowMapper = new RowMapper<Rep01000Vo>() {
	    	@Override
	    	public Rep01000Vo mapRow(ResultSet rs, int arg1) throws SQLException {
	    		Rep01000Vo vo = new Rep01000Vo();
	    		vo.setId(rs.getLong("REQFORM_ID"));
	    		vo.setRequestDate(DateConstant.convertDateToStrDDMMYYYY(rs.getDate("REQUEST_DATE")));                   
	    		vo.setTmbRequestno(rs.getString("TMB_REQUESTNO"));                 
	    		vo.setOrganizeId(rs.getString("ORGANIZE_ID"));                     
	    		vo.setCompanyName(rs.getString("COMPANY_NAME")); 
	    		
	    		vo.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE")); 
	    		vo.setCustsegmentDesc(rs.getString("CUSTSEGMENT_DESC")); 
	    		
	    		vo.setRequestTypeCode(rs.getString("REQUEST_TYPE_CODE"));  
	    		vo.setRequestTypeDesc(rs.getString("REQUEST_TYPE_DESC"));
	    		
	    		vo.setCertypeCode(rs.getString("CERTYPE_CODE")); 
	    		vo.setCertypeDesc(rs.getString("CERTYPE_DESC"));  
	    		
	    		vo.setAccountNo(rs.getString("ACCOUNT_NO"));  
	    		
	    		Float totalAmount = 0f;
	    		
	    		vo.setAmountDbd(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_DBD")));  
	    		totalAmount+=convertBigDecimalToLong(vo.getAmountDbd());
	    		
	    		vo.setAmountTmb(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT_TMB")));  
	    		totalAmount+=convertBigDecimalToLong(vo.getAmountTmb());
	    		
	    		vo.setAmount(convertBigDecimalToZero(rs.getBigDecimal("AMOUNT"))); 
	    		totalAmount+=convertBigDecimalToLong(vo.getAmount());
	    		
	    		vo.setTotalAmount(new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN));   
	    		
	    		vo.setMakerById(rs.getString("MAKER_BY_ID"));   
	    		vo.setMakerByName(rs.getString("MAKER_BY_NAME"));   
	    		
	    		vo.setCheckerById(rs.getString("CHECKER_BY_ID"));  
	    		vo.setCheckerByName(rs.getString("CHECKER_BY_NAME")); 
	    		
	    		vo.setStatus(rs.getString("STATUS"));                   
	    		vo.setRemark(rs.getString("REMARK"));                   
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





