package com.tmb.ecert.requestorform.persistence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.utils.BeanUtils;

import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Repository
public class RequestorDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	private final String SQL_ECERT_REQUEST_FORM_INSERT = "INSERT INTO ECERT_REQUEST_FORM";
	private final String SQL_ECERT_REQUEST_FORM_UPDATE = "UPDATE ECERT_REQUEST_FORM SET";
	private final String SQL_ECERT_REQUEST_CERTIFICATE_INSERT = "INSERT INTO ECERT_REQUEST_CERTIFICATE";
	private final String SQL_ECERT_REQUEST_CERTIFICATE_UPDATE = "UPDATE ECERT_REQUEST_CERTIFICATE SET";

	public Long saveCertificates(RequestCertificate vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_CERTIFICATE_INSERT);
		sql.append(
				"(REQFORM_ID,CERTIFICATE_CODE,TOTALNUMBER,CREATED_BY_ID,CREATED_BY_NAME,REGISTERED_DATE,CREATED_DATETIME,STATEMENT_YEAR,ACCEPTED_DATE,OTHER)");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?)");

		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				Date date = new Date(timestamp.getTime());
				ps.setLong(1, vo.getReqFormId());
				ps.setString(2, vo.getCertificateCode());
				ps.setInt(3, vo.getTotalNumber());
				ps.setString(4, vo.getCreatedById());
				ps.setString(5, vo.getCreatedByName());
				if (vo.getRegisteredDate() != null) {
					ps.setDate(6, new Date(vo.getRegisteredDate().getTime()));
				} else {
					ps.setNull(6, Types.DATE);
				}
				ps.setTimestamp(7, timestamp);
				if (vo.getStatementYear() != null) {
					ps.setInt(8, vo.getStatementYear());
				} else {
					ps.setNull(8, Types.SMALLINT);
				}
				if (vo.getAcceptedDate() != null) {
					ps.setDate(9, new Date(vo.getAcceptedDate().getTime()));
				} else {
					ps.setNull(9, Types.DATE);
				}
				if (BeanUtils.isNotEmpty(vo.getOther())) {
					ps.setString(10, vo.getOther());
				} else {
					ps.setString(10, null);
				}
				return ps;
			}
		}, holder);

		Long currentId = holder.getKey().longValue();

		return currentId;
	}

	public void updateCertificates(RequestCertificate vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_CERTIFICATE_UPDATE);
		sql.append(
				" REQFORM_ID=?,CERTIFICATE_CODE=?,TOTALNUMBER=?,REGISTERED_DATE=?,STATEMENT_YEAR=?,ACCEPTED_DATE=?,OTHER=?");
		sql.append(" WHERE REQCERTIFICATE_ID=?");
		int row = jdbcTemplate.update(sql.toString(),
				new Object[] { vo.getReqFormId(), vo.getCertificateCode(), vo.getTotalNumber(), vo.getRegisteredDate(),
						vo.getStatementYear(), vo.getAcceptedDate(), vo.getOther(), vo.getReqCertificateId() });
		logger.info("SQL_ECERT_REQUEST_CERTIFICATE_UPDATE rows updated => {}", row);
	}

	public void deleteCertificates(Long id) {
		String sql = " DELETE FROM ECERT_REQUEST_CERTIFICATE WHERE REQCERTIFICATE_ID = ? ";
		jdbcTemplate.update(sql, new Object[] { id });
	}

	public Long save(RequestForm vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_INSERT);
		sql.append("(CERTYPE_CODE,ORGANIZE_ID,CUSTOMER_NAME,COMPANY_NAME,");
		sql.append("BRANCH,CUSTSEGMENT_CODE,CA_NUMBER,DEPARTMENT,PAIDTYPE_CODE,");
		sql.append("DEBIT_ACCOUNT_TYPE,TRANCODE,GLTYPE,ACCOUNTTYPE,ACCOUNT_NO,");
		sql.append("ACCOUNT_NAME,CUSTOMER_NAMERECEIPT,TELEPHONE,REQUESTFORM_FILE,");
		sql.append("IDCARD_FILE,CHANGENAME_FILE,CERTIFICATE_FILE,ADDRESS,");
		sql.append("REMARK,RECEIPT_NO,STATUS,CREATED_BY_ID,CREATED_BY_NAME,");
		sql.append("CREATED_DATETIME,MAKER_BY_ID,MAKER_BY_NAME,TMB_REQUESTNO, REQUEST_DATE,");
		sql.append("LOCK_FLAG");
		sql.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); // GETDATE()

		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				Date date = new Date(timestamp.getTime());
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
				ps.setTimestamp(28, vo.getCreatedDateTime()); // timestamp
				ps.setString(29, null);
				ps.setString(30, null);
				ps.setString(31, vo.getTmbRequestNo());
				ps.setTimestamp(32, vo.getRequestDate()); // timestamp
				/**
				 * Lock Status 0, locked 1, unlocked
				 */
				ps.setInt(33, 1);
				return ps;
			}
		}, holder);

		Long currentId = holder.getKey().longValue();

		return currentId;
	}

	public void update(RequestForm vo) {

		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		sql.append(" CERTYPE_CODE=?,ORGANIZE_ID=?,CUSTOMER_NAME=?,COMPANY_NAME=?,");
		sql.append("BRANCH=?,CUSTSEGMENT_CODE=?,CA_NUMBER=?,DEPARTMENT=?,PAIDTYPE_CODE=?,");
		sql.append("DEBIT_ACCOUNT_TYPE=?,TRANCODE=?,GLTYPE=?,ACCOUNTTYPE=?,ACCOUNT_NO=?,");
		sql.append("ACCOUNT_NAME=?,CUSTOMER_NAMERECEIPT=?,TELEPHONE=?,REQUESTFORM_FILE=?,");
		sql.append("IDCARD_FILE=?,CHANGENAME_FILE=?,CERTIFICATE_FILE=?,ADDRESS=?,");
		sql.append("REMARK=?,RECEIPT_NO=?,MAKER_BY_ID=?,MAKER_BY_NAME=?,UPDATED_BY_ID=?,");
		sql.append("UPDATED_BY_NAME=?,UPDATED_DATETIME=?,STATUS=?,RECEIPT_DATE=?,");
		sql.append("RECEIPT_FILE=?,ECM_FLAG=?,REF1=?,REF2=?,AMOUNT=?,REJECTREASON_CODE=?,REJECTREASON_OTHER=?,");
		sql.append("AMOUNT_TMB=?,AMOUNT_DBD=?,CHECKER_BY_ID=?,CHECKER_BY_NAME=?,LOCK_FLAG=?");
		sql.append(" WHERE REQFORM_ID = ?");
		int row = jdbcTemplate.update(sql.toString(), new Object[] { vo.getCerTypeCode(), vo.getOrganizeId(),
				vo.getCustomerName(), vo.getCompanyName(), vo.getBranch(), vo.getCustsegmentCode(), vo.getCaNumber(),
				vo.getDepartment(), vo.getPaidTypeCode(), vo.getDebitAccountType(), vo.getTranCode(), vo.getGlType(),
				vo.getAccountType(), vo.getAccountNo(), vo.getAccountName(), vo.getCustomerNameReceipt(),
				vo.getTelephone(), vo.getRequestFormFile(), vo.getIdCardFile(), vo.getChangeNameFile(),
				vo.getCertificateFile(), vo.getAddress(), vo.getRemark(), vo.getReceiptNo(), vo.getMakerById(),
				vo.getMakerByName(), UserLoginUtils.getCurrentUserLogin().getUserId(),
				UserLoginUtils.getCurrentUserLogin().getUsername(), new java.util.Date(), vo.getStatus(),
				vo.getReceiptDate(), vo.getReceiptFile(), vo.getEcmFlag(), vo.getRef1(), vo.getRef2(), vo.getAmount(),
				vo.getRejectReasonCode(), vo.getRejectReasonOther(), vo.getAmountTmb(), vo.getAmountDbd(),
				vo.getCheckerById(), vo.getCheckerByName(), vo.getLockFlag(), vo.getReqFormId() });

		logger.info("SQL_ECERT_REQUEST_FORM_UPDATE rows updated => {}", row);
	}

	public void updateLockStatus(RequestForm vo) { // ON
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sql.append(" LOCK_FLAG=?,UPDATED_BY_ID=?,UPDATED_BY_NAME=?,UPDATED_DATETIME=? WHERE REQFORM_ID=? ");
		int row = jdbcTemplate.update(sql.toString(), new Object[] { vo.getLockFlag(), vo.getUpdatedById(),
				vo.getUpdatedByName(), timestamp, vo.getReqFormId() });
		logger.info("updateLockStatus SQL_ECERT_REQUEST_FORM_UPDATE rows updated => {}", row);
	}

}
