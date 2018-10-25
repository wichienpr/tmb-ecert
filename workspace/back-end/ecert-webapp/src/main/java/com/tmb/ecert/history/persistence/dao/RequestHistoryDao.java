package com.tmb.ecert.history.persistence.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.history.persistence.vo.RequestHistoryVo;

import th.co.baiwa.buckwaframework.common.util.DatatableUtils;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Repository
public class RequestHistoryDao {

	@Autowired
	JdbcTemplate jdbc;

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	private static String SQL_SELECT = " SELECT RH.* FROM ECERT_REQUEST_HISTORY RH WHERE 1=1 ";

	private static String CREATE_HISTORY = " INSERT INTO ECERT_REQUEST_HISTORY";

	public void save(RequestForm form) {
		logger.info("RequestHistoryDao::save");
		StringBuilder sql = new StringBuilder(CREATE_HISTORY);
		sql.append("(");
		sql.append("REQFORM_ID, REQUEST_DATE, TMB_REQUESTNO, CERTYPE_CODE, ORGANIZE_ID, CUSTOMER_NAME, COMPANY_NAME,");
		sql.append("BRANCH, CUSTSEGMENT_CODE, OFFICE_CODE, CA_NUMBER, DEPARTMENT, PAIDTYPE_CODE, DEBIT_ACCOUNT_TYPE,");
		sql.append("TRANCODE, GLTYPE, ACCOUNTTYPE, ACCOUNT_NO, ACCOUNT_NAME, CUSTOMER_NAMERECEIPT, TELEPHONE,");
		sql.append("REQUESTFORM_FILE, IDCARD_FILE, CHANGENAME_FILE, CERTIFICATE_FILE, ADDRESS, REMARK, PAYMENT_DATE,");
		sql.append("PAYLOADTS, PAYMENT_BRANCHCODE, POSTDATE, REF1, REF2, AMOUNT, AMOUNT_TMB, AMOUNT_DBD, RECEIPT_NO,");
		sql.append("RECEIPT_DATE, STATUS, ERROR_DESCRIPTION, PAYMENT_STATUS, COUNT_PAYMENT, REJECTREASON_CODE,");
		sql.append("REJECTREASON_OTHER, CREATED_BY_ID, CREATED_BY_NAME, CREATED_DATETIME, UPDATED_BY_ID,");
		sql.append("UPDATED_BY_NAME, UPDATED_DATETIME, MAKER_BY_ID, MAKER_BY_NAME, CHECKER_BY_ID, CHECKER_BY_NAME");
		sql.append(")");
		sql.append(" VALUES (");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?");
		sql.append(")");
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		List<Object> params = new ArrayList<>();
		params.add(form.getReqFormId());
		params.add(form.getRequestDate());
		params.add(form.getTmbRequestNo());
		params.add(form.getCerTypeCode());
		params.add(form.getOrganizeId());
		params.add(form.getCustomerName());
		params.add(form.getCompanyName());

		params.add(form.getBranch());
		params.add(form.getCustsegmentCode());
		params.add(form.getOfficeCode());
		params.add(form.getCaNumber());
		params.add(form.getDepartment());
		params.add(form.getPaidTypeCode());
		params.add(form.getDebitAccountType());

		params.add(form.getTranCode());
		params.add(form.getGlType());
		params.add(form.getAccountType());
		params.add(form.getAccountNo());
		params.add(form.getAccountName());
		params.add(form.getCustomerNameReceipt());
		params.add(form.getTelephone());

		params.add(form.getRequestFormFile());
		params.add(form.getIdCardFile());
		params.add(form.getChangeNameFile());
		params.add(form.getCertificateFile());
		params.add(form.getAddress());
		params.add(form.getRemark());
		params.add(form.getPaymentDate());

		params.add(form.getPayLoadTs());
		params.add(form.getPaymentBranchCode());
		params.add(form.getPostDate());
		params.add(form.getRef1());
		params.add(form.getRef2());
		params.add(form.getAmount());
		params.add(form.getAmountTmb());
		params.add(form.getAmountDbd());
		params.add(form.getReceiptNo());

		params.add(form.getReceiptDate());
		params.add(form.getStatus());
		params.add(form.getErrorDescription());
		params.add(form.getPaymentStatus());
		params.add(form.getCountPayment());
		params.add(form.getRejectReasonCode());

		params.add(form.getRejectReasonOther());
		params.add(form.getCreatedById());
		params.add(form.getCreatedByName());
		params.add(timestamp); // params.add(form.getCreatedDateTime());
		params.add(UserLoginUtils.getCurrentUserLogin().getUserId()); // params.add(form.getUpdatedById());

		params.add(UserLoginUtils.getCurrentUserLogin().getUsername()); // params.add(form.getUpdatedByName());
		params.add(timestamp); // params.add(form.getUpdatedDateTime());
		params.add(form.getMakerById());
		params.add(form.getMakerByName());
		params.add(form.getCheckerById());
		params.add(form.getCheckerByName());

		int rows = jdbc.update(sql.toString(), params.toArray());

		logger.info("ECERT_REQUEST_HISTORY Inserted : {}", rows);
	}

