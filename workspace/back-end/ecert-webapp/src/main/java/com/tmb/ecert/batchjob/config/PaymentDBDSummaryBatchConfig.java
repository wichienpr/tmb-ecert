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

import com.tmb.ecert.batchjob.job.PaymentDBDSummaryBatchJob;
import com.tmb.ecert.batchjob.service.PaymentDBDSummaryBatchService;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.paymentdbd.summary.cornexpression" , havingValue="" ,matchIfMissing=false)
public class PaymentDBDSummaryBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_PAYMENTSUMMARYDBD);
	
	@Value("${job.paymentdbd.summary.cornexpression}")
	private String cronExpressions;
	
	@Autowired
	private PaymentDBDSummaryBatchService paymentDBDSummaryBatchService;
	
	@PostConstruct
	public void init() {
		log.info("################ PaymentDBDSummaryBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ PaymentDBDSummaryBatchConfig ################");
	}
	
	@Bean("paymentDBDSummaryBatchConfigDetail")
	public JobDetail paymentDBDSummaryBatchConfigDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("paymentDBDSummaryBatchService", paymentDBDSummaryBatchService);
		JobDetail job = JobBuilder.newJob(PaymentDBDSummaryBatchJob.class)
			      .withIdentity("paymentDBDSummaryBatchJob", "batchjobgroup") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("paymentDBDSummaryBatchConfigCronTrigger")
	public CronTrigger paymentDBDSummaryBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("paymentDBDSummaryBatchConfigCronTrigger", "batchjobgroup")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("paymentDBDSummaryBatchConfigSchedulerFactory")
	public SchedulerFactory  paymentDBDSummaryBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(paymentDBDSummaryBatchConfigDetail(), paymentDBDSummaryBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("PaymentDBDSummaryBatchConfig.. shutdown");
		paymentDBDSummaryBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
