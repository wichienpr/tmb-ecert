package com.tmb.ecert.batchjob.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.service.EcmMasterDataBatchService;

public class EcmMasterDataBatchJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ECMMASTERDATA);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			EcmMasterDataBatchService ecmMasterDataBatchService = (EcmMasterDataBatchService)  dataMap.get("ecmMasterDataBatchService");
			ecmMasterDataBatchService.runBatchJob();
			
		}catch (Exception e) {
			log.error("exception in EcmMasterDataBatchJob : " , e);
		}
		
	}

}