	public List<RequestHistoryVo> findAll() {
		StringBuilder sql = new StringBuilder(SQL_SELECT);
		return jdbc.query(sql.toString(), mapping);
	}

	public List<RequestHistoryVo> findByReqFormId(Long reqFormId) {
		StringBuilder sql = new StringBuilder("SELECT");
		sql.append(
				" F.REQHISTORY_ID,F.PAIDTYPE_CODE,F.DEBIT_ACCOUNT_TYPE,F.CUSTSEGMENT_CODE,F.REQFORM_ID,F.REQUEST_DATE,F.TMB_REQUESTNO,F.ORGANIZE_ID,F.CUSTOMER_NAME,F.COMPANY_NAME,F.BRANCH,F.CA_NUMBER,F.DEPARTMENT,F.TRANCODE,F.GLTYPE,F.ACCOUNTTYPE,F.ACCOUNT_NO,F.ACCOUNT_NAME,F.CUSTOMER_NAMERECEIPT,F.TELEPHONE,F.REQUESTFORM_FILE,F.IDCARD_FILE,F.CHANGENAME_FILE,F.CERTIFICATE_FILE,F.ADDRESS,F.REMARK,F.PAYMENT_DATE,F.PAYLOADTS,F.PAYMENT_BRANCHCODE,F.POSTDATE,F.REF1,F.REF2,F.AMOUNT,F.AMOUNT_TMB,F.AMOUNT_DBD,F.RECEIPT_NO,F.ERROR_DESCRIPTION,F.PAYMENT_STATUS,F.COUNT_PAYMENT,F.REJECTREASON_CODE,F.REJECTREASON_OTHER,F.CREATED_BY_ID,F.CREATED_BY_NAME,F.CREATED_DATETIME,F.UPDATED_BY_ID,F.UPDATED_BY_NAME,F.UPDATED_DATETIME,F.MAKER_BY_ID,F.MAKER_BY_NAME,F.CHECKER_BY_ID,F.CHECKER_BY_NAME,F.OFFICE_CODE,F.RECEIPT_DATE");
		sql.append(" ,L.NAME AS STATUS");
		sql.append(" ,C.NAME AS CERTYPE_CODE");
		sql.append(" FROM ECERT_REQUEST_HISTORY F");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ON F.CERTYPE_CODE = C.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE L ON F.STATUS = L.CODE");
		List<Object> params = new ArrayList<Object>();

		sql.append(" WHERE F.REQFORM_ID = ? ");
		params.add(reqFormId);
		logger.info(sql.toString());
		logger.info("ECERT_REQUEST_HISTORY SELECTED BY REQFORM_ID = {}", reqFormId);
		return jdbc.query(sql.toString(), params.toArray(), mapping);
	}

	public List<RequestHistoryVo> findByReqFormId(RequestHistoryVo formVo) {
		StringBuilder sql = new StringBuilder("SELECT");
		sql.append(
				" F.REQHISTORY_ID,F.PAIDTYPE_CODE,F.DEBIT_ACCOUNT_TYPE,F.CUSTSEGMENT_CODE,F.REQFORM_ID,F.REQUEST_DATE,F.TMB_REQUESTNO,F.ORGANIZE_ID,F.CUSTOMER_NAME,F.COMPANY_NAME,F.BRANCH,F.CA_NUMBER,F.DEPARTMENT,F.TRANCODE,F.GLTYPE,F.ACCOUNTTYPE,F.ACCOUNT_NO,F.ACCOUNT_NAME,F.CUSTOMER_NAMERECEIPT,F.TELEPHONE,F.REQUESTFORM_FILE,F.IDCARD_FILE,F.CHANGENAME_FILE,F.CERTIFICATE_FILE,F.ADDRESS,F.REMARK,F.PAYMENT_DATE,F.PAYLOADTS,F.PAYMENT_BRANCHCODE,F.POSTDATE,F.REF1,F.REF2,F.AMOUNT,F.AMOUNT_TMB,F.AMOUNT_DBD,F.RECEIPT_NO,F.ERROR_DESCRIPTION,F.PAYMENT_STATUS,F.COUNT_PAYMENT,F.REJECTREASON_OTHER,F.CREATED_BY_ID,F.CREATED_BY_NAME,F.CREATED_DATETIME,F.UPDATED_BY_ID,F.UPDATED_BY_NAME,F.UPDATED_DATETIME,F.MAKER_BY_ID,F.MAKER_BY_NAME,F.CHECKER_BY_ID,F.CHECKER_BY_NAME,F.OFFICE_CODE,F.RECEIPT_DATE");
		sql.append(" ,L.NAME AS STATUS");
		sql.append(" ,C.NAME AS CERTYPE_CODE");
		sql.append(" ,R.NAME AS REJECTREASON_CODE");
		sql.append(" FROM ECERT_REQUEST_HISTORY F");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ON F.CERTYPE_CODE = C.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE L ON F.STATUS = L.CODE");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE R ON F.REJECTREASON_CODE = R.CODE ");
		List<Object> params = new ArrayList<Object>();

