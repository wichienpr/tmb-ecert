package com.tmb.ecert.batchjob.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.EcertJobGLFailed;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.batchjob.domain.EcertRequestForm;

@Repository
public class PaymentGLSummaryBatchDao {

//	private Logger log = LoggerFactory.getLogger(PaymentGLSummaryBatchDao.class);
	
	private final String DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String QUERY_REQ_GL_SUMMARY_PROCESS = " SELECT * FROM ECERT_REQUEST_FORM WHERE STATUS IN ('10009','10010') AND CONVERT(date, REQUEST_DATE) = ? ";
	
	private final String QUERY_INSERT_ECERT_JOB_MONITORING = " INSERT INTO ECERT_JOB_MONITORING ( " +
			" JOBTYPE_CODE,  	 	" + 
			" START_DATE,  	 		" + 
			" STOP_DATE, 	 		" + 
			" ENDOFDATE, 		 	" + 
			" STATUS,  		 		" + 
			" ERROR_DESC,  	 		" + 
			" RERUN_NUMBER, 	 	" + 
			" RERUN_BY_ID, 	 		" + 
			" RERUN_BY_NAME, 	 	" + 
			" RERUN_DATETIME ) 		" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private final String QUERY_UPDATE_ECERT_JOB_MONITORING = " UPDATE ECERT_JOB_MONITORING " + 
			" SET STOP_DATE = ?, STATUS = ? AND ERROR_DESC = ?, RERUN_ID = ?, RERUN_BY_NAME = ?, RERUN_DATETIME = ?" +
			" WHERE JOBMONITORING_ID = ? ";
	
	private final String QUERY_INSERT_ECERT_JOB_GL_FAILED = " INSERT INTO ECERT_JOBGL_FAILED (PAYMENTDATE) VALUES (?) ";
	
	private final String QUERY_OFFICE_CODE = " select OFFICE_CODE2 from ECERT_HR_OFFICECODE where OFFICE_CODE1 = ? ";

	private final String QUERY_CERTIFICATE_REQ = " SELECT c.CERTIFICATE FROM ECERT_REQUEST_CERTIFICATE rc " + 
			"LEFT JOIN ECERT_CERTIFICATE c ON rc.CERTIFICATE_CODE = c.CODE WHERE rc.REQFORM_ID = ? ";
	
	public List<EcertRequestForm> queryReqGlSummaryProcess(Date runDate) {
		String date = DateFormatUtils.format(runDate, this.DATE_FORMAT);
		return jdbcTemplate.query(this.QUERY_REQ_GL_SUMMARY_PROCESS, new Object[] {date}, reqGlSummaryProcessRowMapper);
	}
	
