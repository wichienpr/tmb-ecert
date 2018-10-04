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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.dao.AuditLogDao;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.common.constant.ProjectConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.PARAMETER_CONFIG;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import com.tmb.ecert.common.utils.ArchiveFileUtil;

@Service
@ConditionalOnProperty(name="job.housekeeping.archive.cornexpression" , havingValue="" ,matchIfMissing=false)
public class HouseKeepingBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HOUSEKEEPING);

	@Autowired
	private AuditLogDao auditLogDao;
	
	@Autowired
	private ApplicationCache applicationCache;
	
	@Autowired
	private ArchiveFileUtil archiveFileUtil;

	/**
	 * 
	 */
	public void archiveAuditLog() {
		long start = System.currentTimeMillis();
		log.info("HouseKeepingBatchService is starting process...");
		boolean isLogFail = false;
		String errorMesage = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss" , Locale.ENGLISH);
		String pathFile = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_PATH);
		String fileName = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_FILENAME);
		String tarFile = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_TARFILE);
		int days =  Integer.parseInt(applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_DAYS));
		
		try {
			List<AuditLog> auditLogs = auditLogDao.findAuditLogWithDays(days);
			if(auditLogs!=null && auditLogs.size()>0){
				Date currentDate = new Date();
				
				//Archive Audit Log file 
				String fileFullName = pathFile + File.separator + fileName.replace("$format", dateFormat.format(currentDate));
				
				boolean result = writeFileWithEncoding(auditLogs,fileFullName,"UTF8");
				
				if(result){
					
					String tarFullName = pathFile + File.separator + tarFile.replace("$format", dateFormat.format(currentDate));
					
					//Archive Audit Log file to Tar file
					result = archiveFileUtil.archiveFile(fileFullName, tarFullName);
					
					if(result){
					
						log.info("HouseKeepingBatchService archived file at = "+tarFullName);
						
						//Delete audit log
						auditLogDao.deleteAuditlog(days);
						
					}else{
						log.info("HouseKeepingBatchService archived file failed.");
					}
				}
			}
			
		} catch (Exception ex) {
			log.error("HouseKeepingBatchService Error = ",ex);
		} finally {
			
			// Remove old file
			try {
				removeFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			log.info("HouseKeepingBatchService is working Time(ms) = " + (end - start));
		}
		log.info("HouseKeepingBatchService end process...");
	}
	
	/**
	 * @throws IOException
	 */
	public void removeFile() throws IOException {
		String path = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_PATH);
		Integer afterDay =  Integer.valueOf(applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_AFTERDAY));
		String removeFileIndex = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_RMFILE);
		int after = (afterDay == null) ? -7 : afterDay.intValue() * -1;
		File actual = new File(path);
		
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
				log.info("HouseKeepingBatchService export text file at "+fileName);
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
