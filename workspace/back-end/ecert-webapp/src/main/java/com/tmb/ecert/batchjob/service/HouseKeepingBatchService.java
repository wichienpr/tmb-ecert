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
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.dao.AuditLogDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestStatusDao;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.utils.ArchiveFileUtil;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name="job.housekeeping.archive.cornexpression" , havingValue="" ,matchIfMissing=false)
public class HouseKeepingBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HOUSEKEEPING);

	@Autowired
	private AuditLogDao auditLogDao;
		
	@Autowired
	private ArchiveFileUtil archiveFileUtil;
		
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private CheckRequestStatusDao checkRequestStatusDao;

	/**
	 * 
	 */
	public void archiveAuditLog() {
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		log.info("HouseKeepingBatchService is starting process...");
		boolean isSuccess = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss" , Locale.ENGLISH);
		String pathFile = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_PATH);
		String fileName = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_FILENAME);
		String tarFile = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_TARFILE);
		int days =  Integer.parseInt(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_DAYS));
		
		try {
			
			//############################ WRITE AND ARCHIVE FILE AUDIT LOG HOUSE KEEPING SUMMARY BEGIN #################################
			List<AuditLog> auditLogs = auditLogDao.findAuditLogWithDays(days);
			if(auditLogs!=null && auditLogs.size()>0){
				Date currentDate = new Date();
				
				//Archive Audit Log file 
				String fileFullName = pathFile + File.separator + fileName.replace("$format", dateFormat.format(currentDate));
				
				isSuccess = writeFileWithEncoding(auditLogs,fileFullName,"UTF8");
				
				if(isSuccess){
					
					String tarFullName = pathFile + File.separator + tarFile.replace("$format", dateFormat.format(currentDate));
					
					//Archive Audit Log file to Tar file
					isSuccess = archiveFileUtil.archiveFile(fileFullName, tarFullName);
					
					if(isSuccess){	
						//Delete audit log
						auditLogDao.deleteAuditlog(days);
					}else{
						log.error("HouseKeepingBatchService archived file failed.");
						jobMonitoring.setErrorDesc("HouseKeepingBatchService archived file failed.");
					}
				}else {
					jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_CONVERT);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			//############################ WRITE AND ARCHIVE FILE AUDIT LOG HOUSE KEEPING SUMMARY BEGIN #################################
			
			//ARCHIVING REQUEST FORM
			checkRequestStatusDao.deleteRequestFormByDeleteFlag(days);

		} catch (Exception ex) {
			log.error("HouseKeepingBatchService Error = ",ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			// Remove old file
			try {
				removeFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			log.info("HouseKeepingBatchService is working Time(ms) = " + (end - start));
			
			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.HOUSEKEEPING_TYPE);
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
		log.info("HouseKeepingBatchService end process...");
	}
	
	/**
	 * @throws IOException
	 */
	public void removeFile() throws IOException {
		String path = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_PATH);
		Integer afterDay =  Integer.valueOf(ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_AFTERDAY));
		String removeFileIndex = ApplicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_RMFILE);
		int after = (afterDay == null) ? -7 : afterDay.intValue() * -1;
		File actual = new File(path);
		if(actual!=null && actual.listFiles() !=null && actual.listFiles().length>0) {
			for (File f : actual.listFiles()) {
				String fileName = f.getName();
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
				
				if (StringUtils.contains(removeFileIndex, extension)) {
					
					BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()),
							BasicFileAttributes.class);
					FileTime fileCreated = attr.creationTime();
					Date fileDate = new Date(fileCreated.toMillis());

					Calendar fileDateCalendar = Calendar.getInstance();
					fileDateCalendar.setTime(fileDate);
					fileDateCalendar.set(Calendar.MINUTE, 0);
					fileDateCalendar.set(Calendar.SECOND, 0);
					fileDateCalendar.set(Calendar.MILLISECOND, 0);

					Calendar afterDate = Calendar.getInstance();
					afterDate.add(Calendar.DATE, after);
					afterDate.set(Calendar.MINUTE, 0);
					afterDate.set(Calendar.SECOND, 0);
					afterDate.set(Calendar.MILLISECOND, 0);

					boolean isRemove = afterDate.after(fileDateCalendar) || afterDate.equals(fileDateCalendar);

					if (isRemove) {
						FileUtils.forceDelete(f);
						log.info("HouseKeepingBatchService removed old file success.");
					}
				}
			}
		}
		
	}
	
	/**
	 * @param auditLogs
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
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
			log.error("HouseKeepingBatchService export text file error = ",ex);
			result = false;
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				result = false;
				log.error("HouseKeepingBatchService error =  ",e);
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
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",Locale.ENGLISH);
		return null == col ? "" : format.format(col);
	}
}
