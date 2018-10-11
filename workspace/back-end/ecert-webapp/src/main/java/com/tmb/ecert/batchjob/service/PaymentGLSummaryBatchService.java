package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.ECERT_CUSTSEGMENT_CODE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.GL_CUSTSEGMENT_CODE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOB_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.OFFICE_CODE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PAID_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PAYMENT_GL_SUMMARY;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.dao.PaymentGLSummaryBatchDao;
import com.tmb.ecert.batchjob.domain.EcertJobGLFailed;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class PaymentGLSummaryBatchService {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_GL);
	
	@Autowired
	private PaymentGLSummaryBatchDao paymentGLSummaryBatchDao;
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	private String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
	private String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	private String DATE_FILE_NAME = "yyyyMMddHHmmss";
	private final String CONST_PIPE = "|";
	private final String CONST_DASH = "-";
	private final String CONST_COMMA = ",";
	
	public void runBatchJob() {
		Date runDate = new Date();
		long start = System.currentTimeMillis();
		log.info(" Start PaymentGLSummaryBatch Process... ");
		List<RequestForm> requestForms = paymentGLSummaryBatchDao.queryReqGlSummaryProcess(runDate);
		
		log.info(" PaymentGLSummaryBatch find process ==> {}", requestForms.size());
		
		String errorDesc = "";
		Date requestDate = runDate;
		try {
			List<String> contents = this.createContentFile(requestForms);
			contents.add(this.createTrailer(requestForms));
			
			String fileType = ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_HEADER_FILE_TYPE);
			String achiveFilePath = String.format("%s%s%s", ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_ARCHIVE_FILE_PATH), 
					DateFormatUtils.format(runDate, DATE_FILE_NAME), fileType);
			
			File file = this.writeFile(contents, StandardCharsets.UTF_8.name(), achiveFilePath);
			String path = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_SUMMARY_PATH);
			String host = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_SUMMARY_IP);
			String username = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_SUMMARY_USERNAME);
			String password = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_SUMMARY_PASSWORD);
			
			String fileName = this.createFileName(BatchJobConstant.RERUN_DEFAULT, runDate);
			List<SftpFileVo> files = new ArrayList<>();
			files.add(new SftpFileVo(file, path, fileName));
			SftpVo sftpVo = new SftpVo(files, host, username, password);
			boolean isSuccess = SftpUtils.putFile(sftpVo);
			
			if (!isSuccess) {
				errorDesc = sftpVo.getErrorMessage();
				log.error("error PaymentGLSummaryBatch sftp file : {} ", errorDesc);
			} 
		} catch (Exception e) {
			errorDesc = e.getMessage();
			log.error("exception in PaymentGLSummaryBatch : ", e);
		} finally {
			if (StringUtils.isBlank(errorDesc)) {
				this.saveEcertJobMonitoring(requestDate, runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.SUCCESS, "");
			} else {
				this.saveEcertJobMonitoring(requestDate, runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.FAILED, errorDesc);
				this.saveEcertJobGLFailed(requestDate);
			}
			
			long end = System.currentTimeMillis();
			log.info("PaymentGLSummaryBatch is working Time : {} ms", (end - start));
		}
		log.info("End PaymentGLSummaryBatch Process... ");
	}
	
	private Long saveEcertJobMonitoring(Date requestDate, Date runDate, Integer rerunNumber, String status, String errorDesc) {
		EcertJobMonitoring ecertJobMonitoring = new EcertJobMonitoring();
		Date nowDate = new Date();
		ecertJobMonitoring.setJobTypeCode(JOB_TYPE.GL_BATCH_JOB);
		ecertJobMonitoring.setStartDate(runDate);
		ecertJobMonitoring.setStopDate(nowDate);
		ecertJobMonitoring.setEndOfDate(requestDate);
		ecertJobMonitoring.setStatus(status);
		ecertJobMonitoring.setErrorDesc(errorDesc);
		ecertJobMonitoring.setRerunNumber(rerunNumber);
		ecertJobMonitoring.setRerunById(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunByName(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunDatetime(nowDate);
		return jobMonitoringDao.insertEcertJobMonitoring(ecertJobMonitoring);
	}
	
	private void saveEcertJobGLFailed(Date requestDate) {
		EcertJobGLFailed ecertJobGLFailed = new EcertJobGLFailed();
		ecertJobGLFailed.setPaymentDate(requestDate);
		paymentGLSummaryBatchDao.insertEcertJobGLFailed(ecertJobGLFailed);
	}
	
	private List<String> createContentFile(List<RequestForm> requestForms) {
		List<String> contents = new ArrayList<>();
		String effrectiveDate = this.covertDate(new Date(), DATE_FORMAT_DDMMYYYY);
		for (RequestForm request : requestForms) {
			if (PAID_TYPE.CUSTOMER_PAY_DBD.equals(request.getPaidTypeCode())) {
				contents.add(this.customerPayContent(request, effrectiveDate));
			} else if (PAID_TYPE.CUSTOMER_PAY_DBD_TMB.equals(request.getPaidTypeCode())) {
				contents.add(this.customerPayContent(request, effrectiveDate));
			} else if (PAID_TYPE.TMB_PAY_DBD_TMB.equals(request.getPaidTypeCode())) {
				contents.add(this.tmbPayContent(request, effrectiveDate));
			}
		}
		
		return contents;
	}
	
	private String customerPayContent(RequestForm request, String effrectiveDate) {
		List<String> tmp = new ArrayList<>();
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_INDICATOR1), 0, 1));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_LEDGER1), 0, 30));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_SOURCECODE1), 0, 3));
		tmp.add(this.replaceValue(effrectiveDate, 0, 8));
		tmp.add(this.replaceValue(this.covertDate(request.getPayLoadTs(), DATE_FORMAT_DDMMYYYY), 0, 8));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_EVENTDESC1), 0, 80));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_ENTITYCODE1), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_INTERCODE1), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_OWNDER_BRANCHCODE1), 0, 4));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_ENTRY_BRANCHCODE), 0, 4));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_DESTINATION_BRANCHCODE1), 0, 4));
		tmp.add(this.replaceValue(this.getOfficeCode(request.getCustsegmentCode(), request.getOfficeCode()), 0, 10));
		tmp.add(this.replaceValue(request.getOfficeCode(), 0, 10));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_DESCTICATION_OFFICECODE1), 0, 10));
		tmp.add(this.replaceValue(getGLCustomerCode(request.getCustsegmentCode()), 0, 3));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_PRODUCTCODE1), 0, 240));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CHANNELCODE1), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_PROJECTCODE1), 0, 8));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_TAXCODE1), 0, 2));
		tmp.add(this.replaceValue(this.getAmountNoVat(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CURRENCY_CODE1), 0, 3));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(this.getAmountNoVat(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CURRENCY_CODE1), 0, 3));
		tmp.add(this.replaceValue(getCertificateRequest(request.getCertificateList()), 0, 240));
		tmp.add(this.replaceValue(request.getTmbRequestNo(), 0, 30));
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(request.getReceiptNo(), 0, 30));
		tmp.add(this.replaceValue(this.covertDate(request.getReceiptDate(), DATE_FORMAT_DDMMYYYY), 0, 8));
		tmp.add("");
		tmp.add(this.replaceValue(request.getOrganizeId(), 0, 13));
		tmp.add(this.replaceValue(request.getCompanyName(), 0, 100));
		tmp.add(this.replaceValue(request.getAddress(), 0, 100));
		tmp.add(this.replaceValue(request.getAddress(), 100, 200));
		tmp.add(this.replaceValue(request.getAddress(), 200, 300));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_RD_PLACE), 0, 100));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(this.getAmountNoVat(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(this.getVatValue(request.getAmountDbd()), 0, 16));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(request.getAccountNo(), 0, 30));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(this.getVatValue(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_AMT_CURRENCY_CODE), 0, 3));
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(this.getVatValue(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_AMT_CURRENCY_CODE), 0, 3));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		
		return StringUtils.join(tmp, CONST_PIPE);
	}
	
	private String tmbPayContent(RequestForm request, String effrectiveDate) {
		List<String> tmp = new ArrayList<>();
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_INDICATOR2), 0, 1));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_LEDGER2), 0, 30));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_SOURCECODE2), 0, 3));
		tmp.add(this.replaceValue(effrectiveDate, 0, 8));
		tmp.add(this.replaceValue(this.covertDate(request.getPayLoadTs(), DATE_FORMAT_DDMMYYYY), 0, 8));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_EVENTDESC2), 0, 80));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_ENTITYCODE2), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_INTERCODE2), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_OWNDER_BRANCHCODE2), 0, 4));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_ENTRY_BRANCHCODE), 0, 4));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_DESTINATION_BRANCHCODE2), 0, 4));
		tmp.add(this.replaceValue(request.getOfficeCode(), 0, 10));
		tmp.add(this.replaceValue(request.getOfficeCode(), 0, 10));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_DESCTICATION_OFFICECODE2), 0, 10));
		tmp.add(this.replaceValue(getGLCustomerCode(request.getCustsegmentCode()), 0, 3));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_PRODUCTCODE2), 0, 240));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CHANNELCODE2), 0, 2));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_PROJECTCODE2), 0, 8));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_TAXCODE2), 0, 2));
		tmp.add(this.replaceValue(this.getAmountNoVat(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CURRENCY_CODE2), 0, 3));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add(this.replaceValue(this.getAmountNoVat(request.getAmountDbd()), 0, 16));
		tmp.add(this.replaceValue(ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_CURRENCY_CODE2), 0, 3));
		tmp.add(this.replaceValue(getCertificateRequest(request.getCertificateList()), 0, 240));
		tmp.add(this.replaceValue(request.getTmbRequestNo(), 0, 30));
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		tmp.add("");
		
		return StringUtils.join(tmp, CONST_PIPE);
	}
	
	private String createTrailer(List<RequestForm> requestForms) {
		List<String> trailer = new ArrayList<>();
		String totalCount = String.valueOf(requestForms.size());
		String indicator = "T"; 
		BigDecimal calSumTransaction = new BigDecimal(0.0);
		for (RequestForm request : requestForms) {
			BigDecimal enteredAmount = new BigDecimal(this.getDefaultAmount(request.getAmountDbd()));
			calSumTransaction = calSumTransaction.add(enteredAmount);
		}
		String sumTransaction = String.format("%.2f", calSumTransaction.doubleValue());

		trailer.add(indicator);
		trailer.add(totalCount);
		trailer.add(sumTransaction);
		return StringUtils.join(trailer, CONST_PIPE);
	}
	
	private String createFileName(Integer rerunNumber, Date runDate) {
		String sourceSystemFrom = ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_HEADER_SOURCE_FROM);
		String sourceSystemTo = ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_HEADER_SOURCE_TO);
		String typeData = ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_HEADER_TYPE_DATA);
		String fileType = ApplicationCache.getParamValueByName(PAYMENT_GL_SUMMARY.BATCH_GL_HEADER_FILE_TYPE);
		String version = StringUtils.leftPad(String.valueOf(rerunNumber), 3, "0");
		return String.format("%s_%s_%s_%s_%s%s", sourceSystemFrom, sourceSystemTo, typeData, this.covertDate(runDate, DATE_FORMAT_YYYYMMDD), version, fileType); 
	}
	
	private String covertDate(Date date, String format) {
		String dateFormat = "";
		if (date != null) {
			dateFormat = DateFormatUtils.format(date, format);
		}
		return dateFormat;
	}
	private String getOfficeCode(String customerCode, String officeCode) {
		
		String officeCode1 = "";
		String officeCode2 = "";
		if (ECERT_CUSTSEGMENT_CODE.SB.equals(customerCode)) {
			officeCode1 = OFFICE_CODE.OFFICE_1092;
			officeCode2 = paymentGLSummaryBatchDao.queryOfficeCode2(officeCode1);
		} else if (ECERT_CUSTSEGMENT_CODE.BB.equals(customerCode)) {
			officeCode1 = OFFICE_CODE.OFFICE_1078;
			officeCode2 = paymentGLSummaryBatchDao.queryOfficeCode2(officeCode1);
		} else if (ECERT_CUSTSEGMENT_CODE.MB.equals(customerCode)) {
			officeCode1 = OFFICE_CODE.OFFICE_1078;
			officeCode2 = paymentGLSummaryBatchDao.queryOfficeCode2(officeCode1);
		} else {
			officeCode2 = officeCode;
		}
		
		return officeCode2;
	}
	
	private String getGLCustomerCode(String customerCode) {
		String glCustomerCode = "";
		if (ECERT_CUSTSEGMENT_CODE.SB.equals(customerCode)) {
			glCustomerCode = GL_CUSTSEGMENT_CODE.SB;
		} else if (ECERT_CUSTSEGMENT_CODE.BB.equals(customerCode)) {
			glCustomerCode = GL_CUSTSEGMENT_CODE.BB;
		} else if (ECERT_CUSTSEGMENT_CODE.CB.equals(customerCode)) {
			glCustomerCode = GL_CUSTSEGMENT_CODE.CB;
		} else if (ECERT_CUSTSEGMENT_CODE.MB.equals(customerCode)) {
			glCustomerCode = GL_CUSTSEGMENT_CODE.MB;
		}
		return glCustomerCode;
	}
	
	private String getAmountNoVat(BigDecimal amount) {
		return String.format("%.2f", this.getDefaultAmount(amount));
	}
	
	private Double getDefaultAmount(BigDecimal amount) {
		Double value = 0.0;
		if (amount != null && amount.doubleValue() > 0) {
			Double vat = NumberUtils.toDouble(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.VAT_PERCENT));
			value = amount.doubleValue() / ( (100 + vat) / 100 );
		}
		return value;
	}
	
	private String getVatValue(BigDecimal amount) {
		String vatValue = "";
		if (amount != null && amount.doubleValue() > 0) {
			Double value = amount.doubleValue() - this.getDefaultAmount(amount);
			vatValue = String.format("%.2f", value);
		}
		return vatValue;
	}
	
	private String replaceValue(String value, int start, int length) {
		String v = StringUtils.isNotBlank(value) ? StringUtils.replace(value, CONST_PIPE, CONST_DASH) : "";
		
		if (v.length() > length) {
			v = StringUtils.substring(v, start, length);
		}
		return v;
	}
	
	private String getCertificateRequest(List<RequestCertificate> certificateList) {
		List<String> certificates = new ArrayList<>();
		for (RequestCertificate requestCertificate : certificateList) {
			if (requestCertificate.getCertificate() != null) {
				certificates.add(requestCertificate.getCertificate().getCertificate());
			}
		}
		return StringUtils.join(certificates, CONST_COMMA);
	}
	
	private File writeFile(List<String> content, String encoding, String archiveFilePath) throws Exception {
		OutputStreamWriter writer = null;
		File file = new File(archiveFilePath);
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file, true), encoding);
			FileUtils.writeLines(file, content, false);
		} catch (Exception e) {
			log.error("exception write file : ", e);
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return file;
	}
}
