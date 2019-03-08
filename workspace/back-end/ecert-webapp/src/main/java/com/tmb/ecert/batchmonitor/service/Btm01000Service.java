package com.tmb.ecert.batchmonitor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.service.AuditLogBatchService;
import com.tmb.ecert.batchjob.service.EcmMasterDataBatchService;
import com.tmb.ecert.batchjob.service.HROfficeCodeBatchService;
import com.tmb.ecert.batchjob.service.HouseKeepingBatchService;
import com.tmb.ecert.batchjob.service.ImportECMBatchService;
import com.tmb.ecert.batchjob.service.ImportReceiptToECMBatchService;
import com.tmb.ecert.batchjob.service.PaymentDBDSummaryBatchService;
import com.tmb.ecert.batchjob.service.PaymentGLSummaryBatchService;
import com.tmb.ecert.batchjob.service.PaymentOnDemandSummaryBatchService;
import com.tmb.ecert.batchmonitor.persistence.dao.BatchMonitoringDao;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000FormVo;
import com.tmb.ecert.batchmonitor.persistence.vo.Btm01000Vo;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.StatusConstant;
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
	
	@Autowired
	private ImportECMBatchService importEcmService ;
	
	@Autowired
	private EcmMasterDataBatchService ecmMasterService ;
	
	@Autowired
	private ImportReceiptToECMBatchService importReceiptToECMBatchService;
	
	public DataTableResponse<Btm01000Vo> getListBatch(Btm01000FormVo form) {
		DataTableResponse<Btm01000Vo> list  = new DataTableResponse<>();
		List<Btm01000Vo> adl01000VoList = batchDao.getListBatch(form);
		list.setData(adl01000VoList);
		int count = batchDao.conutListBatch(form);
		list.setRecordsTotal(count);

		return list;

	}
	
	@Async
	public void  rerunJob(Btm01000Vo form,String fullName,String userid) {
		try {

			if(StatusConstant.JOBMONITORING.BATCH_AUDILOG.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_AUDILOG.");
				auditlogBatchService.transferAuditLogByActionCode(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_ACTIONCODE),
						EcerDateUtils.parseDateEN(form.getStartDate()));
			}else if (StatusConstant.JOBMONITORING.BATCH_HOUSEKEEP.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_HOUSEKEEP.");
				houseKeepingBatchService.archiveAuditLog();
			}else if (StatusConstant.JOBMONITORING.BATCH_HR.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_HR.");
				hrBatchService.runBatchJob();
			}else if (StatusConstant.JOBMONITORING.BATCH_DBD.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_DBD.");
				dbdBatchService.paymentDBDSummary(EcerDateUtils.parseDateEN(form.getStartDate()));
			}else if (StatusConstant.JOBMONITORING.BATCH_ONDEMAND.equals(form.getJobtypeCode())) {
				logger.info("rerun BATCH_ONDEMAND.");
				paymentOndemandService.paymentOnDemandSummary(EcerDateUtils.parseDateEN(form.getStartDate()));
			}else if (StatusConstant.JOBMONITORING.BATCH_GL.equals(form.getJobtypeCode())) {
				paymentGLService.reRunBatchJob(EcerDateUtils.parseDateEN(form.getStartDate()),EcerDateUtils.parseDateEN(form.getStopDate()));
				logger.info("rerun BATCH_GL.");
			}else if (StatusConstant.JOBMONITORING.BATCH_ECM.equals(form.getJobtypeCode())) {
				importEcmService.sendDocumentToECM();
				logger.info("rerun BATCH_IMPORT TO ECM.");
			}else if (StatusConstant.JOBMONITORING.BATCH_ECM_MASTER.equals(form.getJobtypeCode())) {
				ecmMasterService.runBatchJob();
				logger.info("rerun BATCH_ECM MASTER.");
			}else if (StatusConstant.JOBMONITORING.BATCH_ECM_RECEIPT.equals(form.getJobtypeCode())) {
				importReceiptToECMBatchService.sendReceiptToECM();
				logger.info("rerun BATCH IMPORT RECEIPT TO ECM.");
			}
			batchDao.updateRerunJobById(form, fullName, userid);
			logger.info("rerun batch success.");
		} catch (Exception e) {
			logger.error("rerun batch error",e);
		}
		
	}

}
