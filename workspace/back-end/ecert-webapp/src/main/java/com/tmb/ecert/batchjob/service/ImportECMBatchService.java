package com.tmb.ecert.batchjob.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
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

import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.constant.BatchJobConstant.JOBMONITORING_TYPE;
import com.tmb.ecert.batchjob.dao.ImportECMBatchDao;
import com.tmb.ecert.batchjob.dao.JobMonitoringDao;
import com.tmb.ecert.batchjob.domain.EcertJobMonitoring;
import com.tmb.ecert.batchjob.domain.EcmMasterData;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUploadResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUuploadRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FileImportRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.IndexGuoupResponse;
import com.tmb.ecert.checkrequeststatus.service.ReqidGenKeyService;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.ProjectConstant.CHANNEL;
import com.tmb.ecert.common.constant.StatusConstant.JOBMONITORING;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "job.ecmimport.document.cornexpression", havingValue = "", matchIfMissing = false)
public class ImportECMBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_IMPORTECM);
	
	@Value("${app.datasource.path.upload}")
	private String pathUploadfile;
	
/*	@Value("${app.datasource.ws.url}")
	private String WSROOT;
	
	@Value("${app.datasource.ws.importdoc}")
	private String WSIMPORTURL;

	@Value("${app.datasource.ws.checkstatus}")
	private String WSCHECKURL;*/

	@Autowired
	private ImportECMBatchDao importECMBatchDao;

	@Autowired
	private JobMonitoringDao jobMonitoringDao;
	
	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ReqidGenKeyService reqidGenKeyService;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;
	
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
		String ecmConfig = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_CONFIGURARION);
		if(StatusConstant.ECM_CONFIGURATION.NEW_VERSION.equals(ecmConfig)) {
			this.sendDocumentToECM_New();
			
		}else {
			this.sendDocumentToECM_old();
			
		}
	}
	public void sendDocumentToECM_old() {
		boolean isSuccess = true;
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		String errorResp = "";
		
		String pathFile = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PATH); 
		String ftpHost= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_HOST);
		String ftpUsername= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_USERNAME);
		String ftpPassword= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PASSWORD);

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
			String ftpPath = "";
			
			if (reqFormList != null && reqFormList.size() > 0) {
				log.info(" BATCH REQUEST FROM SIZE  "+reqFormList.size());
				
				for (RequestForm requestForm : reqFormList) {

					countftp = 0;
					CheckStatusDocumentResponse checkStatusVo = null;
					String reqID ="";
					while (countftp < timeLoop) {
						reqID = ramdomKey(channelid);
						ftpPath = pathFile+ "/"+ reqID;
						List<SftpFileVo> files = new ArrayList<>();
						if (checkStatusVo == null  ) {
							
							files.add(new SftpFileVo(new File(pathUploadfile +"/" + requestForm.getCertificateFile()), ftpPath, requestForm.getCertificateFile()));
							files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getRequestFormFile()), ftpPath, requestForm.getRequestFormFile()));
							
							if (StringUtils.isNotBlank(requestForm.getReceiptFile())) {
								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getReceiptFile()), ftpPath, requestForm.getReceiptFile()));
							}
							if (StringUtils.isNotBlank(requestForm.getIdCardFile())) {
								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getIdCardFile()), ftpPath,  requestForm.getIdCardFile()));
							}
