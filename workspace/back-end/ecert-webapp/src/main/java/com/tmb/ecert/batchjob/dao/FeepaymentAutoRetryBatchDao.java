package com.tmb.ecert.batchjob.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class FeepaymentAutoRetryBatchDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String SQL_ECERT_REQUEST_FORM = " SELECT F.* FROM ECERT_REQUEST_FORM F WHERE 1=1 AND F.DELETE_FLAG = 0 ";
	private final String SQL_ECERT_REQUEST_FORM_UPDATE = " UPDATE ECERT_REQUEST_FORM SET ";
	
	public List<RequestForm> findByStatus(String status) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		sql.append(" AND F.STATUS = ? ");
		
		return jdbcTemplate.query(sql.toString(), new Object[]{ status }, formMapping);
	}
	
	public void updateRequesstForm(RequestForm vo) {
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM_UPDATE);
		sql.append(" CERTYPE_CODE=?,ORGANIZE_ID=?,CUSTOMER_NAME=?,COMPANY_NAME=?,");
		sql.append("BRANCH=?,CUSTSEGMENT_CODE=?,CA_NUMBER=?,DEPARTMENT=?,PAIDTYPE_CODE=?,");
		sql.append("DEBIT_ACCOUNT_TYPE=?,TRANCODE=?,GLTYPE=?,ACCOUNTTYPE=?,ACCOUNT_NO=?,");
		sql.append("ACCOUNT_NAME=?,CUSTOMER_NAMERECEIPT=?,TELEPHONE=?,REQUESTFORM_FILE=?,");
		sql.append("IDCARD_FILE=?,CHANGENAME_FILE=?,CERTIFICATE_FILE=?,ADDRESS=?,");
		sql.append("REMARK=?,RECEIPT_NO=?,MAKER_BY_ID=?,MAKER_BY_NAME=?,UPDATED_BY_ID=?,");
		sql.append("UPDATED_BY_NAME=?,UPDATED_DATETIME=?,STATUS=?,RECEIPT_DATE=?,");
		sql.append("RECEIPT_FILE=?,ECM_FLAG=?,REF1=?,REF2=?,AMOUNT=?,REJECTREASON_CODE=?,REJECTREASON_OTHER=?,");
		sql.append("AMOUNT_TMB=?,AMOUNT_DBD=?,CHECKER_BY_ID=?,CHECKER_BY_NAME=?,LOCK_FLAG=?,PAYMENT_STATUS=?,COUNT_PAYMENT=?");
		sql.append(" WHERE REQFORM_ID = ?");
		jdbcTemplate.update(sql.toString(), new Object[] { vo.getCerTypeCode(), vo.getOrganizeId(),
				vo.getCustomerName(), vo.getCompanyName(), vo.getBranch(), vo.getCustsegmentCode(), vo.getCaNumber(),
				vo.getDepartment(), vo.getPaidTypeCode(), vo.getDebitAccountType(), vo.getTranCode(), vo.getGlType(),
				vo.getAccountType(), vo.getAccountNo(), vo.getAccountName(), vo.getCustomerNameReceipt(),
				vo.getTelephone(), vo.getRequestFormFile(), vo.getIdCardFile(), vo.getChangeNameFile(),
				vo.getCertificateFile(), vo.getAddress(), vo.getRemark(), vo.getReceiptNo(), vo.getMakerById(),
				vo.getMakerByName(), vo.getUpdatedById(), vo.getUpdatedByName(),
				new java.util.Date(), vo.getStatus(), vo.getReceiptDate(), vo.getReceiptFile(), vo.getEcmFlag(),
				vo.getRef1(), vo.getRef2(), vo.getAmount(), vo.getRejectReasonCode(), vo.getRejectReasonOther(),
				vo.getAmountTmb(), vo.getAmountDbd(), vo.getCheckerById(), vo.getCheckerByName(), vo.getLockFlag(),
				vo.getPaymentStatus(), vo.getCountPayment(),
				vo.getReqFormId() });
	}
	
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
			row.setCountPayment(rs.getInt("COUNT_PAYMENT"));
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
			row.setLockFlag(rs.getInt("LOCK_FLAG"));
			return row;
		}
	};
	
}
