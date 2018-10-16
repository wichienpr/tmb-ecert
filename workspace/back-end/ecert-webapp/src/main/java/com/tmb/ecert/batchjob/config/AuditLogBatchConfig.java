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

import com.tmb.ecert.batchjob.job.AuditLogBatchJob;
import com.tmb.ecert.batchjob.service.AuditLogBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.auditlog.cornexpression" , havingValue="" ,matchIfMissing=false)
public class AuditLogBatchConfig {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUDITLOG);

	@Value("${job.auditlog.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private AuditLogBatchService auditLogBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ AuditLogBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ AuditLogBatchConfig ################");
	}
	
	@Bean("auditLogBatchConfigDetail")
	public JobDetail auditLogBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("auditLogBatchService", auditLogBatchService);
		JobDetail job = JobBuilder.newJob(AuditLogBatchJob.class)
			      .withIdentity("auditLogBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("auditLogBatchConfigCronTrigger")
	public CronTrigger auditLogBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("auditLogBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("auditLogBatchConfigSchedulerFactory")
	public SchedulerFactory auditLogBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(auditLogBatchConfigDetail(), auditLogBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("AuditLogBatchConfig.. shutdown");
		auditLogBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}

