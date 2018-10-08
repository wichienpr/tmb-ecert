package com.tmb.ecert.checkrequeststatus.persistence.dao;

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

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class CheckRequestDetailDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);
	
	private final String SQL_ECERT_REQUEST_FORM = " SELECT * FROM ECERT_REQUEST_FORM WHERE 1=1 ";
	private final String SQL_ECERT_REQUEST_CERTIFICATE = " SELECT * FROM ECERT_REQUEST_CERTIFICATE WHERE 1=1 ";

	public List<RequestForm> findReqFormById(Long reqFormId) {
		logger.info("RequestorDao::findReqFormById => {}", reqFormId);
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		sql.append(" AND REQFORM_ID = ? ");
		params.add(reqFormId);
		return jdbcTemplate.query(sql.toString(), params.toArray(), formMapping);
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
			row.setReqCertificateId(rs.getLong("REQCERTIFICATE_ID"));
			row.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
			row.setCreatedById(rs.getString("CREATED_BY_ID"));
			row.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			row.setCreatedDateTime(rs.getTimestamp("CREATED_DATETIME"));
			row.setReqCertificateId(rs.getLong("REQCERTIFICATE_ID"));
			row.setReqFormId(rs.getLong("REQFORM_ID"));
			row.setTotalNumber(rs.getInt("TOTALNUMBER"));
			row.setUpdateById(rs.getString("UPDATED_BY_ID"));
			row.setUpdateByName(rs.getString("UPDATED_BY_NAME"));
			row.setUpdateDateTime(rs.getTimestamp("UPDATED_DATETIME"));
			return row;
		}
	};

	private RowMapper<RequestForm> formMapping = new RowMapper<RequestForm>() {
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
			row.setCreatedDateTime(rs.getTimestamp("CREATED_DATETIME"));
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
			row.setPayLoadTs(rs.getTimestamp("PAYLOADTS"));
			row.setPaymentBranchCode(rs.getString("PAYMENT_BRANCHCODE"));
			row.setPaymentDate(rs.getTimestamp("PAYMENT_DATE"));
			row.setPaymentStatus(rs.getString("PAYMENT_STATUS"));
			row.setPostDate(rs.getTimestamp("POSTDATE"));
			row.setReceiptNo(rs.getString("RECEIPT_NO"));
			row.setRef1(rs.getString("REF1"));
			row.setRef2(rs.getString("REF2"));
			row.setRejectReasonCode(rs.getString("REJECTREASON_CODE"));
			row.setRejectReasonOther(rs.getString("REJECTREASON_OTHER"));
			row.setRemark(rs.getString("REMARK"));
			row.setReqFormId(rs.getLong("REQFORM_ID"));
			row.setRequestDate(rs.getTimestamp("REQUEST_DATE"));
			row.setRequestFormFile(rs.getString("REQUESTFORM_FILE"));
			row.setStatus(rs.getString("STATUS"));
			row.setTelephone(rs.getString("TELEPHONE"));
			row.setTmbRequestNo(rs.getString("TMB_REQUESTNO"));
			row.setTranCode(rs.getString("TRANCODE"));
			row.setUpdatedById(rs.getString("UPDATED_BY_ID"));
			row.setUpdatedByName(rs.getString("UPDATED_BY_NAME"));
			row.setUpdatedDateTime(rs.getTimestamp("UPDATED_DATETIME"));
			return row;
		}
	};
}
