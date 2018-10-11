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

import com.tmb.ecert.batchjob.job.PaymentOnDemandSummaryBatchJob;
import com.tmb.ecert.batchjob.service.PaymentOnDemandSummaryBatchService;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.ondemand.summary.cornexpression" , havingValue="" ,matchIfMissing=false)
public class PaymentOnDemandSummaryBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ONDEMAND);
	
	@Value("${job.ondemand.summary.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private PaymentOnDemandSummaryBatchService paymentOnDemandSummaryBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ PaymentOnDemandSummaryBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ PaymentOnDemandSummaryBatchConfig ################");
	}
	
	@Bean("paymentOnDemandSummaryBatchConfigDetail")
	public JobDetail paymentOnDemandSummaryBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("paymentOnDemandSummaryBatchService", paymentOnDemandSummaryBatchService);
		JobDetail job = JobBuilder.newJob(PaymentOnDemandSummaryBatchJob.class)
			      .withIdentity("paymentOnDemandSummaryBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("paymentOnDemandSummaryBatchConfigCronTrigger")
	public CronTrigger paymentOnDemandSummaryBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("paymentOnDemandSummaryBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("paymentOnDemandSummaryBatchConfigSchedulerFactory")
	public SchedulerFactory  paymentOnDemandSummaryBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(paymentOnDemandSummaryBatchConfigDetail(), paymentOnDemandSummaryBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("PaymentOnDemandSummaryBatchConfig.. shutdown");
		paymentOnDemandSummaryBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
