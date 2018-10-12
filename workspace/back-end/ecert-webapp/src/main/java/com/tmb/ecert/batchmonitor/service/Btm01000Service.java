package com.tmb.ecert.batchmonitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tmb.ecert.auditlog.persistence.vo.Adl01000Vo;
import com.tmb.ecert.batchjob.constant.BatchJobConstant;
import com.tmb.ecert.batchjob.service.AuditLogBatchService;
import com.tmb.ecert.batchjob.service.HROfficeCodeBatchService;
import com.tmb.ecert.batchjob.service.HouseKeepingBatchService;
import com.tmb.ecert.batchjob.service.PaymentDBDSummaryBatchService;
import com.tmb.ecert.batchjob.service.PaymentGLSummaryBatchService;
import com.tmb.ecert.batchjob.service.PaymentOnDemandSummaryBatchService;
import com.tmb.ecert.batchmonitor.persistence.dao.BatchMonitoringDao;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.domain.CommonMessage;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;
import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class Btm01000Service {
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_BATCHMONITORING);

	
	@Autowired
	private BatchMonitoringDao batchDao;
	
	@Autowired
	private AuditLogBatchService auditlogBatchService;
	
	@Autowired
	private HouseKeepingBatchService houseKeepingBatchService;
	
	@Autowired
	private HROfficeCodeBatchService hrBatchService;
	
	@Autowired
	private PaymentDBDSummaryBatchService dbdBatchService ;
	
	@Autowired
	private PaymentOnDemandSummaryBatchService paymentOndemandService ;
	
	@Autowired
	private PaymentGLSummaryBatchService paymentGLService ;
	
	private static String BATCH_DBD = "60001";
	private static String BATCH_ONDEMAND = "60002";
	private static String BATCH_GL = "60003";
	private static String BATCH_HR = "60004";
	private static String BATCH_AUDILOG = "60005";
	private static String BATCH_HOUSEKEEP = "60006";
	
	public DataTableResponse<Btm01000Vo> getListBatch(Btm01000FormVo form) {
		DataTableResponse<Btm01000Vo> list  = new DataTableResponse<>();
		List<Btm01000Vo> adl01000VoList = batchDao.getListBatch(form);
		list.setData(adl01000VoList);
		int count = batchDao.conutListBatch(form);
		list.setRecordsTotal(count);

		return list;

	}
	
	@Async
	public void  rerunJon(Btm01000Vo form) {
		CommonMessage<String> msg = new CommonMessage<>();
		try {
			if(BATCH_AUDILOG.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_AUDILOG.");
				auditlogBatchService.transferAuditLogByActionCode(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_ACTIONCODE));
				
			}else if (BATCH_HOUSEKEEP.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_HOUSEKEEP.");
				houseKeepingBatchService.archiveAuditLog();
			}else if (BATCH_HR.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_HR.");
				hrBatchService.runBatchJob();
			}else if (BATCH_DBD.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_DBD.");
				dbdBatchService.paymentDBDSummary(EcerDateUtils.parseDateEN(form.getEndofdate()));
			}else if (BATCH_ONDEMAND.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_ONDEMAND.");
				paymentOndemandService.paymentOnDemandSummary(EcerDateUtils.parseDateEN(form.getEndofdate()));
			}else if (BATCH_GL.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_GL.");
			}
			logger.info("rerun batch success.");
		} catch (Exception e) {
			logger.error("rerun batch error",e);
		}
		
	}


}