		sql.append(" WHERE F.REQFORM_ID = ? ");
		sql.append(" ORDER BY F.REQHISTORY_ID DESC");
		params.add(formVo.getReqFormId());
		logger.info("ECERT_REQUEST_HISTORY SELECTED BY REQFORM_ID = {}", formVo.getReqFormId());
		return jdbc.query(DatatableUtils.limitForDataTable(sql.toString(), formVo.getStart(), formVo.getLength()),
				params.toArray(), mapping);
	}

	private RowMapper<RequestHistoryVo> mapping = new RowMapper<RequestHistoryVo>() {
		@Override
		public RequestHistoryVo mapRow(ResultSet rs, int args1) throws SQLException {
			RequestHistoryVo row = new RequestHistoryVo();
			row.setReqhistoryId(rs.getLong("REQHISTORY_ID"));
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

	public int count(RequestHistoryVo vo) {
		StringBuilder sql = new StringBuilder("SELECT");
		sql.append(
				" F.REQHISTORY_ID,F.PAIDTYPE_CODE,F.DEBIT_ACCOUNT_TYPE,F.CUSTSEGMENT_CODE,F.REQFORM_ID,F.REQUEST_DATE,F.TMB_REQUESTNO,F.ORGANIZE_ID,F.CUSTOMER_NAME,F.COMPANY_NAME,F.BRANCH,F.CA_NUMBER,F.DEPARTMENT,F.TRANCODE,F.GLTYPE,F.ACCOUNTTYPE,F.ACCOUNT_NO,F.ACCOUNT_NAME,F.CUSTOMER_NAMERECEIPT,F.TELEPHONE,F.REQUESTFORM_FILE,F.IDCARD_FILE,F.CHANGENAME_FILE,F.CERTIFICATE_FILE,F.ADDRESS,F.REMARK,F.PAYMENT_DATE,F.PAYLOADTS,F.PAYMENT_BRANCHCODE,F.POSTDATE,F.REF1,F.REF2,F.AMOUNT,F.AMOUNT_TMB,F.AMOUNT_DBD,F.RECEIPT_NO,F.ERROR_DESCRIPTION,F.PAYMENT_STATUS,F.COUNT_PAYMENT,F.REJECTREASON_CODE,F.REJECTREASON_OTHER,F.CREATED_BY_ID,F.CREATED_BY_NAME,F.CREATED_DATETIME,F.UPDATED_BY_ID,F.UPDATED_BY_NAME,F.UPDATED_DATETIME,F.MAKER_BY_ID,F.MAKER_BY_NAME,F.CHECKER_BY_ID,F.CHECKER_BY_NAME,F.OFFICE_CODE,F.RECEIPT_DATE");
		sql.append(" ,L.NAME AS STATUS");
		sql.append(" ,C.NAME AS CERTYPE_CODE");
		sql.append(" FROM ECERT_REQUEST_HISTORY F");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE C ON F.CERTYPE_CODE = C.CODE ");
		sql.append(" LEFT JOIN ECERT_LISTOFVALUE L ON F.STATUS = L.CODE");
		List<Object> params = new ArrayList<Object>();

		sql.append(" WHERE F.REQFORM_ID = ? ");
		params.add(vo.getReqFormId());
		BigDecimal rs = jdbc.queryForObject(DatatableUtils.countForDatatable(sql.toString()), BigDecimal.class, params.toArray());
		return rs.intValue();
	}
	
}
