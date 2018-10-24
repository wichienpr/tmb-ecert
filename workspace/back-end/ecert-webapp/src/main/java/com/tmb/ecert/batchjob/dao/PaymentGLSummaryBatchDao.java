package com.tmb.ecert.batchjob.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.tmb.ecert.batchjob.domain.EcertJobGLFailed;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;

@Repository
public class PaymentGLSummaryBatchDao {

//	private Logger log = LoggerFactory.getLogger(PaymentGLSummaryBatchDao.class);
	
	private final String DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String QUERY_REQ_GL_SUMMARY_PROCESS = " SELECT A.*,B.REQFORM_ID AS REQFORM_ID_1,B.CERTIFICATE_CODE,"
			+ " C.CERTIFICATE_ID,C.CODE AS CERT_CODE,C.CERTIFICATE FROM ECERT_REQUEST_FORM A "
			+ " INNER JOIN ECERT_REQUEST_CERTIFICATE B ON B.REQFORM_ID = A.REQFORM_ID "
			+ " INNER JOIN ECERT_CERTIFICATE C ON C.CODE = B.CERTIFICATE_CODE "
	        + " WHERE A.STATUS IN ('10009','10010') AND CONVERT(date, A.REQUEST_DATE) = ? ";
	
	private final String QUERY_REQ_GL_SUMMARY_PROCESS_FROMTODATE = " SELECT A.*,B.REQFORM_ID AS REQFORM_ID_1,B.CERTIFICATE_CODE,"
			+ " C.CERTIFICATE_ID,C.CODE AS CERT_CODE,C.CERTIFICATE FROM ECERT_REQUEST_FORM A "
			+ " INNER JOIN ECERT_REQUEST_CERTIFICATE B ON B.REQFORM_ID = A.REQFORM_ID "
			+ " INNER JOIN ECERT_CERTIFICATE C ON C.CODE = B.CERTIFICATE_CODE "
	        + " WHERE A.STATUS IN ('10009','10010') AND CAST(A.REQUEST_DATE as DATE) >= ? AND CAST(A.REQUEST_DATE as DATE) <= ? ";
	
	
	private final String QUERY_UPDATE_ECERT_JOB_MONITORING = " UPDATE ECERT_JOB_MONITORING " + 
			" SET STOP_DATE = ?, STATUS = ? AND ERROR_DESC = ?, RERUN_ID = ?, RERUN_BY_NAME = ?, RERUN_DATETIME = ?" +
			" WHERE JOBMONITORING_ID = ? ";
	
	private final String QUERY_INSERT_ECERT_JOB_GL_FAILED = " INSERT INTO ECERT_JOBGL_FAILED (PAYMENTDATE) VALUES (?) ";
	
	private final String QUERY_OFFICE_CODE = " select OFFICE_CODE2 from ECERT_HR_OFFICECODE where OFFICE_CODE1 = ? ";
	
	public List<RequestForm> queryReqGlSummaryProcess(Date runDate) {
		String date = DateFormatUtils.format(runDate, this.DATE_FORMAT);
		return jdbcTemplate.query(this.QUERY_REQ_GL_SUMMARY_PROCESS, new Object[] {date},reqFormListMapping);
	}
	
	public List<RequestForm> queryReqGlSummaryProcessByFromToDate(Date fromDate,Date toDate) {
		String fDate = DateFormatUtils.format(fromDate, this.DATE_FORMAT);
		String tDate = DateFormatUtils.format(toDate, this.DATE_FORMAT);
		return jdbcTemplate.query(this.QUERY_REQ_GL_SUMMARY_PROCESS_FROMTODATE, new Object[] {fDate,tDate},reqFormListMapping);
	}
	
