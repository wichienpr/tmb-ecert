package com.tmb.ecert.batchjob.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.service.FeepaymentAutoRetryBatchService;

public class FeepaymentAutoRetryBatchJob extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUTOPAYMENT);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("execute");
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			FeepaymentAutoRetryBatchService feepaymentAutoRetryBatchService = (FeepaymentAutoRetryBatchService) dataMap.get("feepaymentAutoRetryBatchService");
			feepaymentAutoRetryBatchService.run();
		} catch (Exception e) {
			log.error("FeepaymentAutoRetryBatchJob: ", e);
		}
		log.info("finish");
	}

}