//							if (StringUtils.isNotBlank(requestForm.getChangeNameFile())) {
//								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getChangeNameFile()), ftpPath, requestForm.getChangeNameFile()));
//							}
							
						}else if (StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
							
							for (IndexGuoupResponse groupResp : checkStatusVo.getIndexGroups()) {
								if ( ! StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(groupResp.getFileResCode()) ) {
									files.add(new SftpFileVo(new File(pathUploadfile  +"/" + groupResp.getFileName() ), ftpPath, groupResp.getFileName() ));
								}
							}
							
						}else if (!StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())) {
							
							files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getCertificateFile()), ftpPath, requestForm.getCertificateFile()));
							files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getRequestFormFile()), ftpPath, requestForm.getRequestFormFile()));
							
							if (StringUtils.isNotBlank(requestForm.getReceiptFile())) {
								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getReceiptFile()), ftpPath, requestForm.getReceiptFile()));
							}
							
							if (StringUtils.isNotBlank(requestForm.getIdCardFile())) {
								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getIdCardFile()), ftpPath,  requestForm.getIdCardFile()));
							}
//							if (StringUtils.isNotBlank(requestForm.getChangeNameFile())) {
//								files.add(new SftpFileVo(new File(pathUploadfile  +"/" + requestForm.getChangeNameFile()), ftpPath, requestForm.getChangeNameFile()));
//							}
						}

						SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, TmbAesUtil.decrypt(keystorePath, ftpPassword));
						boolean ftpSuccess = SftpUtils.putFile(sftpVo,ftpPath);
						
						if(ftpSuccess) {
							countImport = 0;
							countCheckstatus = 0;
							while(countImport < timeLoop) {
								
								Thread.sleep(timesleep);
								ImportDocumentResponse impResp = callImportWS(requestForm,reqID,channelid, requestForm.getMakerById(),files,docTyep);
								
								if (CODE_SUCCESS.equals(impResp.getResCode())) {
//									log.info(" WS IMPORT SUCCESS ");
									statusUpload = true;
									break;
									
								}else {
									statusUpload = false;
									countImport++;
//									log.info("CALL WS IMPORT FIAL {} ", impResp.getDescription());
								}
							}
							if (statusUpload == true) {
								while (countCheckstatus < timeLoop) {
									
									Thread.sleep(timesleep);
									checkStatusVo  = CheckStatusWS(requestForm,reqID,channelid,"BATCH",docTyep);
									
									if (CODE_CHECK_SUCCESS.equals(checkStatusVo.getStatusCode())) {
//										log.info(" WS CHECK STATUS SUCCESS ");
										statusUpload = true;
										break ;
										
									}else if (CODE_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
										statusUpload = false;
										
									}else {
										statusUpload = false;
										
									}
									countCheckstatus++;
//									log.info(" WS CHECK STATUS FAIL "+checkStatusVo.getStatusCode() +" Des" + checkStatusVo.getDescription() );
								}		
							}
							if(statusUpload) {
								break;
							}
							
				
						} else {
							statusUpload = false;
							break;
						}
						countftp++;	
					}
					if (statusUpload) {
						isSuccess = isSuccess && statusUpload;
						int upldateResult = checkReqDetailDao.updateECMFlag(requestForm.getReqFormId(),StatusConstant.ECM_CONFIGURATION.ECM_SUCCESS);
//						log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS!! ");
					} else {
						isSuccess = false;
//						int upldateResult = checkReqDetailDao.updateECMFlag(requestForm.getReqFormId(),StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
						errorResp = errorResp +" REQ ID "+requestForm.getReqFormId().toString()+" upload file fail \n";
//						log.info(" END PROCESS UPLOAD CERTIFIACTE FAILED!! ");
					}
				}
				
				if (!isSuccess) {
					jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
				}
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}
			//############################ SEND DOCUMENTS TO ECM END #############################

		} catch (Exception ex) {
			log.error("ImportECMBatchService Error = ", ex);
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP , "FTP FILE IMPORT DOCUMENT FAIL");
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
	

	private ImportDocumentResponse  callImportWS(RequestForm reqVo,String reqID, String channelid, String userid, List<SftpFileVo> arrFile ,String docTyep) {
		String fileReq = "REQFORM";
		String fileRecp = "RECEIPT";
		String fileCer = "CERTIFICATE";
		String fileId = "IDCARD";
		String fileOther = "NCHANGE";
		
		String docTypeReq = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_REQ);
		String docTypeCer = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_CER);
		String docTypeRec =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_RECP);
		String docTypeId  =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_IDCARD);
		String docTypeOther =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_DOCTYPE_OTHER);
		
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_IMPORT_DOCUMENT);
		RestTemplate restTemplate = new RestTemplate();
		ImportDocumentRequest req = new ImportDocumentRequest();
