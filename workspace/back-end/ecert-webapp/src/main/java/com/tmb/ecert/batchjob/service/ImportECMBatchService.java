package com.tmb.ecert.batchjob.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.dao.ImportECMBatchDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.RequestForm;

@Service
@ConditionalOnProperty(name = "job.ecmimport.document.cornexpression", havingValue = "", matchIfMissing = false)
public class ImportECMBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);

	@Autowired
	private ImportECMBatchDao importECMBatchDao;

	@Autowired
	private JobMonitoringDao jobMonitoringDao;

	/**
	 * 
	 */
	public void sendDocumentToECM() {
		boolean isSuccess = false;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		log.info("ImportECMBatchService is starting process...");
		
		
		try {
			//############################ SEND DOCUMENTS TO ECM BEGIN ##########################
			List<RequestForm> reqFormList = importECMBatchDao.getRequestFormWithEcmFlag();
			
			if (reqFormList != null && reqFormList.size() > 0) {
				//
			}
			//############################ SEND DOCUMENTS TO ECM END #############################

		} catch (Exception ex) {
			log.error("ImportECMBatchService Error = ", ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			log.info("ImportECMBatchService is working Time(ms) = " + (end - start));

			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.IMPORT_ECM);
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
		log.info("ImportECMBatchService end process...");
	}
}
