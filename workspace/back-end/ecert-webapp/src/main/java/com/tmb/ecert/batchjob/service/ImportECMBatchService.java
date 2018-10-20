package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.dao.ImportECMBatchDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FileImportRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "job.ecmimport.document.cornexpression", havingValue = "", matchIfMissing = false)
public class ImportECMBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);
	
	@Value("${app.datasource.path.upload}")
	private String pathUploadfile;
	
	@Value("${app.datasource.ws.url}")
	private String WSROOT;
	
	@Value("${app.datasource.ws.importdoc}")
	private String WSIMPORTURL;

	@Value("${app.datasource.ws.checkstatus}")
	private String WSCHECKURL;

	@Value("${app.datasource.ftp.path}")
	private String ftpPath;

	@Value("${app.datasource.ftp.url}")
	private String ftpHost;

	@Value("${app.datasource.ftp.username}")
	private String ftpUsername;

	@Value("${app.datasource.ftp.password}")
	private String ftpPassword;

	@Autowired
	private ImportECMBatchDao importECMBatchDao;

	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	private static String PATH_UPLOAD = "tmb-requestor/";
	private static String SEGMENTCODE_1 = "20005";
	private static String SEGMENTCODE_2 = "20004";
	private static String SEGMENTCODE_3 = "20003";
	private static String SEGMENTCODE_4 = "20002";
	
	private static String CODE_SUCCESS = "0000";
	private static String CODE_PARTIAL_SUCCESS = "0001";
	private static String CODE_CHECK_SUCCESS = "0";
	

	/**
	 * 
	 */
	public void sendDocumentToECM() {
		boolean isSuccess = false;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();

		log.info("ImportECMBatchService is starting process...");
		
		try {
			//############################ SEND DOCUMENTS TO ECM BEGIN ##########################
			List<RequestForm> reqFormList = importECMBatchDao.getRequestFormWithEcmFlag();
			
			int timesleep = Integer.parseInt(ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP));
			int timeLoop = Integer.parseInt(ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME));
			Assert.isTrue(timesleep > 0, ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP + " is greater than 0");
			Assert.isTrue(timeLoop > 0, ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME + " is greater than 0");
			
			String channelid = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHANNELID);
			String docTyep = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_DOCTYPE);
			int countImport  = 0;
			int countCheckstatus = 0;
			int countftp = 0;
			boolean statusUpload = false;
			
			if (reqFormList != null && reqFormList.size() > 0) {
				log.info(" BATCH REQUEST FROM SIZE  "+reqFormList.size());
				
				for (RequestForm requestForm : reqFormList) {
					String[] arrFile = new String[3];
					arrFile[0] =   pathUploadfile + PATH_UPLOAD + requestForm.getRequestFormFile();
					arrFile[1]  =  pathUploadfile + PATH_UPLOAD + requestForm.getCertificateFile();
					arrFile[2]  =  pathUploadfile + PATH_UPLOAD + requestForm.getReceiptFile();
					countftp = 0;
					
					while (countftp < timeLoop) {
						
						List<SftpFileVo> files = new ArrayList<>();
						files.add(new SftpFileVo(new File(arrFile[0] ), ftpPath, requestForm.getCertificateFile()));
						files.add(new SftpFileVo(new File(arrFile[1] ), ftpPath, requestForm.getRequestFormFile()));
						files.add(new SftpFileVo(new File(arrFile[2] ), ftpPath, requestForm.getReceiptFile()));

						SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, ftpPassword);
						boolean ftpSuccess = SftpUtils.putFile(sftpVo);
						
						if(ftpSuccess) {
							String reqID ="";
							countImport = 0;
							countCheckstatus = 0;
							while(countImport < timeLoop) {
								
								Thread.sleep(timesleep);
								reqID = ramdomKey(channelid);
								ImportDocumentResponse impResp = callImportWS(requestForm,reqID,channelid,"",arrFile,docTyep);
								
								if (CODE_SUCCESS.equals(impResp.getResCode())) {
									log.info(" WS IMPORT SUCCESS ");
									statusUpload = true;
									break;
									
								}else {
									statusUpload = false;
									countImport++;
									log.info("CALL WS IMPORT FIAL {} ", impResp.getDescription());
								}
							}
							if (statusUpload == true) {
								while (countCheckstatus < timeLoop) {
									
									Thread.sleep(timesleep);
									CheckStatusDocumentResponse checkStatusVo  = CheckStatusWS(requestForm,reqID,channelid,"",docTyep);
									
									if (CODE_CHECK_SUCCESS.equals(checkStatusVo.getStatusCode())) {
										log.info(" WS CHECK STATUS SUCCESS ");
										statusUpload = true;
										break ;
										
									}else if (CODE_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
										statusUpload = false;
										countCheckstatus++;
									}else {
										statusUpload = false;
										countCheckstatus++;
									}
									log.info(" WS CHECK STATUS FAIL "+checkStatusVo.getStatusCode() +" Des" + checkStatusVo.getDescription() );
								}		
							}
							
				
						} else {
							statusUpload = false;
							break;
						}
						countftp++;	
					}
					if (statusUpload) {
						int upldateResult = checkReqDetailDao.updateECMFlag(requestForm.getReqFormId());
						log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS!! ");
					} else {
						log.info(" END PROCESS UPLOAD CERTIFIACTE FAILED!! ");
					}
				}
			}
			//############################ SEND DOCUMENTS TO ECM END #############################

		} catch (Exception ex) {
			log.error("ImportECMBatchService Error = ", ex);
			jobMonitoring.setErrorDesc(ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			log.info("ImportECMBatchService is working Time(ms) = " + (end - start));

			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.IMPORT_ECM);
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
		log.info("ImportECMBatchService end process...");
	}
	

	private ImportDocumentResponse  callImportWS(RequestForm reqVo,String reqID, String channelid, String userid, String[] arrFile ,String docTyep) {
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_IMPORT_DOCUMENT);
		RestTemplate restTemplate = new RestTemplate();
		ImportDocumentRequest req = new ImportDocumentRequest();