//		List<FileImportRequest> files  = new ArrayList<>();
		req.setReqId(reqID);
		req.setCaNumber(reqVo.getCaNumber());
		req.setChannelId(channelid);
		req.setReqUserId(userid);
		req.setSegmentCode("");
		
		List<FileImportRequest> fileslist = new ArrayList<>();
		FileImportRequest fileImport ;
		for (int j = 0; j < arrFile.size(); j++) {
			fileImport = new FileImportRequest();
			String fileName = reqID + "/" + arrFile.get(j).getFileName();
			
			fileImport.setFileName(fileName);
			fileImport.setCusName(reqVo.getCompanyName());
//			fileImport.setDocTypeCode(docTyep);
			fileImport.setImportDate(EcerDateUtils.formatDDMMYYYYDate(new Date()));
			fileImport.setRegistrationId(reqVo.getOrganizeId());
			fileImport.setRefAppNo(reqVo.getTmbRequestNo());
			
			if (arrFile.get(j).getFileName().indexOf(fileReq) >=0) {
				fileImport.setDocTypeCode(docTypeReq);
			}else if (arrFile.get(j).getFileName().indexOf(fileRecp) >=0) {
				fileImport.setDocTypeCode(docTypeRec);
			}else if (arrFile.get(j).getFileName().indexOf(fileCer) >=0) {
				fileImport.setDocTypeCode(docTypeCer);
			}else if (arrFile.get(j).getFileName().indexOf(fileId) >=0) {
				fileImport.setDocTypeCode(docTypeId);
			}else if (arrFile.get(j).getFileName().indexOf(fileOther) >= 0) {
				fileImport.setDocTypeCode(docTypeOther);
			}
			
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
		
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHECK_STATUS);
		RestTemplate restTemplate = new RestTemplate();
		CheckStatusDocumentRequest checkReq = new CheckStatusDocumentRequest();
		checkReq.setReqId(reqID);
		checkReq.setChannelId(channelid);
		checkReq.setReqUserId(userid);
		checkReq.setSegmentCode("");
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
		String str = chanel + reqidGenKeyService.getNextKey();
		return str;

	}
	
	public void sendDocumentToECM_New() {
		EcertJobMonitoring jobMonitoring = new EcertJobMonitoring();
		Date current = new Date();
		long start = System.currentTimeMillis();
		String errorResp = "";
		boolean isSuccess = true;
		try {
			List<RequestForm> reqFormList = importECMBatchDao.getRequestFormWithEcmFlag();
			log.info(" START PROCESS UPLOAD CERTIFIACTE TO ECM BY BATCH JOB ");
			if (reqFormList != null && reqFormList.size() > 0) {
				log.info(" BATCH REQUEST FROM SIZE  "+reqFormList.size());
				
				for (RequestForm requestForm : reqFormList) {

					List<ECMUuploadRequest> listRequest = createRequest(requestForm, requestForm.getMakerById());
					boolean statusWS = CallECMWevserviceV2(listRequest);
					
					if (statusWS) {
						isSuccess = isSuccess && statusWS;
						int upldateResult = checkReqDetailDao.updateECMFlag(requestForm.getReqFormId(),StatusConstant.ECM_CONFIGURATION.ECM_SUCCESS);
					}else {
						isSuccess = isSuccess && statusWS;
						int upldateResult = checkReqDetailDao.updateECMFlag(requestForm.getReqFormId(),StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
						errorResp = errorResp +" REQ ID "+requestForm.getReqFormId().toString()+" upload file fail \n";
					}
				}
				
			}else {
				jobMonitoring.setErrorDesc(JOBMONITORING.BATCH_MESSAGE_NODATA);
			}

		} catch (Exception ex) {
			log.error("ImportECMBatchService Error = ", ex);
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP , "FTP FILE IMPORT DOCUMENT FAIL");
			jobMonitoring.setErrorDesc(ex.getMessage());
		}finally {
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

	}
	
	public List<ECMUuploadRequest> createRequest(RequestForm req,String userid) {
		List<ECMUuploadRequest> listRequest = new ArrayList<ECMUuploadRequest>();
		
		String tmbIdType = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_TMB_IDTYPE);
		String repositoryId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_REPOSITORY);
		String objTypeId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_OBJ_ID);
		String tmbSource =  ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_TMB_SOURCE);
		String chanelId = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_SUMMIT_CHANNEL);
		
		
		String docTypeReq = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_REQ);
		String docTypeCer = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_CER);
		String docTypeRec =ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_RECP);
		String docTypeId  = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_IDCARD);
		String docTypeOther  = ApplicationCache.getParamValueByName(ProjectConstant.ECM_PARAMETER.ECM_V2_DOCTYPE_OTHER);
		
		
		String pathReq = pathUploadfile +"/" + req.getRequestFormFile();
		String pathCer = pathUploadfile +"/" + req.getCertificateFile();
		String pathRec = pathUploadfile +"/" + req.getReceiptFile();
		String pathIdCard = "";
		String pathOther = "";
		ECMUuploadRequest ecmUploadRequest = null;
		EcmMasterData ecmMaster = null;
		try {
			for (int i = 0; i < 5; i++) {
				ecmUploadRequest = new ECMUuploadRequest();
				
				ecmUploadRequest.setRepositoryId(repositoryId);
				ecmUploadRequest.setObjectTypeId(objTypeId);
				//NAME : YYYYMMDDHHMM-DocTypeCode-ShortDocName-FileName.xxx
				ecmUploadRequest.setTmbSource(tmbSource);
				ecmUploadRequest.setTmbCreatorId(userid); //user
				ecmUploadRequest.setTmbIdentificationId(req.getOrganizeId());
				ecmUploadRequest.setTmbIdentificationType(tmbIdType);
				ecmUploadRequest.setChannel(chanelId);
				ecmUploadRequest.setApplicationId(req.getTmbRequestNo());
				ecmUploadRequest.setCustomerFirstNameThai(req.getCustomerName());
				ecmUploadRequest.setCustomerFirstNameEng(req.getCustomerName());
				
				if (i==0) {
					byte[] bty = FileUtils.readFileToByteArray(new File(pathReq));
					ecmUploadRequest.setTmbDocTypeCode(docTypeReq);
					ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
					
					ecmUploadRequest.setName(this.convertFilenameForECM(req.getRequestFormFile(), docTypeReq, ecmMaster.getTypeShortName()));
					ecmUploadRequest.setFile(bty);
					ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
					ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
					ecmUploadRequest.setCustomerFirstNameThai(ecmMaster.getTypeNameTh());
					ecmUploadRequest.setCustomerFirstNameEng(ecmMaster.getTypeNameEn());
				}else if(i == 1) {
					ecmUploadRequest.setTmbDocTypeCode(docTypeCer);
					ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
					
					ecmUploadRequest.setName(this.convertFilenameForECM(req.getCertificateFile(), docTypeCer, ecmMaster.getTypeShortName()));
					byte[] bty = FileUtils.readFileToByteArray(new File(pathCer));
					ecmUploadRequest.setFile(bty);
					ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
					ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
					ecmUploadRequest.setCustomerFirstNameThai(ecmMaster.getTypeNameTh());
					ecmUploadRequest.setCustomerFirstNameEng(ecmMaster.getTypeNameEn());
				}else if(i == 2) {
					if(StringUtils.isNotBlank(req.getReceiptFile())) {
						ecmUploadRequest.setTmbDocTypeCode(docTypeRec);
						ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
						
						ecmUploadRequest.setName(this.convertFilenameForECM(req.getReceiptFile(), docTypeRec, ecmMaster.getTypeShortName()));
						byte[] bty = FileUtils.readFileToByteArray(new File(pathRec));
						ecmUploadRequest.setFile(bty);
						ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
						ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
						ecmUploadRequest.setCustomerFirstNameThai(ecmMaster.getTypeNameTh());
						ecmUploadRequest.setCustomerFirstNameEng(ecmMaster.getTypeNameEn());
					}
				}else if (i==3) {
					if(StringUtils.isNotBlank(req.getIdCardFile())) {
						ecmUploadRequest.setTmbDocTypeCode(docTypeId);
						ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
						
						ecmUploadRequest.setName(this.convertFilenameForECM(req.getIdCardFile(), docTypeId, ecmMaster.getTypeShortName()));
						byte[] bty = FileUtils.readFileToByteArray(new File(pathUploadfile +"/" +req.getIdCardFile()));
						ecmUploadRequest.setFile(bty);
						ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
						ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
						ecmUploadRequest.setCustomerFirstNameThai(ecmMaster.getTypeNameTh());
						ecmUploadRequest.setCustomerFirstNameEng(ecmMaster.getTypeNameEn());
					}
				}
/*				else if (i==4) {
					if(StringUtils.isNotBlank(req.getChangeNameFile())) {
						ecmUploadRequest.setTmbDocTypeCode(docTypeOther);
						ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
						
						ecmUploadRequest.setName(this.convertFilenameForECM(req.getChangeNameFile(), docTypeOther, ecmMaster.getTypeShortName()));
						byte[] bty = FileUtils.readFileToByteArray(new File(pathUploadfile +"/" +req.getChangeNameFile()));
						ecmUploadRequest.setFile(bty);
						ecmUploadRequest.setArchival(this.convertYearFromMaster(ecmMaster.getArchivalPeriod()));
						ecmUploadRequest.setDisposal(this.convertYearFromMaster(ecmMaster.getDisposalPeriod()));
						
					}
				}*/
				listRequest.add(ecmUploadRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRequest;
	}

	public boolean CallECMWevserviceV2(List<ECMUuploadRequest> listReq) throws URISyntaxException {
		ECMUploadResponse response = new ECMUploadResponse();
		RestTemplate restTemplate = new RestTemplate();
		URI fooResourceUrl = new URI( ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CREATE_DOC));
		boolean statusWS = false;
		for (int i = 0; i < listReq.size(); i++) {
			if (listReq.get(i).getFile() != null) {
				HttpEntity<ECMUuploadRequest> request = new HttpEntity<>(listReq.get(i));
				ResponseEntity<ECMUploadResponse> wsresponse = restTemplate.exchange(fooResourceUrl , HttpMethod.POST, request, ECMUploadResponse.class);
				response = wsresponse.getBody();
				if (!StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(response.getStatusCode())) {
					statusWS = false;
					break;
				}
				statusWS = true;
				log.info("CALL WS CHECK STATUS {}", response.getDescription());
			}
		}
		return statusWS;
		
	}
	
	public String convertFilenameForECM(String fileName,String docTypeCode ,String shotName ) {
//		format file YYYYMMMDDHHMM-DoctypeCode-ShotDoctypeName-fileName.xxx
		
		return String.format("%s-%s-%s-%s", EcerDateUtils.formatYYMMDDDate(new Date()),docTypeCode,shotName,fileName);
		
	}
	
	public int convertYearFromMaster(int year) {
		int yearReturn = Integer.parseInt(EcerDateUtils.formatYYYY_EN(new Date())) + year;
		
		return yearReturn;
		
	}
}
