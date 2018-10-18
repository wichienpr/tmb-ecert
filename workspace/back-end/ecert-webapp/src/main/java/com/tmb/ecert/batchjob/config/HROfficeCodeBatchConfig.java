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

import com.tmb.ecert.batchjob.job.HROfficeCodeBatchJob;
import com.tmb.ecert.batchjob.service.HROfficeCodeBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.hrofficecode.cornexpression" , havingValue="", matchIfMissing=false)
public class HROfficeCodeBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HROFFICECODE);

	@Autowired
	private HROfficeCodeBatchService hrOfficeCodeBatchService;
	
	@Value("${job.hrofficecode.cornexpression}")
	private String cronExpressions;
	
	@PostConstruct
	public void init(){
		log.info("################ HROfficeCodeBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ HROfficeCodeBatchConfig ################");
	}
	
	@Bean("hrOfficeCodeConfigJobDetail")
	public JobDetail hrOfficeCodeBatchConfigJobDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("hrOfficeCodeBatchService", hrOfficeCodeBatchService);
		JobDetail job = JobBuilder.newJob(HROfficeCodeBatchJob.class)
			      .withIdentity("hrOfficeCodeBatchConfigJobDetail", "group1")
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("hrOfficeCodeBatchConfigCronTrigger")
	public CronTrigger hrOfficeCodeBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("hrOfficeCodeBatchConfigCronTrigger", "group1")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("hrOfficeCodeBatchConfigSchedulerFactory")
	public SchedulerFactory hrOfficeCodeBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(hrOfficeCodeBatchConfigJobDetail(), hrOfficeCodeBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("HROfficeCodeBatchConfig.. shutdown");
		hrOfficeCodeBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
