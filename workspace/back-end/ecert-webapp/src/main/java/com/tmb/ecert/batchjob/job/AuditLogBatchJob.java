package com.tmb.ecert.batchjob.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.tmb.ecert.batchjob.service.AuditLogBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

public class AuditLogBatchJob extends QuartzJobBean{

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUDITLOG);
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			AuditLogBatchService auditLogBatchService = (AuditLogBatchService) dataMap.get("auditLogBatchService");
			auditLogBatchService.transferAuditLogByActionCode(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_ACTIONCODE),new Date());
		}catch (Exception e) {
			log.error("AuditLogBatchJob: " , e);
		}
	}

}