	private ResultSetExtractor<List<RequestForm>> reqFormListMapping = new ResultSetExtractor<List<RequestForm>>() {

		@Override
		public List<RequestForm> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, RequestForm> map = new HashMap<Long, RequestForm>();
			while (rs.next()) {
				long reqFormId = rs.getLong("REQFORM_ID");
				RequestForm reqForm = map.get(reqFormId);
				if (reqForm == null) {
					reqForm = new RequestForm();
					reqForm.setReqFormId(rs.getLong("REQFORM_ID"));
					reqForm.setRequestDate(rs.getTimestamp("REQUEST_DATE"));
					reqForm.setTmbRequestNo(rs.getString("TMB_REQUESTNO"));
					reqForm.setCerTypeCode(rs.getString("CERTYPE_CODE"));
					reqForm.setOrganizeId(rs.getString("ORGANIZE_ID"));
					reqForm.setCustomerName(rs.getString("CUSTOMER_NAME"));
					reqForm.setCompanyName(rs.getString("COMPANY_NAME"));
					reqForm.setBranch(rs.getString("BRANCH"));
					reqForm.setCustsegmentCode(rs.getString("CUSTSEGMENT_CODE"));
					reqForm.setOfficeCode(rs.getString("OFFICE_CODE"));
					reqForm.setCaNumber(rs.getString("CA_NUMBER"));
					reqForm.setDepartment(rs.getString("DEPARTMENT"));
					reqForm.setPaidTypeCode(rs.getString("PAIDTYPE_CODE"));
					reqForm.setDebitAccountType(rs.getString("DEBIT_ACCOUNT_TYPE"));
					reqForm.setTranCode(rs.getString("TRANCODE"));
					reqForm.setGlType(rs.getString("GLTYPE"));
					reqForm.setAccountType(rs.getString("ACCOUNTTYPE"));
					reqForm.setAccountNo(rs.getString("ACCOUNT_NO"));
					reqForm.setAccountName(rs.getString("ACCOUNT_NAME"));
					reqForm.setCustomerNameReceipt(rs.getString("CUSTOMER_NAMERECEIPT"));
					reqForm.setTelephone(rs.getString("TELEPHONE"));
					reqForm.setRequestFormFile(rs.getString("REQUESTFORM_FILE"));
					reqForm.setIdCardFile(rs.getString("IDCARD_FILE"));
					reqForm.setChangeNameFile(rs.getString("CHANGENAME_FILE"));
					reqForm.setCertificateFile(rs.getString("CERTIFICATE_FILE"));
					reqForm.setAddress(rs.getString("ADDRESS"));
					reqForm.setRemark(rs.getString("REMARK"));
					reqForm.setPaymentDate(rs.getTimestamp("PAYMENT_DATE"));
					reqForm.setPayLoadTs(rs.getTimestamp("PAYLOADTS"));
					reqForm.setPaymentBranchCode(rs.getString("PAYMENT_BRANCHCODE"));
					reqForm.setPostDate(rs.getTimestamp("POSTDATE"));
					reqForm.setRef1(rs.getString("REF1"));
					reqForm.setRef2(rs.getString("REF2"));
					reqForm.setAmount(rs.getBigDecimal("AMOUNT"));
					reqForm.setAmountTmb(rs.getBigDecimal("AMOUNT_TMB"));
					reqForm.setAmountDbd(rs.getBigDecimal("AMOUNT_DBD"));
					reqForm.setReceiptNo(rs.getString("RECEIPT_NO"));
					reqForm.setReceiptDate(rs.getTimestamp("RECEIPT_DATE"));
					reqForm.setStatus(rs.getString("STATUS"));
					reqForm.setErrorDescription(rs.getString("ERROR_DESCRIPTION"));
					reqForm.setPaymentStatus(rs.getString("PAYMENT_STATUS"));
					reqForm.setCountPayment(rs.getInt("COUNT_PAYMENT"));
					reqForm.setRejectReasonCode(rs.getString("REJECTREASON_CODE"));
					reqForm.setRejectReasonOther(rs.getString("REJECTREASON_OTHER"));
					reqForm.setCreatedById(rs.getString("CREATED_BY_ID"));
					reqForm.setCreatedByName(rs.getString("CREATED_BY_NAME"));
					reqForm.setCreatedDateTime(rs.getTimestamp("CREATED_DATETIME"));
					reqForm.setMakerById(rs.getString("MAKER_BY_ID"));
					reqForm.setMakerByName(rs.getString("MAKER_BY_NAME"));
					reqForm.setCheckerById(rs.getString("CHECKER_BY_ID"));
					reqForm.setCheckerByName(rs.getString("CHECKER_BY_NAME"));
					reqForm.setUpdatedById(rs.getString("UPDATED_BY_ID"));
					reqForm.setUpdatedByName(rs.getString("UPDATED_BY_NAME"));
					reqForm.setUpdatedDateTime(rs.getTimestamp("UPDATED_DATETIME"));
					reqForm.setOfficeCode(rs.getString("OFFICE_CODE"));
					reqForm.setReceiptFile(rs.getString("RECEIPT_FILE"));

					long reqFormCertId = rs.getLong("REQFORM_ID_1");
					if (reqFormCertId > 0) {
						RequestCertificate requestCertificate = new RequestCertificate();
						requestCertificate.setReqFormId(rs.getLong("REQFORM_ID_1"));
						requestCertificate.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
						
						Certificate certificate = new Certificate();
						certificate.setCertificateId(rs.getLong("CERTIFICATE_ID"));
						certificate.setCode(rs.getString("CERT_CODE"));
						certificate.setCertificate(rs.getString("CERTIFICATE"));
						
						requestCertificate.setCertificate(certificate);
						
						reqForm.getCertificateList().add(requestCertificate);
					}

					map.put(reqFormId, reqForm);
				}else {
					long reqFormCertId = rs.getLong("REQFORM_ID_1");
					if (reqFormCertId > 0) {
						
						RequestCertificate requestCertificate = new RequestCertificate();
						requestCertificate.setReqFormId(rs.getLong("REQFORM_ID_1"));
						requestCertificate.setCertificateCode(rs.getString("CERTIFICATE_CODE"));
						
						Certificate certificate = new Certificate();
						certificate.setCertificateId(rs.getLong("CERTIFICATE_ID"));
						certificate.setCode(rs.getString("CERT_CODE"));
						certificate.setCertificate(rs.getString("CERTIFICATE"));
						
						requestCertificate.setCertificate(certificate);
						
						reqForm.getCertificateList().add(requestCertificate);
						
						map.put(reqFormId, reqForm);
					}
				}
			}
			return new ArrayList<RequestForm>(map.values());
		}

	};
	
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
		List<String> officeCode2 = jdbcTemplate.query(this.QUERY_OFFICE_CODE, new Object[] {officeCode1}, officeCode2RowMapper);
		return !CollectionUtils.isEmpty(officeCode2) ? officeCode2.get(0) : "";
	}
	
	private RowMapper<String> officeCode2RowMapper = new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int arg1) throws SQLException {
			return rs.getString("OFFICE_CODE2");
		}
	};
}
