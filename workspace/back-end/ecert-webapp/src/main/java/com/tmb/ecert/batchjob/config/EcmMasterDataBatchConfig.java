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
import com.tmb.ecert.batchjob.job.EcmMasterDataBatchJob;
import com.tmb.ecert.batchjob.service.EcmMasterDataBatchService;

@Configuration
@ConditionalOnProperty(name="job.ecmimport.masterdata.cornexpression" , havingValue="", matchIfMissing=false)
public class EcmMasterDataBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ECMMASTERDATA);
	
	@Value("${job.ecmimport.masterdata.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private EcmMasterDataBatchService ecmMasterDataBatchService;
	
	@PostConstruct
	public void init(){
		log.info("################ EcmMasterDataBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ EcmMasterDataBatchConfig ################");
	}
	

	@Bean("ecmMasterDataBatchConfigJobDetail")
	public JobDetail ecmMasterDataBatchConfigJobDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("ecmMasterDataBatchService", ecmMasterDataBatchService);
		JobDetail job = JobBuilder.newJob(EcmMasterDataBatchJob.class)
			      .withIdentity("ecmMasterDataBatchConfigDetail", "group1")
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("ecmMasterDataBatchConfigCronTrigger")
	public CronTrigger ecmMasterDataBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("ecmMasterDataBatchConfigCronTrigger", "group1")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("ecmMasterDataBatchConfigSchedulerFactory")
	public SchedulerFactory ecmMasterDataBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(ecmMasterDataBatchConfigJobDetail(), ecmMasterDataBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("ECMMasterDataBatchConfig.. shutdown");
		ecmMasterDataBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
