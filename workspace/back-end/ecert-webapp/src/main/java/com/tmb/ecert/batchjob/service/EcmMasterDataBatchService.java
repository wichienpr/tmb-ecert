package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.schemas.office.visio.x2012.main.MastersType;
import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.ECM_MASTER;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.HROFFICE_CODE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOB_TYPE;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.batchjob.dao.EcmMasterDataDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.batchjob.domain.EcmMasterData;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.ProjectConstant.ENCODING;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class EcmMasterDataBatchService {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_ECMMASTERDATA);
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private EcmMasterDataDao ecmMasterDataDao;
	
	private String DATE_FILE_NAME = "yyyyMMddHHmmss";
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;
	
	public void runBatchJob() {
		Date runDate = new Date();
		log.info("ECM MASTER DATA BATCH JOB START...");
		String errorDesc = "";
		
		try {
			String path = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_PATH);
			String host = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_IP);
			String username = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_USERNAME);
			String password = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_PASSWORD);
			String fileName = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_FILENAME);
			String fileType = ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_FILE_TYPE);
			
			String achiveFilePath = String.format("%s%s%s", ApplicationCache.getParamValueByName(ECM_MASTER.BATCH_ECM_V2_ARCHIVE_FILE_PATH), 
					DateFormatUtils.format(runDate, DATE_FILE_NAME), fileType);
			
			SftpVo sftpVo = new SftpVo(new SftpFileVo(path, fileName), host, username,TmbAesUtil.decrypt(keystorePath, password),achiveFilePath);
			File file = SftpUtils.getFile(sftpVo);

			if (file != null && file.length() > 0) {
				List<EcmMasterData> listMaster = readFile(file);
				
				ecmMasterDataDao.deleteECMMasterData();
				ecmMasterDataDao.insertECMMasterData(listMaster);
				log.info("ECM Masterdata Batch save success total : {} records", listMaster.size());
			}else {
				errorDesc = sftpVo.getErrorMessage();
			}

		} catch (Exception e) {
			errorDesc = e.getMessage();
			e.printStackTrace();
		} finally {
			if (StringUtils.isBlank(errorDesc)) {
				this.saveEcertJobMonitoring(runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.SUCCESS, "");
			} else {
				this.saveEcertJobMonitoring(runDate, BatchJobConstant.RERUN_DEFAULT, JOBMONITORING.FAILED, errorDesc);
			}
		}
	}
	
	public List<EcmMasterData> readFile(File file) throws IOException{
		List<String> contents = FileUtils.readLines(file, ENCODING.TIS_620);
//		log.info("line count " +contents.size() );
		
		List<EcmMasterData> listEcmMaster = new ArrayList<>();
		
//		for (String content : contents) {
		for (int i = 1; i < contents.size()-1; i++) {
			String content = contents.get(i);
			EcmMasterData master = new EcmMasterData();
			
			master.setRecIndicator(getValue(content, 0, 1));
			master.setSource(getValue(content, 1, 11));
			master.setChannel(getValue(content, 11, 13));
			master.setSegment(getValue(content, 13, 16));
			master.setTypeCode(getValue(content, 16, 26));
			master.setTypeNameTh(getValue(content, 26, 526));
			master.setTypeNameEn(getValue(content, 526, 1026));
			master.setTypeShortName(getValue(content, 1026, 1226));
			master.setDocLocation(getValue(content, 1226, 1998));
			master.setDomainFolder(getValue(content, 1998, 2253));
			master.setDimensionFolder(getValue(content, 2253, 2508));
			master.setFunctionFolder(getValue(content, 2508, 2763));
			master.setArchivalPeriod(getValueInt(content, 2763, 2768));
			master.setDisposalPeriod(getValueInt(content, 2768, 2773));
			master.setExpiryPeriod(getValueInt(content, 2773, 2778));
			master.setRepository(getValue(content, 2778, 2798));
			master.setDocTemplate(getValue(content, 2798, 2898));
			listEcmMaster.add(master);
//			log.info("line sprit to object " +master.getRecIndicator()+":"+master.getSource()+":"+master.getChannel()+":"+master.getSegment()+":"
//			+master.getTypeCode()+":"+master.getTypeNameTh()+":"+master.getTypeNameEn()+":"+master.getTypeShortName()+":"+master.getDocLocation()+":"
//			+master.getDomainFolder()+":"+master.getDimensionFolder()+":"+master.getFunctionFolder()+":"+master.getArchivalPeriod()+":"+master.getDisposalPeriod()+":"
//			+master.getExpiryPeriod()+":"+master.getRepository()+":"+master.getDocTemplate());
		}
		return listEcmMaster;
	}
	
	private String getValue(String content, int start, int end) {
		return StringUtils.trim(StringUtils.substring(content, start, end));
	}
	
	private Integer getValueInt(String content, int start, int end) {
		String str = StringUtils.trim(StringUtils.substring(content, start, end));
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}else {
			return null;
		}
	}
	
	private Long saveEcertJobMonitoring(Date runDate, Integer rerunNumber, String status, String errorDesc) {
		EcertJobMonitoring ecertJobMonitoring = new EcertJobMonitoring();
		Date nowDate = new Date();
		ecertJobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.ECM_MASTERDATA);
		ecertJobMonitoring.setStartDate(runDate);
		ecertJobMonitoring.setStopDate(nowDate);
		ecertJobMonitoring.setEndOfDate(runDate);
		ecertJobMonitoring.setStatus(status);
		ecertJobMonitoring.setErrorDesc(errorDesc);
		ecertJobMonitoring.setRerunNumber(rerunNumber);
		ecertJobMonitoring.setRerunById(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunByName(CHANNEL.BATCH);
		ecertJobMonitoring.setRerunDatetime(nowDate);
		return jobMonitoringDao.insertEcertJobMonitoring(ecertJobMonitoring);
	}

}
