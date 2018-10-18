package com.tmb.ecert.batchjob.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeepaymentAutoRetryBatchJob  implements Job {

	private static final Logger log = LoggerFactory.getLogger(FeepaymentAutoRetryBatchJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("execute");
//		   	  JobKey key = context.getJobDetail().getKey();
//		      JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//		      String jobSays = dataMap.getString("jobSays");
//			try {
//				
//			}
//			}catch (Exception e) {
//				log.error("AdjLogsJobService" , e);
//			}
		
	}

}