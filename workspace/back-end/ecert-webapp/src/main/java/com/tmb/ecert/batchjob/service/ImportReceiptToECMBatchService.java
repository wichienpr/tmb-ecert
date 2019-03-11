package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.dao.ImportECMBatchDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.batchjob.domain.EcmMasterData;
import com.tmb.ecert.batchjob.domain.EcmReceipt;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUploadResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUuploadRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FileImportRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.checkrequeststatus.service.ReqidGenKeyService;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "job.ecmimport.receipt.cornexpression", havingValue = "", matchIfMissing = false)
public class ImportReceiptToECMBatchService {
	
	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);
	
	@Value("${app.datasource.path.upload}")
	private String pathUploadfile;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;
	
	
	@Autowired 
	private ImportECMBatchDao importECMBatchDao;
	
	@Autowired
	private ReqidGenKeyService reqidGenKeyService;
	
	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	@Autowired
	private ImportECMBatchService importECM;
	
	
	public void sendReceiptToECM() {
		String ecmConfig = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_CONFIGURARION);
		List<EcmReceipt> listReqRec = importECMBatchDao.findRequestReceipt();
		
		if(StatusConstant.ECM_CONFIGURATION.NEW_VERSION.equals(ecmConfig)) {
			this.sendDocumentToECM_New(listReqRec);
			
		}else {
			this.sendDocumentToECM_old(listReqRec);
			
		}
		
	}
	
	public void sendDocumentToECM_old(List<EcmReceipt> listReqRec) {
		log.info(" START PROCESS UPLOAD RECEIPT TO ECM BY BATCH JOB ");
		boolean isSuccess = true;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		String errorResp = "";
		
		int timesleep = Integer.parseInt(ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP));
		int timeLoop = Integer.parseInt(ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME));
		Assert.isTrue(timesleep > 0, ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP + " is greater than 0");
		Assert.isTrue(timeLoop > 0, ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME + " is greater than 0");
		String channelid = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHANNELID);
		int countImport  = 0;
		int countCheckstatus = 0;
		int countftp = 0;
		boolean statusUpload = false;
		String ftpPath = "";
		try {
			log.info(" BATCH REQUEST FROM SIZE  "+listReqRec.size());
			if (listReqRec != null && listReqRec.size() > 0) {

				for (EcmReceipt reqReceiptVo : listReqRec) {
					CheckStatusDocumentResponse checkStatusVo  = null;
					countftp = 0;
					while (countftp < timeLoop) {
						String reqID = ramdomKey(channelid);
						boolean ftpSuccess = putftpFile(reqReceiptVo,reqID);
						if (ftpSuccess) {
							Thread.sleep(timesleep);
							ImportDocumentResponse impResp = importDocumentWebService(reqID,reqReceiptVo,reqReceiptVo.getMakerById());
				
							if (StatusConstant.IMPORT_ECM_WS.IMPORT_STATUS_SUCCESS.equals(impResp.getResCode())) {
								statusUpload = true;
								break;
								
							}else {
								statusUpload = false;
								countImport++;
							}
							
							if (statusUpload == true) {
								while (countCheckstatus < timeLoop) {
									Thread.sleep(timesleep);
									checkStatusVo  = CheckStatusWS(reqReceiptVo,reqID,channelid,reqReceiptVo.getMakerById());
									
									if (StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(checkStatusVo.getStatusCode())) {
										statusUpload = true;
										break ;
									}else {
										statusUpload = false;
									}
									countCheckstatus++;
								}		
							}
							if(statusUpload) {
								break;
							}
						}else {
//							System.out.println("ftp fail");
						}
						countftp++;	
						if (statusUpload) {
							isSuccess = isSuccess && statusUpload;
							int updateStatus = checkReqDetailDao.updateReceiptECMFlag(reqReceiptVo.getReceiptId(),StatusConstant.ECM_CONFIGURATION.ECM_SUCCESS);
							log.info(" RECEIPT ID "+Long.toString(reqReceiptVo.getReceiptId())+" UPLOAD CERTIFIACTE SUCCESS "+ Integer.toString(updateStatus));
						} else {
							isSuccess = false;
//							checkReqDetailDao.updateECMFlag(reqReceiptVo.getReceiptId(),StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
//							errorResp = errorResp +" REQ ID "+reqReceiptVo.getReqId().toString()+" upload file fail \n";
//							log.info(" END PROCESS UPLOAD CERTIFIACTE FAILED!! ");
						}
					}
					
					if (!isSuccess) {
						errorResp = errorResp +" REQ ID "+reqReceiptVo.getReqId().toString()+" upload file fail. ";
					}
				}
				
				if (!isSuccess) {
					jobMonitoring.setErrorDesc(errorResp);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			
			
		} catch (Exception e) {
			log.error("ImportECMBatchService Error = ", e);
		}finally {
			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.IMPORT_RECEIPT);
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
	
	public void sendDocumentToECM_New(List<EcmReceipt> listReqRec) {
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		String errorResp = "";
		boolean isSuccess = false;
		try {
//			List<RequestForm> reqFormList = importECMBatchDao.getRequestFormWithEcmFlag();
			log.info(" START PROCESS UPLOAD RECEIPT TO ECM BY BATCH JOB ");
			if (listReqRec != null && listReqRec.size() > 0) {
				log.info(" BATCH REQUEST FROM SIZE  "+listReqRec.size());
				
				for (EcmReceipt ecmReceipt : listReqRec) {

					ECMUuploadRequest requestCMIS = createRequest(ecmReceipt, ecmReceipt.getMakerById());
					boolean statusWS = importCMISImport(requestCMIS);
					
					if (statusWS) {
						isSuccess = isSuccess && statusWS;
						int updateStatus = checkReqDetailDao.updateReceiptECMFlag(ecmReceipt.getReceiptId(),StatusConstant.ECM_CONFIGURATION.ECM_SUCCESS);
						log.info(" RECEIPT ID "+Long.toString(ecmReceipt.getReceiptId())+" UPLOAD CERTIFIACTE SUCCESS "+ Integer.toString(updateStatus));
					}else {
						isSuccess = isSuccess && statusWS;
//						checkReqDetailDao.updateECMFlag(ecmReceipt.getReceiptId(),StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
						errorResp = errorResp +" REQ ID "+ecmReceipt.getReqId().toString()+" upload file fail.";
					}
				}
				
				if (!isSuccess) {
					jobMonitoring.setErrorDesc(errorResp);
				}
				
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}

		} catch (Exception ex) {
			log.error("ImportECMBatchService Error = ", ex);
//			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP , "FTP FILE IMPORT DOCUMENT FAIL");
//			jobMonitoring.setErrorDesc(ex.getMessage());
		}finally {
			long end = System.currentTimeMillis();
			log.info("ImportECMBatchService is working Time(ms) = " + (end - start));

			//############################ INSERT JOBMONITORING BATCH BEGIN #########################################
			jobMonitoring.setStartDate(current);
			jobMonitoring.setJobTypeCode(JOBMONITORING_TYPE.IMPORT_RECEIPT);
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
	
	public boolean putftpFile(EcmReceipt reqReceiptVo,String reqId) {
		String pathFile = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PATH); 
		String ftpHost= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_HOST);
		String ftpUsername= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_USERNAME);
		String ftpPassword= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PASSWORD);
		
		List<SftpFileVo> files = new ArrayList<>();
		String ftpPath = pathFile+ "/"+ reqId;
		try {
			files.add(new SftpFileVo(new File(pathUploadfile  +"/" + reqReceiptVo.getFileName()), ftpPath, reqReceiptVo.getFileName()));
			SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername,TmbAesUtil.decrypt(keystorePath, ftpPassword));
			return  SftpUtils.putFile(sftpVo,ftpPath);
			
		} catch (Exception e) {
			log.error("ImportECMBatchService Error = ", e);
			return false;
		}
	}
	
	public ImportDocumentResponse importDocumentWebService(String reqID , EcmReceipt recpVo ,String userId) {
		String docTypeRec =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_RECP);
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_IMPORT_DOCUMENT);
		String channelid = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHANNELID);
		
		RestTemplate restTemplate = new RestTemplate();
		ImportDocumentRequest req = new ImportDocumentRequest();
		List<FileImportRequest> files  = new ArrayList<>();
		req.setReqId(reqID);
		req.setCaNumber(recpVo.getCaNumber());
		req.setChannelId(channelid);
		req.setReqUserId(userId);
		req.setSegmentCode("");
		
		FileImportRequest fileImport = new FileImportRequest();
		fileImport.setCusName(recpVo.getCompanyName());
		fileImport.setDocTypeCode(docTypeRec);
		fileImport.setFileName(recpVo.getFileName());
		fileImport.setImportDate(EcerDateUtils.formatDDMMYYYYDate(new Date()));
		fileImport.setRefAppNo(recpVo.getTmbRequestNo());
		fileImport.setRegistrationId(recpVo.getOrganizeId());
		files.add(fileImport);
		
		req.setFiles(files);
		
		HttpEntity<ImportDocumentRequest> request = new HttpEntity<>(req);
		ResponseEntity<ImportDocumentResponse> response = restTemplate.exchange(endPoint,
				HttpMethod.POST, request, ImportDocumentResponse.class);

		ImportDocumentResponse resVo = response.getBody();
		
		return resVo;
		
	}
	
	private CheckStatusDocumentResponse CheckStatusWS( EcmReceipt recpVo,String reqID, String channelid, String userid) {
		
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHECK_STATUS);
		RestTemplate restTemplate = new RestTemplate();
		CheckStatusDocumentRequest checkReq = new CheckStatusDocumentRequest();
		checkReq.setReqId(reqID);
		checkReq.setChannelId(channelid);
		checkReq.setReqUserId(userid);
		checkReq.setSegmentCode("");
		checkReq.setCaNumber(recpVo.getCaNumber());

		HttpEntity<CheckStatusDocumentRequest> chekcRequest = new HttpEntity<>(checkReq);
		ResponseEntity<CheckStatusDocumentResponse> checkResponse = restTemplate.exchange(
				endPoint, HttpMethod.POST, chekcRequest,
				CheckStatusDocumentResponse.class);
		CheckStatusDocumentResponse checkStatusVo = checkResponse.getBody();
		return checkStatusVo;
	}
	
	private String ramdomKey(String chanel) {
		String str = chanel + reqidGenKeyService.getNextKey();
		return str;

	}
	
	private String convertCostomerSegment(String segmentcode) {
		if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_1.equals(segmentcode)) {
			return "1";
		}else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_2.equals(segmentcode)) {
			return "2";
		}else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_3.equals(segmentcode)) {
			return "3";
		}else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_4.equals(segmentcode)) {
			return "4";
		}else {
			return "0";
		}
	}
	
	public ECMUuploadRequest createRequest(EcmReceipt recpVo,String userid) {

		String tmbIdType = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_TMB_IDTYPE);
		String repositoryId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_REPOSITORY);
		String objTypeId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_OBJ_ID);
		String tmbSource =  ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_TMB_SOURCE);
		String chanelId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_SUMMIT_CHANNEL);

		String docTypeRec =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_RECP);
		ECMUuploadRequest ecmUploadRequest = null;
		EcmMasterData ecmMaster = null;
		try {

			ecmUploadRequest = new ECMUuploadRequest();
			
			ecmUploadRequest.setRepositoryId(repositoryId);
			ecmUploadRequest.setObjectTypeId(objTypeId);
			//NAME : YYYYMMDDHHMM-DocTypeCode-ShortDocName-FileName.xxx
			ecmUploadRequest.setTmbSource(tmbSource);
			ecmUploadRequest.setTmbCreatorId(userid); //user
			ecmUploadRequest.setTmbIdentificationId(recpVo.getOrganizeId());
			ecmUploadRequest.setTmbIdentificationType(tmbIdType);
			ecmUploadRequest.setChannel(chanelId);
			ecmUploadRequest.setApplicationId(recpVo.getTmbRequestNo());
			ecmUploadRequest.setCustomerFirstNameThai(recpVo.getCompanyName());
			ecmUploadRequest.setCustomerFirstNameEng(recpVo.getCompanyName());
			
			
			ecmUploadRequest.setTmbDocTypeCode(docTypeRec);
			ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
			
			ecmUploadRequest.setName(this.convertFilenameForECM(recpVo.getFileName(), docTypeRec, ecmMaster.getTypeShortName()));
			byte[] bty = FileUtils.readFileToByteArray(new File(pathUploadfile +"/" +recpVo.getFileName()));
			ecmUploadRequest.setFile(bty);
			ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
			ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
			ecmUploadRequest.setCustomerFirstNameThai(ecmMaster.getTypeNameTh());
			ecmUploadRequest.setCustomerFirstNameEng(ecmMaster.getTypeNameEn());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecmUploadRequest;
	}
	
	public boolean importCMISImport(ECMUuploadRequest Req) throws URISyntaxException {
		ECMUploadResponse response = new ECMUploadResponse();
		RestTemplate restTemplate = new RestTemplate();
		URI fooResourceUrl = new URI( ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CREATE_DOC));
		boolean statusWS = false;

		HttpEntity<ECMUuploadRequest> request = new HttpEntity<>(Req);
		ResponseEntity<ECMUploadResponse> wsresponse = restTemplate.exchange(fooResourceUrl , HttpMethod.POST, request, ECMUploadResponse.class);
		response = wsresponse.getBody();
		if (!StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(response.getStatusCode())) {
			statusWS = false;
		}
		statusWS = true;
//		log.info("CALL WS CHECK STATUS {}", response.getDescription());

		return statusWS;
		
	}
	
	public String convertFilenameForECM(String fileName,String docTypeCode ,String shotName ) {
//		format file YYYYMMMDDHHMM-DoctypeCode-ShotDoctypeName-fileName.xxx
		
		return String.format("%s-%s-%s-%s", EcerDateUtils.formatYYMMDDDate(new Date()),docTypeCode,shotName,fileName);
		
	}
	


}
