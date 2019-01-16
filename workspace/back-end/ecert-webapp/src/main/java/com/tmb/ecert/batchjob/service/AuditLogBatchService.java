package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.dao.AuditLogDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name="job.auditlog.cornexpression" , havingValue="" ,matchIfMissing=false)
public class AuditLogBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUDITLOG);

	@Autowired
	private AuditLogDao auditLogDao;
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;

	public void transferAuditLogByActionCode(String actionCode,Date runDate) {
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		log.info("AuditLogBatchService is starting process...");	
		String localPath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_LOCALPATH);
		String fileName = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FILENAME);
		String ftpPath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPPATH);
		String ftpHost = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPHOST);
		String ftpUsername = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPUSERNAME);
		String ftpPassword = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPPWD);

		try {
			//############################ WRITE FILE AND FTP AUDIT LOG SUMMARY BEGIN #################################
			List<AuditLog> auditLogs = auditLogDao.findAuditLogByActionCode(actionCode,runDate);
			if(auditLogs!=null && auditLogs.size()>0){
				SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy_HHmmss" , Locale.US);
				fileName = String.format(fileName,  df.format(new Date()));
				String fileNameFull = localPath + File.separator + fileName;							

				// Step 1. Write File include file ISA format
				isSuccess = writeFileWithEncoding(auditLogs,fileNameFull,"UTF8");
				
				// Step 2. SFTP File and save log fail or success !!
				if(isSuccess){
					List<SftpFileVo> files = new ArrayList<>();
					files.add(new SftpFileVo(new File(fileNameFull), ftpPath, fileName));
					SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, TmbAesUtil.decrypt(keystorePath, ftpPassword));
				    isSuccess = SftpUtils.putFile(sftpVo);
					if(!isSuccess){
						log.error("AuditLogBatchService FTP Error: {} ",sftpVo.getErrorMessage());
						emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP, jobMonitoring.getErrorDesc() );
						jobMonitoring.setErrorDesc(sftpVo.getErrorMessage());
					}
				}else {
					jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_CONVERT);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			//############################ WRITE FILE AND FTP AUDIT LOG SUMMARY END #################################
		} catch (Exception ex) {
			log.error("AuditLogBatchService Error = ",ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			log.info("AuditLogBatchService is working Time(ms) = " + (end - start));
			
			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.AUDITLOG_TYPE);
			jobMonitoring.setEndOfDate(current);
			jobMonitoring.setStopDate(new Date());
			jobMonitoring.setStatus(isSuccess ? JOBMONITORING.SUCCESS : JOBMONITORING.FAILED);
			jobMonitoring.setRerunNumber(0);
			jobMonitoring.setRerunById(CHANNEL.BATCH);
			jobMonitoring.setRerunByName(CHANNEL.BATCH);
			jobMonitoring.setRerunDatetime(new Date());
			jobMonitoringDao.insertEcertJobMonitoring(jobMonitoring);
			//############################ INSERT JOBMONITORING BATCH END ###########################################
		}
	}
	
	private boolean writeFileWithEncoding(List<AuditLog> auditLogs, String fileName,String encoding) throws IOException {
		OutputStreamWriter writer = null;
		boolean result = true;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
				file.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(file, true),encoding);
			for (AuditLog info : auditLogs) {
				StringJoiner sj = new StringJoiner("|");
				sj.add(getData(info.getAuditLogId()));
				sj.add(getDataTimestamp(info.getCreateDatetime()));
				sj.add(getData(info.getActionCode()));
				sj.add(getData(info.getDescription()));
				sj.add(getData(info.getCreateById()));
				sj.add(getData(info.getCreatedByName()));
				writer.append(sj.toString()+"\r\n");
			}
		}catch (Exception ex){
			log.error("AuditLogBatchService export text file error = ",ex);
			result = false;
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				result = false;
				log.error("AuditLogBatchService error =  ",e);
			}
		}
		return result;
	}
	
	/**
	 * @param col
	 * @return
	 */
	private String getData(Object col) {
		return null == col ? "" : String.valueOf(col);
	}
	
	/**
	 * @param col
	 * @return
	 */
	private String getDataTimestamp(Date col) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
		return null == col ? "" : format.format(col);
	}
}
