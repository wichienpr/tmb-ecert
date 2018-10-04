package com.tmb.ecert.batchjob.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tmb.ecert.batchjob.job.HouseKeepingBatchJob;
import com.tmb.ecert.batchjob.service.HouseKeepingBatchService;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.housekeeping.archive.cornexpression" , havingValue="" ,matchIfMissing=false)
public class HousekeepAuditLogConfig {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HOUSEKEEPING);

	@Value("${job.housekeeping.archive.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private HouseKeepingBatchService houseKeepingBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ HouseKeepingBatchJobConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ HouseKeepingBatchJobConfig ################");
	}
	
	@Bean("houseKeepingBatchJobConfigJobDetail")
	public JobDetail houseKeepingBatchJobConfigJobDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("houseKeepingBatchService", houseKeepingBatchService);
		JobDetail job = JobBuilder.newJob(HouseKeepingBatchJob.class)
			      .withIdentity("houseKeepingBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("houseKeepingBatchJobConfigCronTrigger")
	public CronTrigger houseKeepingBatchJobConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("houseKeepingBatchJobConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("houseKeepingBatchJobConfigSchedulerFactory")
	public SchedulerFactory  houseKeepingBatchJobConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(houseKeepingBatchJobConfigJobDetail(), houseKeepingBatchJobConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("houseKeepingBatchJobConfig.. shutdown");
		houseKeepingBatchJobConfigSchedulerFactory().getScheduler().shutdown();
	}

}

