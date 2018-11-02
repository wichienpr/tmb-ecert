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

import com.tmb.ecert.batchjob.job.FeepaymentAutoRetryBatchJob;
import com.tmb.ecert.batchjob.service.FeepaymentAutoRetryBatchService;

@Configuration
@ConditionalOnProperty(name="job.autoRetryFeePayment.cornexpression" , havingValue="" ,matchIfMissing=false)
public class FeepaymentAutoRetryBatchConfig {

	
	private static final Logger log = LoggerFactory.getLogger(FeepaymentAutoRetryBatchConfig.class);

	@Autowired
	private FeepaymentAutoRetryBatchService feepaymentAutoRetryBatchService;
	
	@Value("${job.autoRetryFeePayment.cornexpression}")
	private String cronExpressions;
	
	@PostConstruct
	public void init(){
		log.info("################ FeepaymentAutoRetryBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ FeepaymentAutoRetryBatchConfig ################");
	}
	
	@Bean("feepaymentAutoRetryBatchConfigJobDetail")
	public JobDetail feepaymentAutoRetryBatchConfigJobDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("feepaymentAutoRetryBatchService", feepaymentAutoRetryBatchService);
		JobDetail job = JobBuilder.newJob(FeepaymentAutoRetryBatchJob.class)
			      .withIdentity("backuplog", "group1") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("feepaymentAutoRetryBatchConfigCronTrigger")
	public CronTrigger feepaymentAutoRetryBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("feepaymentAutoRetryBatchConfigCronTrigger", "group1")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("feepaymentAutoRetryBatchConfigSchedulerFactory")
	public SchedulerFactory  feepaymentAutoRetryBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(feepaymentAutoRetryBatchConfigJobDetail(), feepaymentAutoRetryBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("FeepaymentAutoRetryBatchConfig.. shutdown");
		feepaymentAutoRetryBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
