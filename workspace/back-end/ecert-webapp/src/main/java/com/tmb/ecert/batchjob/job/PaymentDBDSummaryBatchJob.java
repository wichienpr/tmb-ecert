package com.tmb.ecert.batchjob.job;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tmb.ecert.batchjob.service.PaymentDBDSummaryBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

public class PaymentDBDSummaryBatchJob extends QuartzJobBean{

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_PAYMENTSUMMARYDBD);
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			PaymentDBDSummaryBatchService paymentDBDSummaryBatchService = (PaymentDBDSummaryBatchService) dataMap.get("paymentDBDSummaryBatchService");
			paymentDBDSummaryBatchService.paymentDBDSummary(new Date());
		}catch (Exception e) {
			log.error("PaymentDBDSummaryBatchJob: " , e);
		}
	}

}