	private RowMapper<EcertRequestForm> reqGlSummaryProcessRowMapper = new RowMapper<EcertRequestForm>() {
		@Override
		public EcertRequestForm mapRow(ResultSet rs, int arg1) throws SQLException {
			EcertRequestForm vo = new EcertRequestForm();
			
			vo.setReqFormId(rs.getLong("REQFORM_ID"));
			vo.setRequestDate(rs.getTimestamp("REQUEST_DATE"));
			vo.setTmbRequestNo(rs.getString("TMB_REQUESTNO"));
			vo.setCerTypeCode(rs.getString("CERTYPE_CODE"));
			vo.setOrganizeId(rs.getString("ORGANIZE_ID"));
			vo.setCustomerName(rs.getString("CUSTOMER_NAME"));
			vo.setCompanyName(rs.getString("COMPANY_NAME"));
			vo.setBranch(rs.getString("BRANCH"));
			vo.setCustSegmentCode(rs.getString("CUSTSEGMENT_CODE"));
			vo.setOfficeCode(rs.getString("OFFICE_CODE"));
			vo.setCaNumber(rs.getString("CA_NUMBER"));
			vo.setDepartment(rs.getString("DEPARTMENT"));
			vo.setPaidTypeCode(rs.getString("PAIDTYPE_CODE"));
			vo.setDebitAccountType(rs.getString("DEBIT_ACCOUNT_TYPE"));
			vo.setTranCode(rs.getString("TRANCODE"));
			vo.setGlType(rs.getString("GLTYPE"));
			vo.setAccountType(rs.getString("ACCOUNTTYPE"));
			vo.setAccountNo(rs.getString("ACCOUNT_NO"));
			vo.setAccountName(rs.getString("ACCOUNT_NAME"));
			vo.setCustomerNameReceipt(rs.getString("CUSTOMER_NAMERECEIPT"));
			vo.setTelephone(rs.getString("TELEPHONE"));
			vo.setRequestFormFile(rs.getString("REQUESTFORM_FILE"));
			vo.setIdCardFile(rs.getString("IDCARD_FILE"));
			vo.setChangeNameFile(rs.getString("CHANGENAME_FILE"));
			vo.setCertificateFile(rs.getString("CERTIFICATE_FILE"));
			vo.setAddress(rs.getString("ADDRESS"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setPaymentDate(rs.getTimestamp("PAYMENT_DATE"));
			vo.setPayloadTs(rs.getTimestamp("PAYLOADTS"));
			vo.setPaymentBranchCode(rs.getString("PAYMENT_BRANCHCODE"));
			vo.setPostDate(rs.getTimestamp("POSTDATE"));
			vo.setRef1(rs.getString("REF1"));
			vo.setRef2(rs.getString("REF2"));
			vo.setAmount(rs.getBigDecimal("AMOUNT"));
			vo.setAmountTmb(rs.getBigDecimal("AMOUNT_TMB"));
			vo.setAmountDbd(rs.getBigDecimal("AMOUNT_DBD"));
			vo.setReceiptNo(rs.getString("RECEIPT_NO"));
			vo.setReceiptDate(rs.getTimestamp("RECEIPT_DATE"));
			vo.setStatus(rs.getString("STATUS"));
			vo.setErrorDescription(rs.getString("ERROR_DESCRIPTION"));
			vo.setPaymentStatus(rs.getString("PAYMENT_STATUS"));
			vo.setCountPayment(rs.getBigDecimal("COUNT_PAYMENT"));
			vo.setRejectReasonCode(rs.getString("REJECTREASON_CODE"));
			vo.setRejectReasonOther(rs.getString("REJECTREASON_OTHER"));
			vo.setCreatedById(rs.getString("CREATED_BY_ID"));
			vo.setCreatedByName(rs.getString("CREATED_BY_NAME"));
			vo.setCreatedDatetime(rs.getTimestamp("CREATED_DATETIME"));
			vo.setMarkerById(rs.getString("MAKER_BY_ID"));
			vo.setMarkerByName(rs.getString("MAKER_BY_NAME"));
			vo.setCheckerById(rs.getString("CHECKER_BY_ID"));
			vo.setCheckerByName(rs.getString("CHECKER_BY_NAME"));
			vo.setUpdatedById(rs.getString("UPDATED_BY_ID"));
			vo.setUpdatedByName(rs.getString("UPDATED_BY_NAME"));
			vo.setUpdatedDatetime(rs.getTimestamp("UPDATED_DATETIME"));
			return vo;
		}
	};
	
	public Long insertEcertJobMonitoring(EcertJobMonitoring ecertJobMonitoring) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(QUERY_INSERT_ECERT_JOB_MONITORING, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, ecertJobMonitoring.getJobTypeCode());
				ps.setTimestamp(2, ecertJobMonitoring.getStartDate() != null ? new Timestamp(ecertJobMonitoring.getStartDate().getTime()) : null);
				ps.setTimestamp(3, ecertJobMonitoring.getStopDate() != null ? new Timestamp(ecertJobMonitoring.getStopDate().getTime()) : null);
				ps.setTimestamp(4, ecertJobMonitoring.getEndOfDate() != null ? new Timestamp(ecertJobMonitoring.getEndOfDate().getTime()) : null);
				ps.setString(5, ecertJobMonitoring.getStatus());
				ps.setString(6, ecertJobMonitoring.getErrorDesc());
				ps.setInt(7, ecertJobMonitoring.getRerunNumber());
				ps.setString(8, ecertJobMonitoring.getRerunById());
				ps.setString(9, ecertJobMonitoring.getRerunByName());
				ps.setTimestamp(10, ecertJobMonitoring.getRerunDatetime() != null ? new Timestamp(ecertJobMonitoring.getRerunDatetime().getTime()) : null);
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		return id;
	}
	
	public Long insertEcertJobGLFailed(EcertJobGLFailed ecertJobGLFailed) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(QUERY_INSERT_ECERT_JOB_GL_FAILED, Statement.RETURN_GENERATED_KEYS);
				ps.setTimestamp(1, ecertJobGLFailed.getPaymentDate() != null ? new Timestamp(ecertJobGLFailed.getPaymentDate().getTime()) : null);
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		return id;
	}
	
	public void updatedStatusEcertJobMonitoring(EcertJobMonitoring ecertJobMonitoring) {
		jdbcTemplate.update(this.QUERY_UPDATE_ECERT_JOB_MONITORING, new Object[] {
				ecertJobMonitoring.getStopDate(),
				ecertJobMonitoring.getStatus(),
				ecertJobMonitoring.getErrorDesc(),
				ecertJobMonitoring.getRerunById(),
				ecertJobMonitoring.getRerunByName(),
				ecertJobMonitoring.getRerunDatetime(),
				ecertJobMonitoring.getJobMonitoringId()
		});
	}
	
	public String queryOfficeCode2(String officeCode1) {
		String officeCode2 = "";
		officeCode2 = jdbcTemplate.queryForObject(this.QUERY_OFFICE_CODE, new Object[] {officeCode1}, String.class);
		return StringUtils.isNotBlank(officeCode2) ? officeCode2 : "";
	}
	
	public List<String> queryCertificateReq(Long reqFormId) {
		List<String> certificateReqs = jdbcTemplate.query(this.QUERY_CERTIFICATE_REQ, new Object[] {reqFormId}, certificateReqRowMapper);
		return certificateReqs;
	}
	
	private RowMapper<String> certificateReqRowMapper = new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int arg1) throws SQLException {
			return rs.getString("CERTIFICATE");
		}
	};
}
