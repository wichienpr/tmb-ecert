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

import com.tmb.ecert.batchjob.job.ImportECMBatchJob;
import com.tmb.ecert.batchjob.service.ImportECMBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.ecmimport.document.cornexpression" , havingValue="" ,matchIfMissing=false)
public class ImportECMBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);
	
	@Value("${job.ecmimport.document.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private ImportECMBatchService importECMBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ ImportECMBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ ImportECMBatchConfig ################");
	}
	
	@Bean("importECMBatchConfigDetail")
	public JobDetail importECMBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("importECMBatchService", importECMBatchService);
		JobDetail job = JobBuilder.newJob(ImportECMBatchJob.class)
			      .withIdentity("importECMBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("importECMBatchConfigCronTrigger")
	public CronTrigger importECMBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("importECMBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("importECMBatchConfigSchedulerFactory")
	public SchedulerFactory  importECMBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(importECMBatchConfigDetail(), importECMBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("ImportECMBatchConfig.. shutdown");
		importECMBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
