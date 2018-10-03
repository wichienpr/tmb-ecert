package com.tmb.ecert.batchjob.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.tmb.ecert.batchjob.service.HouseKeepingBatchService;
import com.tmb.ecert.common.constant.ProjectConstant;

public class HouseKeepingBatchJob extends QuartzJobBean{

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HOUSEKEEPING);
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			HouseKeepingBatchService houseKeepingBatchService = (HouseKeepingBatchService) dataMap.get("houseKeepingBatchService");
			houseKeepingBatchService.archiveAuditLog();
		}catch (Exception e) {
			log.error("HouseKeepingBatchJob: " , e);
		}
	}

}
