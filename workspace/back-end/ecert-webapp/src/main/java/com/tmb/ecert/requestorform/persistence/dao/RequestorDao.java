package com.tmb.ecert.requestorform.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
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

	private final String SQL_ECERT_REQUEST_FORM = " SELECT * FROM ECERT_REQUEST_FORM WHERE 1=1 ";
	private final String SQL_ECERT_REQUEST_CERTIFICATE = " SELECT * FROM ECERT_REQUEST_CERTIFICATE WHERE 1=1 ";
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
				ps.setString(25, "10001");
				ps.setString(26, vo.getCreatedById());
				ps.setString(27, vo.getCreatedByName());
				ps.setString(28, vo.getMakerById());
				ps.setString(29, vo.getMakerByName());
				ps.setString(30, vo.getAccountNo());
				return ps;
			}
		}, holder);
		
		Long currentId = holder.getKey().longValue();
		
		return currentId;
	}

	public List<RequestForm> findAll() {
		logger.info("RequestorDao::findAll");
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		return jdbcTemplate.query(sql.toString(), mapping);
	}
	
	public List<RequestForm> findById(Long reqFormId) {
		logger.info("RequestorDao::findById => {}", reqFormId);
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		sql.append(" AND REQFORM_ID = ? ");
		params.add(reqFormId);
		return jdbcTemplate.query(sql.toString(), params.toArray(), mapping);
	}
	
	public List<RequestCertificate> findCertByReqFormId(Long reqFormId) {
		logger.info("RequestorDao::findCertByReqFormId => {}", reqFormId);
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_CERTIFICATE);
		sql.append(" AND REQFORM_ID = ? ");
		params.add(reqFormId);
		return jdbcTemplate.query(sql.toString(), params.toArray(), certMapping);
	}
	
	private RowMapper<RequestCertificate> certMapping = new RowMapper<RequestCertificate>() {
		@Override
		public RequestCertificate mapRow(ResultSet rs, int args1) throws SQLException {
			RequestCertificate row = new RequestCertificate();
			row.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
			row.setCreatedById(rs.getString("CREATED_BY_ID"));
			row.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			row.setCreatedDateTime(rs.getDate("CREATED_DATETIME"));
			row.setReqCertificateId(rs.getLong("REQCERTIFICATE_ID"));
			row.setReqFormId(rs.getLong("REQFORM_ID"));
			row.setTotalNumber(rs.getInt("TOTALNUMBER"));
			row.setUpdateById(rs.getString("UPDATED_BY_ID"));
			row.setUpdateByName(rs.getString("UPDATED_BY_NAME"));
			row.setUpdateDateTime(rs.getDate("UPDATED_DATETIME"));
			return row;
		}
	};

	private RowMapper<RequestForm> mapping = new RowMapper<RequestForm>() {
		@Override
		public RequestForm mapRow(ResultSet rs, int args1) throws SQLException {
			RequestForm row = new RequestForm();
			row.setAccountName(rs.getString("ACCOUNT_NAME"));
			row.setAccountNo(rs.getString("ACCOUNT_NO"));
			row.setAccountType(rs.getString("ACCOUNTTYPE"));
			row.setAddress(rs.getString("ADDRESS"));
			row.setAmount(rs.getBigDecimal("AMOUNT"));
			row.setAmountDbd(rs.getBigDecimal("AMOUNT_DBD"));
			row.setAmountTmb(rs.getBigDecimal("AMOUNT_TMB"));
			row.setBranch(rs.getString("BRANCH"));
			row.setCaNumber(rs.getString("CA_NUMBER"));
			row.setCertificateFile(rs.getString("CERTIFICATE_FILE"));
			row.setCerTypeCode(rs.getString("CERTYPE_CODE"));
			row.setChangeNameFile(rs.getString("CHANGENAME_FILE"));
			row.setCheckerById(rs.getString("CHECKER_BY_ID"));
			row.setCheckerByName(rs.getString("CHECKER_BY_NAME"));
			row.setCompanyName(rs.getString("COMPANY_NAME"));
			row.setCountPayment(rs.getInt("COUNT_PAYMENT"));
			row.setCreatedById(rs.getString("CREATED_BY_ID"));
			row.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			row.setCreatedDateTime(rs.getDate("CREATED_DATETIME"));
			row.setCustomerName(rs.getString("CUSTOMER_NAME"));
			row.setCustomerNameReceipt(rs.getString("CUSTOMER_NAMERECEIPT"));
			row.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE"));
			row.setDebitAccountType(rs.getString("DEBIT_ACCOUNT_TYPE"));
			row.setDepartment(rs.getString("DEPARTMENT"));
			row.setErrorDescription(rs.getString("ERROR_DESCRIPTION"));
			row.setGlType(rs.getString("GLTYPE"));
			row.setIdCardFile(rs.getString("IDCARD_FILE"));
			row.setMakerById(rs.getString("MAKER_BY_ID"));
			row.setMakerByName(rs.getString("MAKER_BY_NAME"));
			row.setOrganizeId(rs.getString("ORGANIZE_ID"));
			row.setPaidTypeCode(rs.getString("PAIDTYPE_CODE"));
			row.setPayLoadTs(rs.getDate("PAYLOADTS"));
			row.setPaymentBranchCode(rs.getString("PAYMENT_BRANCHCODE"));
			row.setPaymentDate(rs.getDate("PAYMENT_DATE"));
			row.setPaymentStatus(rs.getString("PAYMENT_STATUS"));
			row.setPostDate(rs.getDate("POSTDATE"));
			row.setReceiptNo(rs.getString("RECEIPT_NO"));
			row.setRef1(rs.getString("REF1"));
			row.setRef2(rs.getString("REF2"));
			row.setRejectReasonCode(rs.getString("REJECTREASON_CODE"));
			row.setRejectReasonOther(rs.getString("REJECTREASON_OTHER"));
			row.setRemark(rs.getString("REMARK"));
			row.setReqFormId(rs.getLong("REQFORM_ID"));
			row.setRequestDate(rs.getDate("REQUEST_DATE"));
			row.setRequestFormFile(rs.getString("REQUESTFORM_FILE"));
			row.setStatus(rs.getString("STATUS"));
			row.setTelephone(rs.getString("TELEPHONE"));
			row.setTmbRequestNo(rs.getString("TMB_REQUESTNO"));
			row.setTranCode(rs.getString("TRANCODE"));
			row.setUpdatedById(rs.getString("UPDATED_BY_ID"));
			row.setUpdatedByName(rs.getString("UPDATED_BY_NAME"));
			row.setUpdatedDateTime(rs.getDate("UPDATED_DATETIME"));
			return row;
		}
	};
}
