package com.tmb.ecert.batchjob.service;

import java.io.File;
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

import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.ONDEMAND;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.dao.PaymentOnDemandSummaryBatchDao;
import com.tmb.ecert.batchjob.domain.DetailOndemand;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.batchjob.domain.HeaderOndemand;
import com.tmb.ecert.batchjob.domain.OnDemandSummaryTransactionFile;
import com.tmb.ecert.batchjob.domain.OnDemandSummaryTransactionFile.Page;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.ListOfValue;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "job.ondemand.summary.cornexpression", havingValue = "", matchIfMissing = false)
public class PaymentOnDemandSummaryBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ONDEMAND);

	@Autowired
	private PaymentOnDemandSummaryBatchDao paymentOnDemandSummaryBatchDao;

	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired 
	private EmailService  emailService;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;


	/**
	 * 
	 */
	public void paymentOnDemandSummary(Date reqDate) {
		boolean isSuccess = false;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		log.info("PaymentOnDemandSummaryBatchService is starting process...");
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy", Locale.US);
		SimpleDateFormat formatterDD = new SimpleDateFormat("d");
		
		
		String path =  ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_FTPPATH);
		String fileName =  ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_FILENAME);
		String archivePath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_ARCHIVEPATH);
		String ftpHost = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_FTPHOST);
		String ftpUsername = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_FTPUSERNAME);
		String ftpPassword = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_ONDEMANDSUMMARY_FTPPWD);
		
		try {
			//############################ GENERATE AND FTP ONDEMAND PAYMENT SUMMARY BEGIN ##########################
			List<RequestForm> reqFormList = paymentOnDemandSummaryBatchDao.getPaymentDBDReqFormWithReqDate(reqDate);
			
			if (reqFormList != null && reqFormList.size() > 0) {
				
				String reportId = String.format(ApplicationCache.getParamValueByName(ONDEMAND.REPORTID),"001");
				
				OnDemandSummaryTransactionFile onDemandSummaryTransactionFile= mapping(reqFormList,reportId);
				
				if(onDemandSummaryTransactionFile!=null) {
					
					fileName = String.format(fileName, reportId,ddMMyyyy.format(current));
										
					String fullFileName = archivePath + File.separator + File.separator+fileName;
					
					String fullPath = path + "/" + formatterDD.format(current);
					
					File file = new File(fullFileName);
															
					FileUtils.writeByteArrayToFile(file, onDemandSummaryTransactionFile.getPage().getDetails().toString().getBytes("TIS-620"));
										
					// Step 2. SFTP File and save log fail or success !!
					List<SftpFileVo> files = new ArrayList<>();
					files.add(new SftpFileVo(new File(fullFileName), fullPath , fileName));
					SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername,  TmbAesUtil.decrypt(keystorePath, ftpPassword));
					isSuccess = SftpUtils.putFile(sftpVo);
					if(!isSuccess){
						log.error("PaymentOnDemandSummaryBatchService FTP Error: {} ",sftpVo.getErrorMessage());
						jobMonitoring.setErrorDesc(sftpVo.getErrorMessage());
						emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP, sftpVo.getErrorMessage());
					}
				}else {
					jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_CONVERT);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			//############################ GENERATE AND FTP ONDEMAND PAYMENT SUMMARY END #############################

		} catch (Exception ex) {
			log.error("PaymentOnDemandSummaryBatchService Error = ", ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			log.info("PaymentOnDemandSummaryBatchService is working Time(ms) = " + (end - start));

			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.ONDEMAND_TYPE);
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
		log.info("PaymentOnDemandSummaryBatchService end process...");
	}
	
	private OnDemandSummaryTransactionFile mapping(List<RequestForm> reqFormList,String reportId) {		
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat amountFormat = new DecimalFormat("#,##0.00");
		OnDemandSummaryTransactionFile onDemandTrnFile = new OnDemandSummaryTransactionFile();
		try {
			//############################ MAPPING FIELD ONDEMAND PAYMENT SUMMARY BEGIN #################################
			Date asDate = new Date();
			
			Page page = onDemandTrnFile.getPage();
			int totalData = reqFormList.size();
			int totalPage = totalData / ONDEMAND.NUMBER_OF_PAGE;
			
			if(totalData % ONDEMAND.NUMBER_OF_PAGE == 0 ) {
				totalPage = totalData / ONDEMAND.NUMBER_OF_PAGE;
			}else {
				totalPage = (totalData / ONDEMAND.NUMBER_OF_PAGE)+1;
			}
			
			int start = 1;
			int numberOfDetail = 0;
			while(start <= totalPage ) {
				HeaderOndemand header = new HeaderOndemand();				
				
				//Header Line 1
				header.setControlPage(String.valueOf(1));
				header.setBranchCode(ApplicationCache.getParamValueByName(ONDEMAND.OFFICECODE));
				header.setBankName(ApplicationCache.getParamValueByName(ONDEMAND.TMBBANK));
				header.setPageNo(String.valueOf(start));
				page.getDetails().append(onDemandTrnFile.tranformHeader(header, 1)).append(System.lineSeparator());
				
				//Header Line 2
				header = new HeaderOndemand();
				header.setBranchName(ApplicationCache.getParamValueByName(ONDEMAND.BRANCHNAME));
				header.setSystemName(ApplicationCache.getParamValueByName(ONDEMAND.SYSNAME));
				header.setAsDate(ddMMyyyy.format(asDate));
				page.getDetails().append(onDemandTrnFile.tranformHeader(header, 2)).append(System.lineSeparator());
				
				//Header Line 3
				header = new HeaderOndemand();
				header.setReportId(reportId);
				header.setReportName(ApplicationCache.getParamValueByName(ONDEMAND.REPORTNAME));
				header.setRunDate(ddMMyyyy.format(asDate));
				page.getDetails().append(onDemandTrnFile.tranformHeader(header , 3)).append(System.lineSeparator());
				
				//Header Line 4
				page.getDetails().append(onDemandTrnFile.tranformHeader(null , 4));
				
				//Header Line 5
				page.getDetails().append(onDemandTrnFile.tranformHeader(null , 5));
				
				//Detail
				page.getDetails().append(System.lineSeparator());
				page.getDetails().append(StringUtils.rightPad(StringUtils.EMPTY,1));
				page.getDetails().append(StringUtils.rightPad("",130,"-"));
				page.getDetails().append(System.lineSeparator());
				
				while(numberOfDetail < (ONDEMAND.NUMBER_OF_PAGE*start)) {
					if(numberOfDetail < totalData && reqFormList.get(numberOfDetail)!=null) {
						RequestForm reqForm = (RequestForm)reqFormList.get(numberOfDetail);
						DetailOndemand detail = new DetailOndemand();
						detail.setRequestDate(StringUtils.defaultString(ddMMyyyy.format(reqForm.getRequestDate()),StringUtils.EMPTY));
						detail.setTmeReqNo(StringUtils.defaultString(reqForm.getTmbRequestNo(),StringUtils.EMPTY));
						detail.setOrgId(StringUtils.defaultString(reqForm.getOrganizeId(),StringUtils.EMPTY));
						ListOfValue segmentCode = ApplicationCache.getLovByCode(reqForm.getCustsegmentCode());
						detail.setSegment(StringUtils.defaultString(segmentCode!=null ? segmentCode.getShortName().trim(): null ,StringUtils.EMPTY));
						ListOfValue paidType = ApplicationCache.getLovByCode(reqForm.getPaidTypeCode());
//						detail.setPaidType(StringUtils.defaultString(paidType!=null  ? paidType.getName(): null,StringUtils.EMPTY));
						detail.setAccountNo(StringUtils.defaultString(reqForm.getAccountNo(),StringUtils.EMPTY));
						detail.setAmount(StringUtils.defaultString(reqForm.getAmountTmb()!=null?amountFormat.format(reqForm.getAmountTmb()):"0.00"));
						detail.setPaidType(StringUtils.defaultString(paidType!=null  ? paidType.getName(): null,StringUtils.EMPTY));
						page.getDetails().append(onDemandTrnFile.tranformDetail(detail));
						page.getDetails().append(System.lineSeparator());		
					}
					numberOfDetail++;
				}
				start++;
			}
			//############################ MAPPING FIELD ONDEMAND PAYMENT SUMMARY END #################################
		}catch(Exception e) {
			log.error("PaymentDBDSummaryBatchService Error = ",e);
		}
		return onDemandTrnFile;
	}
}
