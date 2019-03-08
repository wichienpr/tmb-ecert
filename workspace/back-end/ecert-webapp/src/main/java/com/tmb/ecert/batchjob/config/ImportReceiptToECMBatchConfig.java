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

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.job.ImportReceiptToECMBatchJob;
import com.tmb.ecert.batchjob.service.ImportECMBatchService;
import com.tmb.ecert.batchjob.service.ImportReceiptToECMBatchService;

@Configuration
@ConditionalOnProperty(name="job.ecmimport.receipt.cornexpression" , havingValue="" ,matchIfMissing=false)
public class ImportReceiptToECMBatchConfig {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);
	
	@Value("${job.ecmimport.receipt.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private ImportReceiptToECMBatchService importReceiptToECMBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ ImportReceiptToECMBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ ImportReceiptToECMBatchConfig ################");
	}
	
	@Bean("ImportReceiptToECMBatchConfigDetail")
	public JobDetail ImportReceiptToECMBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("importReceiptToECMBatchService", importReceiptToECMBatchService);
		JobDetail job = JobBuilder.newJob(ImportReceiptToECMBatchJob.class)
			      .withIdentity("importReceiptToECMBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("importReceiptToECMBatchConfigCronTrigger")
	public CronTrigger ImportReceiptToECMBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("importReceiptToECMBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("importReceiptToECMBatchConfigSchedulerFactory")
	public SchedulerFactory  ImportReceiptToECMBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(ImportReceiptToECMBatchConfigDetail(), ImportReceiptToECMBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("ImportReceiptToECMBatchConfig.. shutdown");
		ImportReceiptToECMBatchConfigSchedulerFactory().getScheduler().shutdown();
	}
}
