package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.HROFFICE_CODE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOB_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.dao.HROfficeCodeBatchDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertHROfficeCode;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.ProjectConstant.ENCODING;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class HROfficeCodeBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HROFFICECODE);
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private HROfficeCodeBatchDao hrOfficeCodeBatchDao;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;
	
	private final String DATE_FORMAT = "YYYYMMDD";
	private String DATE_FILE_NAME = "yyyyMMddHHmmss";
	
	public void runBatchJob() {

		Date runDate = new Date();
		long start = System.currentTimeMillis();
		log.info(" Start HROfficeCodeBatch Process... ");
		
		String errorDesc = "";
		try {
			String path = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_PATH);
			String host = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_IP);
			String username = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_USERNAME);
			String password = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_PASSWORD);
			String fileName = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_FILENAME);
			
			String fileType = ApplicationCache.getParamValueByName(HROFFICE_CODE.BATCH_HROFFICECODE_FILE_TYPE);
			String achiveFilePath = String.format("%s%s%s", ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_GL_ARCHIVE_FILE_PATH), 
					DateFormatUtils.format(runDate, DATE_FILE_NAME), fileType);
			
			SftpVo sftpVo = new SftpVo(new SftpFileVo(path, fileName), host, username, TmbAesUtil.decrypt(keystorePath, password), achiveFilePath);
			File file = SftpUtils.getFile(sftpVo);
			if (this.validateContentFile(errorDesc, file)) {
				List<EcertHROfficeCode> ecertHROfficeCodes = this.readFile(file);
				
				this.clearHROfficeCode();
				this.saveHROfficeCode(ecertHROfficeCodes);
				log.info("HROfficeCodeBatch save success total : {} records", ecertHROfficeCodes.size());
			} else {
				errorDesc = sftpVo.getErrorMessage();
				emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP , errorDesc);
			}
		} catch (Exception e) {
			errorDesc = e.getMessage();
			log.error("exception in HROfficeCodeBatch : ", e);
		} finally {
			if (StringUtils.isBlank(errorDesc)) {
				this.saveEcertJobMonitoring(runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.SUCCESS, "");
			} else {
				this.saveEcertJobMonitoring(runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.FAILED, errorDesc);
			}
			
			long end = System.currentTimeMillis();
			log.info("HROfficeCodeBatch is working Time : {} ms", (end - start));
		}
		log.info("End HROfficeCodeBatch Process... ");
	}
	
	private Long saveEcertJobMonitoring(Date runDate, Integer rerunNumber, String status, String errorDesc) {
		EcertJobMonitoring ecertJobMonitoring = new EcertJobMonitoring();
		Date nowDate = new Date();
		ecertJobMonitoring.setJobTypeCode(JOB_TYPE.HR_BATCH_JOB);
		ecertJobMonitoring.setStartDate(runDate);
		ecertJobMonitoring.setStopDate(nowDate);
		ecertJobMonitoring.setEndOfDate(runDate);
		ecertJobMonitoring.setStatus(status);
		ecertJobMonitoring.setErrorDesc(errorDesc);
		ecertJobMonitoring.setRerunNumber(rerunNumber);
		ecertJobMonitoring.setRerunById(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunByName(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunDatetime(nowDate);
		return jobMonitoringDao.insertEcertJobMonitoring(ecertJobMonitoring);
	}
	
	private boolean validateContentFile(String errorDesc, File file) {
		return StringUtils.isBlank(errorDesc) && file != null && file.length() > 0;
	}
	
	private String getValue(String content, int start, int end) {
		return StringUtils.trim(StringUtils.substring(content, start, end));
	}
	
	private List<EcertHROfficeCode> readFile(File file) throws Exception {
		List<String> contents = FileUtils.readLines(file, ENCODING.TIS_620);
		
		List<EcertHROfficeCode> ecerthrOfficeCodes = new ArrayList<>();
		for (String content : contents) {
			EcertHROfficeCode ecertHROfficeCode = new EcertHROfficeCode();
			ecertHROfficeCode.setEffectiveDate(DateUtils.parseDate(this.getValue(content, 0, 8), this.DATE_FORMAT));
			ecertHROfficeCode.setStatus(getValue(content, 8, 9));
			ecertHROfficeCode.setType(getValue(content, 9, 11));
			ecertHROfficeCode.setOfficeCode1(getValue(content, 11, 15));
			ecertHROfficeCode.setOfficeCode2(getValue(content, 15, 25));
			ecertHROfficeCode.setTdeptEn(getValue(content, 25, 175));
			ecertHROfficeCode.setDescrshortEn(getValue(content, 175, 205));
			ecertHROfficeCode.setTdeptTh(getValue(content, 205, 255));
			ecertHROfficeCode.setDescrshortTh(getValue(content, 255, 285));
			ecerthrOfficeCodes.add(ecertHROfficeCode);
		}
		
		return ecerthrOfficeCodes;
	}
	
	private void clearHROfficeCode() {
		hrOfficeCodeBatchDao.deleteEcertHROfficeCode();
	}
	
	private void saveHROfficeCode(List<EcertHROfficeCode> ecertHROfficeCodes) {
		hrOfficeCodeBatchDao.insertEcertHROfficeCode(ecertHROfficeCodes);
	}
	
}
