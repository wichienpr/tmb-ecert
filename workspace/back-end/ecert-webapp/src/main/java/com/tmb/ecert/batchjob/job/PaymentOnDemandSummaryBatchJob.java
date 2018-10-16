package com.tmb.ecert.batchjob.job;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmb.ecert.batchjob.service.PaymentOnDemandSummaryBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

public class PaymentOnDemandSummaryBatchJob extends QuartzJobBean{

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ONDEMAND);
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			PaymentOnDemandSummaryBatchService paymentOnDemandSummaryBatchService = (PaymentOnDemandSummaryBatchService) dataMap.get("paymentOnDemandSummaryBatchService");
			paymentOnDemandSummaryBatchService.paymentOnDemandSummary(new Date());
		}catch (Exception e) {
			log.error("PaymentOnDemandSummaryBatchJob: " , e);
		}
	}

}
