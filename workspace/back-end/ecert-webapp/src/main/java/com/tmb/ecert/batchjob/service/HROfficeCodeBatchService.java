package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOB_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class HROfficeCodeBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HROFFICECODE);
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	public void runBatchJob() {

		Date runDate = new Date();
		long start = System.currentTimeMillis();
		log.info(" Start HROfficeCodeBatch Process... ");
		
		
		String errorDesc = "";
		try {
			String path = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_IP);
			String host = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_IP);
			String username = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_USERNAME);
			String password = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_PASSWORD);
			String fileName = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HROFFICECODE_FILENAME);
			
			SftpVo sftpVo = new SftpVo(new SftpFileVo(path, fileName), host, username, password);
			File file = SftpUtils.getFile(sftpVo);
			
			if (this.validateContentFile(errorDesc, file)) {
				List<String> contents = FileUtils.readLines(file, StandardCharsets.UTF_8);
				for (String content : contents) {
					
				}
			} else {
				errorDesc = sftpVo.getErrorMessage();
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
	
	private Long saveHROfficeCode(Date runDate) {
	
		return 0L;
	}
	
}
