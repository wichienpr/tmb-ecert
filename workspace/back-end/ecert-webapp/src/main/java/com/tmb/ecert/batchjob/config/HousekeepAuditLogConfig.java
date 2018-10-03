package com.tmb.ecert.batchjob.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.tmb.ecert.batchjob.job.HouseKeepingBatchJob;
import com.tmb.ecert.batchjob.job.HouseKeepingService;
import com.tmb.ecert.common.constant.ProjectConstant;

@Configuration
@ConditionalOnProperty(name="job.housekeeping.archive.cornexpression" , havingValue="" ,matchIfMissing=false)
public class HouseKeepingBatchJobConfig {

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

