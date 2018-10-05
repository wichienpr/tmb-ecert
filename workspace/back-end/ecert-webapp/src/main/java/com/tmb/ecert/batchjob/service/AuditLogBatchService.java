package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.dao.AuditLogDao;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.PARAMETER_CONFIG;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

@Service
@ConditionalOnProperty(name="job.auditlog.cornexpression" , havingValue="" ,matchIfMissing=false)
public class AuditLogBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUDITLOG);

	@Autowired
	private AuditLogDao auditLogDao;

	public void transferAuditLogByActionCode(String actionCode) {
		long start = System.currentTimeMillis();
		log.info("AuditLogBatchService is starting process...");	
		String localPath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_LOCALPATH);
		String fileName = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FILENAME);
		String ftpPath = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPPATH);
		String ftpHost = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPHOST);
		String ftpUsername = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPUSERNAME);
		String ftpPassword = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_AUDITLOG_FTPPWD);

		try {
			List<AuditLog> auditLogs = auditLogDao.findAuditLogByActionCode(actionCode);
			if(auditLogs!=null && auditLogs.size()>0){
				SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy_HHmmss" , Locale.US);
				fileName = fileName.replace("{0}", df.format(new Date()));
				String fileNameFull = localPath + File.separator + fileName;							

				// Step 1. Write File include file ISA format
				boolean result = writeFileWithEncoding(auditLogs,fileNameFull,"UTF8");
				
				// Step 2. SFTP File and save log fail or success !!
				if(result){
					List<SftpFileVo> files = new ArrayList<>();
					files.add(new SftpFileVo(new File(fileNameFull), ftpPath, fileName));
					SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, ftpPassword);
				    boolean isSuccess = SftpUtils.putFile(sftpVo);
					if(!isSuccess){
						log.error("AuditLogBatchService FTP Error: {} ",sftpVo.getErrorMessage());
					}
				}
			}
		} catch (Exception ex) {
			log.error("AuditLogBatchService Error = ",ex);
		} finally {
			long end = System.currentTimeMillis();
			log.info("AuditLogBatchService is working Time(ms) = " + (end - start));
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
				log.info("AuditLogBatchService export text file at "+fileName);
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
