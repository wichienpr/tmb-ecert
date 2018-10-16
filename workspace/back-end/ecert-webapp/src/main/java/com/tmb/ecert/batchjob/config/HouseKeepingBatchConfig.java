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
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.housekeeping.archive.cornexpression" , havingValue="" ,matchIfMissing=false)
public class HouseKeepingBatchConfig {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HOUSEKEEPING);

	@Value("${job.housekeeping.archive.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private HouseKeepingBatchService houseKeepingBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ HouseKeepingBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ HouseKeepingBatchConfig ################");
	}
	
	@Bean("houseKeepingBatchConfigDetail")
	public JobDetail houseKeepingBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("houseKeepingBatchService", houseKeepingBatchService);
		JobDetail job = JobBuilder.newJob(HouseKeepingBatchJob.class)
			      .withIdentity("houseKeepingBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("houseKeepingBatchConfigCronTrigger")
	public CronTrigger houseKeepingBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("houseKeepingBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("houseKeepingBatchConfigSchedulerFactory")
	public SchedulerFactory  houseKeepingBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(houseKeepingBatchConfigDetail(), houseKeepingBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("HouseKeepingBatchConfig.. shutdown");
		houseKeepingBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}