//		List<FileImportRequest> files  = new ArrayList<>();
		req.setReqId(reqID);
		req.setCaNumber(reqVo.getCaNumber());
		req.setChannelId(channelid);
		req.setReqUserId(userid);
		req.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));
		
		List<FileImportRequest> fileslist = new ArrayList<>();
		FileImportRequest fileImport ;
		for (int j = 0; j < arrFile.length; j++) {
			fileImport = new FileImportRequest();
			String fileName = reqID + "/" + arrFile[j];
			
			fileImport.setFileName(fileName);
			fileImport.setCusName(reqVo.getCompanyName());
			fileImport.setDocTypeCode(docTyep);
			fileImport.setImportDate(EcerDateUtils.formatDDMMYYYYDate(new Date()));
			fileImport.setRegistrationId(reqVo.getOrganizeId());
			
			fileslist.add(fileImport);
		}
		req.setFiles(fileslist);

		HttpEntity<ImportDocumentRequest> request = new HttpEntity<>(req);
		ResponseEntity<ImportDocumentResponse> response = restTemplate.exchange(endPoint,
				HttpMethod.POST, request, ImportDocumentResponse.class);

		ImportDocumentResponse resVo = response.getBody();
		return resVo;
	}
	
	private CheckStatusDocumentResponse CheckStatusWS(RequestForm reqVo,String reqID, String channelid, String userid,String docTyep) {
		
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_IMPORT_DOCUMENT);
		RestTemplate restTemplate = new RestTemplate();
		CheckStatusDocumentRequest checkReq = new CheckStatusDocumentRequest();
		checkReq.setReqId(reqID);
		checkReq.setChannelId(channelid);
		checkReq.setReqUserId(userid);
		checkReq.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));
		checkReq.setCaNumber(reqVo.getCaNumber());

		HttpEntity<CheckStatusDocumentRequest> chekcRequest = new HttpEntity<>(checkReq);
		ResponseEntity<CheckStatusDocumentResponse> checkResponse = restTemplate.exchange(
				endPoint, HttpMethod.POST, chekcRequest,
				CheckStatusDocumentResponse.class);
		CheckStatusDocumentResponse checkStatusVo = checkResponse.getBody();
		return checkStatusVo;
	}
	
	private String convertCostomerSegment(String segmentcode) {
		if (SEGMENTCODE_1.equals(segmentcode)) {
			return "1";
		}else if (SEGMENTCODE_2.equals(segmentcode)) {
			return "2";
		}else if (SEGMENTCODE_3.equals(segmentcode)) {
			return "3";
		}else if (SEGMENTCODE_4.equals(segmentcode)) {
			return "4";
		}else {
			return "0";
		}
	}

	private String ramdomKey(String chanel) {
		int random = new Random().nextInt(999999);
		String str = chanel + EcerDateUtils.formatYYMMDDDate(new Date())
				+ StringUtils.leftPad(Integer.toString(random), 6, "0");
		return str;

	}
}
