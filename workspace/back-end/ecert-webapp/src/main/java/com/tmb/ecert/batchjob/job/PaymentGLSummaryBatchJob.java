package com.tmb.ecert.batchjob.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmb.ecert.batchjob.service.PaymentGLSummaryBatchService;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;

public class PaymentGLSummaryBatchJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_GL);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			PaymentGLSummaryBatchService paymentGLSummaryBatchService = (PaymentGLSummaryBatchService) dataMap.get("paymentGLSummaryBatchService");
			paymentGLSummaryBatchService.runBatchJob();
		}catch (Exception e) {
			log.error("exception in PaymentGLSummaryBatchJob : " , e);
		}
	
	}
}
