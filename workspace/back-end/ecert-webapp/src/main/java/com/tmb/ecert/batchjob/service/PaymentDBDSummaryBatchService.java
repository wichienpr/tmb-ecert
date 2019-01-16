package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.dao.PaymentDBDSummaryBatchDao;
import com.tmb.ecert.batchjob.domain.DBDSummaryTransactionFile;
import com.tmb.ecert.batchjob.domain.DBDSummaryTransactionFile.Detail;
import com.tmb.ecert.batchjob.domain.DBDSummaryTransactionFile.Header;
import com.tmb.ecert.batchjob.domain.DBDSummaryTransactionFile.Total;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.constant.ProjectConstant.SYSTEM;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "job.paymentdbd.summary.cornexpression", havingValue = "", matchIfMissing = false)
public class PaymentDBDSummaryBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_PAYMENTSUMMARYDBD);

	@Autowired
	private PaymentDBDSummaryBatchDao paymentDBDSummaryBatchDao;

	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;

	/**
	 * 
	 */
	public void paymentDBDSummary(Date reqDate) {
		boolean isSuccess = false;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		log.info("PaymentDBDSummaryBatchService is starting process...");
		StringBuilder metaData = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy", new Locale("th", "TH"));
		SimpleDateFormat formatterDD = new SimpleDateFormat("d");
		
		String path =  ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_DBDSUMMARY_FTPPATH);
		String archivePath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_DBDSUMMARY_ARCHIVEPATH);
		String ftpHost = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_DBDSUMMARY_FTPHOST);
		String ftpUsername = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_DBDSUMMARY_FTPUSERNAME);
		String ftpPassword = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_DBDSUMMARY_FTPPWD);

		try {
			//############################ MAPPING FIELD DBD PAYMENT SUMMARY BEGIN #################################
			List<RequestForm> reqFormList = paymentDBDSummaryBatchDao.getPaymentDBDReqFormWithReqDate(reqDate);
			if (reqFormList != null && reqFormList.size() > 0) {
				DBDSummaryTransactionFile bankTrasactionFile= mapping(reqFormList);
				if(bankTrasactionFile!=null) {
					
					metaData.append(bankTrasactionFile.tranformHeadfer());
					metaData.append(System.lineSeparator());
					metaData.append(StringUtils.join(bankTrasactionFile.tranformDetail(), System.lineSeparator()));
					metaData.append(System.lineSeparator());
					metaData.append(bankTrasactionFile.tranformTotal());
					
					String fileName = String.format("%s_%s_%s_%d.txt", bankTrasactionFile.getHeader().bankCode,
							bankTrasactionFile.getHeader().serviceCode,formatter.format(current),1);
					
					String fullPath = path + "/" + formatterDD.format(current);
					
					String fullFileName = archivePath + File.separator + File.separator+fileName;
					
					File file = new File(fullFileName);
					
					FileUtils.writeByteArrayToFile(file, metaData.toString().getBytes("TIS-620"));
					
					// Step 2. SFTP File and save log fail or success !!
					List<SftpFileVo> files = new ArrayList<>();
					files.add(new SftpFileVo(new File(fullFileName), fullPath, fileName));
					SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername,  TmbAesUtil.decrypt(keystorePath, ftpPassword));
					isSuccess = SftpUtils.putFile(sftpVo);
					if(!isSuccess){
						log.error("PaymentDBDSummaryBatchService FTP Error: {} ",sftpVo.getErrorMessage());
						jobMonitoring.setErrorDesc(sftpVo.getErrorMessage());
						emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP ,sftpVo.getErrorMessage() );
					}
				}else {
					jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_CONVERT);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			//############################ MAPPING FIELD DBD PAYMENT SUMMARY END #################################
		} catch (Exception ex) {
			log.error("PaymentDBDSummaryBatchService Error = ", ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			log.info("PaymentDBDSummaryBatchService is working Time(ms) = " + (end - start));
		
			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.DBDSUMMARY_TYPE);
			jobMonitoring.setEndOfDate(current);
			jobMonitoring.setStopDate(new Date());
			jobMonitoring.setStatus(isSuccess ? JOBMONITORING.SUCCESS : JOBMONITORING.FAILED);
			jobMonitoring.setRerunNumber(0);
			jobMonitoring.setRerunById(CHANNEL.BATCH);
			jobMonitoring.setRerunByName(CHANNEL.BATCH);
			jobMonitoring.setRerunDatetime(new Date());
			jobMonitoringDao.insertEcertJobMonitoring(jobMonitoring);
			//############################ INSERT JOBMONITORING BATCH END ###########################################
		}
		log.info("PaymentDBDSummaryBatchService end process...");

	}
	
	private DBDSummaryTransactionFile mapping(List<RequestForm> reqFormList) {
		
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat hhMMss = new SimpleDateFormat("HHmmss");
		DecimalFormat BATCH_MONEY_FORMATCH = new DecimalFormat("###.00");

		String dbdAccountNo = ApplicationCache.getParamValueByName(SYSTEM.DBD_ACCOUNT_NUMBER);
		String companyName = ApplicationCache.getParamValueByName(SYSTEM.DBD_ACCOUNT_NAME);
		String serviceCode = ApplicationCache.getParamValueByName(SYSTEM.TMB_SERVICE_CODE);
		String transactionCode = ApplicationCache.getParamValueByName(SYSTEM.TMB_TRANSACTION_CODE);
		String bankCode = ApplicationCache.getParamValueByName(SYSTEM.TMB_BANKCODE);
		String tranType = ApplicationCache.getParamValueByName(SYSTEM.TMB_TRAN_TYPE);

		DBDSummaryTransactionFile bf = null;
		
		try {
			bf = new DBDSummaryTransactionFile();
			Header header = bf.getHeader();
			header.recordType = "H";
			header.sequenceNumber = "1";
			header.bankCode = BeanUtils.toString(bankCode);
			header.companyAccountNumber = dbdAccountNo;
			header.companyName = BeanUtils.toString(companyName);
			header.effectiveDate = ddMMyyyy.format(new Date());
			header.serviceCode = BeanUtils.toString(serviceCode);
			header.spare = StringUtils.EMPTY;

			int i = 1;
			BigDecimal sumAmount = BigDecimal.ZERO;
			for (RequestForm reqForm : reqFormList) {
				List<Detail> details = bf.getDetail();
				Detail detail = bf.createDetail();
				detail.recordType = "D";
				detail.sequenceNumber = String.valueOf(++i);
				detail.bankCode = BeanUtils.toString(bankCode);
				detail.companyAccountNumber = BeanUtils.toString(dbdAccountNo);
				detail.paymentDate = BeanUtils.isNotNull(reqForm.getPaymentDate()) ? ddMMyyyy.format(reqForm.getPaymentDate()) : StringUtils.EMPTY;
				detail.paymentTime = BeanUtils.isNotNull(reqForm.getPaymentDate()) ? hhMMss.format(reqForm.getPaymentDate()) : StringUtils.EMPTY;
				detail.customerName = BeanUtils.toString(reqForm.getCustomerName());
				detail.reference1 = BeanUtils.toString(reqForm.getRef1());
				detail.reference2 = BeanUtils.toString(reqForm.getRef2());
				detail.reference3 = StringUtils.EMPTY;
				detail.branchCode = BeanUtils.toString(reqForm.getPaymentBranchCode());
				detail.accountOwnerBranchCode = StringUtils.EMPTY;
				detail.kindofTransaction = BeanUtils.toString(tranType);
				detail.transactionCode = BeanUtils.toString(transactionCode);
				detail.chequeNumber = StringUtils.EMPTY;
				detail.chequeBankCode = StringUtils.EMPTY;
				detail.transactionDate =BeanUtils.isNotNull(reqForm.getPostDate()) ? ddMMyyyy.format(reqForm.getPostDate()) : StringUtils.EMPTY;
				detail.processDate = BeanUtils.isNotNull(reqForm.getPayLoadTs()) ? ddMMyyyy.format(reqForm.getPayLoadTs()) : StringUtils.EMPTY;
				detail.processTime = BeanUtils.isNotNull(reqForm.getPayLoadTs()) ? hhMMss.format(reqForm.getPayLoadTs()) : StringUtils.EMPTY;
				BigDecimal amount = reqForm.getAmountDbd();
				if (amount != null) {
					sumAmount = sumAmount.add(amount);
					detail.amount = replaceDot(BATCH_MONEY_FORMATCH.format(amount));
				}
				detail.spare = StringUtils.EMPTY;

				details.add(detail);
			}

			Total total = bf.getTotal();
			total.recordType = "T";
			total.sequenceNumber = String.valueOf(i);
			total.bankCode = BeanUtils.toString(bankCode);
			total.companyAccountNumber = BeanUtils.toString(dbdAccountNo);
			total.totalAmont = this.replaceDot(BATCH_MONEY_FORMATCH.format(sumAmount));
			total.totalTransaction = String.valueOf(reqFormList.size());
			total.spare = StringUtils.EMPTY;
		}catch(Exception e) {
			log.error("PaymentDBDSummaryBatchService Error = ",e);
		}
		return bf;
	}
	
	private String replaceDot(String str){
		if(str == null) return "";
		return str.replace(".", "");
	}
}
