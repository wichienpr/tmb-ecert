package com.tmb.ecert.requestorform.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class RequestorDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(RequestorDao.class);

	private final String SQL_ECERT_REQUEST_FORM_INSERT = "INSERT INTO ECERT_REQUEST_FORM";
	private final String SQL_ECERT_REQUEST_CERTIFICATE_INSERT = "INSERT INTO ECERT_REQUEST_CERTIFICATE";
	
	public Long saveCertificates(RequestCertificate vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_CERTIFICATE_INSERT);
		sql.append("(REQFORM_ID,CERTIFICATE_CODE,TOTALNUMBER,CREATED_BY_ID,CREATED_BY_NAME,CREATED_DATETIME)");
		sql.append("VALUES(?,?,?,?,?,GETDATE())");

		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, vo.getReqFormId());
				ps.setString(2, vo.getCertificateCode());
				ps.setInt(3, vo.getTotalNumber());
				ps.setString(4, vo.getCreatedById());
				ps.setString(5, vo.getCreatedByName());
				return ps;
			}
		}, holder);
		
		Long currentId = holder.getKey().longValue();
		
		return currentId;
	}

	public Long save(RequestForm vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_INSERT);
		sql.append("(REQUEST_DATE,CERTYPE_CODE,ORGANIZE_ID,CUSTOMER_NAME,COMPANY_NAME,");
		sql.append("BRANCH,CUSTSEGMENT_CODE,CA_NUMBER,DEPARTMENT,PAIDTYPE_CODE,");
		sql.append("DEBIT_ACCOUNT_TYPE,TRANCODE,GLTYPE,ACCOUNTTYPE,ACCOUNT_NO,");
		sql.append("ACCOUNT_NAME,CUSTOMER_NAMERECEIPT,TELEPHONE,REQUESTFORM_FILE,");
		sql.append("IDCARD_FILE,CHANGENAME_FILE,CERTIFICATE_FILE,ADDRESS,");
		sql.append("REMARK,RECEIPT_NO,STATUS,CREATED_BY_ID,CREATED_BY_NAME,");
		sql.append("CREATED_DATETIME,MAKER_BY_ID,MAKER_BY_NAME,TMB_REQUESTNO");
		sql.append(") VALUES (GETDATE(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),?,?,?)"); // GETDATE()

		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, vo.getCerTypeCode());
				ps.setString(2, vo.getOrganizeId());
				ps.setString(3, vo.getCustomerName());
				ps.setString(4, vo.getCompanyName());
				ps.setString(5, vo.getBranch());
				ps.setString(6, vo.getCustsegmentCode());
				ps.setString(7, vo.getCaNumber());
				ps.setString(8, vo.getDepartment());
				ps.setString(9, vo.getPaidTypeCode());
				ps.setString(10, vo.getDebitAccountType());
				ps.setString(11, vo.getTranCode());
				ps.setString(12, vo.getGlType());
				ps.setString(13, vo.getAccountType());
				ps.setString(14, vo.getAccountNo());
				ps.setString(15, vo.getAccountName());
				ps.setString(16, vo.getCustomerNameReceipt());
				ps.setString(17, vo.getTelephone());
				ps.setString(18, vo.getRequestFormFile());
				ps.setString(19, vo.getIdCardFile());
				ps.setString(20, vo.getChangeNameFile());
				ps.setString(21, vo.getCertificateFile());
				ps.setString(22, vo.getAddress());
				ps.setString(23, vo.getRemark());
				ps.setString(24, vo.getReceiptNo());
				ps.setString(25, vo.getStatus());
				ps.setString(26, vo.getCreatedById());
				ps.setString(27, vo.getCreatedByName());
				ps.setString(28, vo.getMakerById());
				ps.setString(29, vo.getMakerByName());
				ps.setString(30, vo.getTmbRequestNo());
				return ps;
			}
		}, holder);
		
		Long currentId = holder.getKey().longValue();
		
		return currentId;
	}

}
