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

import com.tmb.ecert.batchjob.job.PaymentGLSummaryBatchJob;
import com.tmb.ecert.batchjob.service.PaymentGLSummaryBatchService;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;

@Configuration
@ConditionalOnProperty(name="job.gl.summary.cornexpression" , havingValue="" ,matchIfMissing=false)
public class PaymentGLSummaryBatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_GL);

	@Autowired
	private PaymentGLSummaryBatchService paymentGLSummaryBatchService;
	
	@Value("${job.gl.summary.cornexpression}")
	private String cronExpressions;
	
	@PostConstruct
	public void init(){
		log.info("################ PaymentGLSummaryBatchConfig ################");
		log.info("# ");
		log.info("# ");
		log.info("# cronEx : " + cronExpressions);
		log.info("# ");
		log.info("# ");
		log.info("################ PaymentGLSummaryBatchConfig ################");
	}
	
	@Bean("paymentGLSummaryBatchConfigJobDetail")
	public JobDetail paymentGLSummaryBatchConfigJobDetail() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("paymentGLSummaryBatchService", paymentGLSummaryBatchService);
		JobDetail job = JobBuilder.newJob(PaymentGLSummaryBatchJob.class)
			      .withIdentity("paymentGLSummaryBatchConfigJobDetail", "group1") // name "myJob", group "group1"
			      .usingJobData(newJobDataMap)
			      .build();
		return job;
	}
	
	@Bean("paymentGLSummaryBatchConfigCronTrigger")
	public CronTrigger paymentGLSummaryBatchConfigCronTrigger() {
		CronTrigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("paymentGLSummaryBatchConfigCronTrigger", "group1")
			    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpressions))
			    .build();
		return trigger;
	}
	
	@Bean("paymentGLSummaryBatchConfigSchedulerFactory")
	public SchedulerFactory paymentGLSummaryBatchConfigSchedulerFactory() throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(paymentGLSummaryBatchConfigJobDetail(), paymentGLSummaryBatchConfigCronTrigger());
		sched.start();
		return sf;
	}
	
	@PreDestroy
	public void destroy() throws SchedulerException {
		log.info("PaymentGLSummaryBatchConfig.. shutdown");
		paymentGLSummaryBatchConfigSchedulerFactory().getScheduler().shutdown();
	}

}
