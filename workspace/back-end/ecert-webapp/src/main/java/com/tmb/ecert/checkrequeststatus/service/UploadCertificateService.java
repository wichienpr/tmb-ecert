package com.tmb.ecert.checkrequeststatus.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FileImportRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.IndexGuoupResponse;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class UploadCertificateService {

	private static final Logger log = LoggerFactory.getLogger(UploadCertificateService.class);

	@Value("${app.datasource.path.upload}")
	private String pathUploadfiel;
	
	@Value("${aes256.keystore.path}")
	private String keystorePath;


	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	@Autowired
	private EmailService emailservice;
	
	@Autowired
	private ReqidGenKeyService reqidGenKeyService;

	public void uploadEcertificate(Long certificateID, String userid) throws Exception{
		String channelid = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHANNELID);
		String docTyep = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_DOCTYPE);
		String ftpPath = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PATH); 
		String ftpHost= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_HOST);
		String ftpUsername= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_USERNAME);
		String ftpPassword= ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_PARAMS.ECM_FTP_PASSWORD);
		
		int countftp = 0;
		String wsErrorDesc = "";
//		boolean statusUpload[] = new boolean[3];
		boolean statusUpload = false;
		boolean statusCheck = false;
		String pathFile = "";
		RequestForm reqVo = null;
		CheckStatusDocumentResponse checkStatusVo = null;
//		try {
			log.info(" START PROCESS UPLOAD CERTIFIACTE BY CERTIFICATE ID "+Long.toString(certificateID));
			int timesleep = Integer.parseInt(
					ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP));
			int timeLoop = Integer.parseInt(
					ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME));

			Assert.isTrue(timesleep > 0,
					ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP + " is greater than 0");
			Assert.isTrue(timeLoop > 0,
					ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME + " is greater than 0");
			uploadEcerLoop: while (countftp < timeLoop) {
				
				String reqID = ramdomKey(channelid);
				
				pathFile = ftpPath+ "/"+ reqID;

				// STEP 1 : read 3 file from db
				reqVo = checkReqDetailDao.findCertificateFileByReqID(certificateID);
				String pathReq = pathUploadfiel +"/" + reqVo.getRequestFormFile();
				String pathCer = pathUploadfiel +"/" + reqVo.getCertificateFile();
				String pathRec = pathUploadfiel +"/" + reqVo.getReceiptFile();
				
				// STEP 1. SFTP File and save log fail or success !!
				List<SftpFileVo> files = new ArrayList<>();
				if (checkStatusVo == null  ) {
					
					files.add(new SftpFileVo(new File(pathReq), pathFile, reqVo.getCertificateFile()));
					files.add(new SftpFileVo(new File(pathCer), pathFile, reqVo.getRequestFormFile()));
					files.add(new SftpFileVo(new File(pathRec), pathFile, reqVo.getReceiptFile()));
					
					if (StringUtils.isNotBlank(reqVo.getIdCardFile())) {
						files.add(new SftpFileVo(new File(pathUploadfiel  +"/"+ reqVo.getIdCardFile()), pathFile,  reqVo.getIdCardFile()));
					}
					if (StringUtils.isNotBlank(reqVo.getChangeNameFile())) {
						files.add(new SftpFileVo(new File(pathUploadfiel  +"/"+ reqVo.getChangeNameFile()), pathFile, reqVo.getChangeNameFile()));
					}
					
				}else if (StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
					
					for (IndexGuoupResponse groupResp : checkStatusVo.getIndexGroups()) {
						if ( ! StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(groupResp.getFileResCode()) ) {
							files.add(new SftpFileVo(new File(pathUploadfiel +"/" + groupResp.getFileName() ), pathFile, groupResp.getFileName() ));
						}
					}
					
				}else if (!StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())) {
					
					files.add(new SftpFileVo(new File(pathReq), pathFile, reqVo.getCertificateFile()));
					files.add(new SftpFileVo(new File(pathCer), pathFile, reqVo.getRequestFormFile()));
					files.add(new SftpFileVo(new File(pathRec), pathFile, reqVo.getReceiptFile()));
					
					if (StringUtils.isNotBlank(reqVo.getIdCardFile())) {
						files.add(new SftpFileVo(new File(pathUploadfiel +"/" + reqVo.getIdCardFile()), pathFile,  reqVo.getIdCardFile()));
					}
					if (StringUtils.isNotBlank(reqVo.getChangeNameFile())) {
						files.add(new SftpFileVo(new File(pathUploadfiel +"/" + reqVo.getChangeNameFile()), pathFile, reqVo.getChangeNameFile()));
					}
				}

				SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, TmbAesUtil.decrypt(keystorePath, ftpPassword));
				boolean isSuccess = SftpUtils.putFile(sftpVo,pathFile);
				if (isSuccess) {
					Thread.sleep(3000);
					int countImport = 0;
					int countCheckStatus = 0;
					log.info(" IMPORT DOCUMENT BY REQID {} ", reqID);
//						call ws import document
					while (countImport < timeLoop) {
						Thread.sleep(timesleep);

						ImportDocumentResponse impResp = callImportWS(reqVo, reqID, channelid, userid, files, docTyep);
						if (StatusConstant.IMPORT_ECM_WS.IMPORT_STATUS_SUCCESS.equals(impResp.getResCode())) {
							statusUpload = true;
							log.info("CALL WS IMPORT DOCUMENT SUCCESS ");
							break ;
						} else {
							wsErrorDesc = impResp.getResCode()+" "+impResp.getDescription();
							statusUpload = false;
							countImport++;
							log.info("CALL WS IMPORT DOCUMENT FIAL {} ", impResp.getDescription());
							
							if(countImport >=3) {
								break uploadEcerLoop;
							}
						}
					}
					
					if(statusUpload == true) {
						while (countCheckStatus < timeLoop) {
//							call ws checkstatus document
							Thread.sleep(timesleep);
							checkStatusVo = CheckStatusWS(reqVo, reqID, channelid, userid,
									docTyep);
							log.info("CALL WS CHECK STATUS {}", checkStatusVo.getDescription());

							if (StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(checkStatusVo.getStatusCode())) {
//									statusUpload[i] = true;
								statusUpload = true;
								break uploadEcerLoop;

							} else if (StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
								statusUpload = false;
//								statusUpload[i] = false;
							}else {
								wsErrorDesc = checkStatusVo.getStatusCode()+" "+checkStatusVo.getDescription();
								statusUpload = false;
//									statusUpload[i] = false;
							}
							countCheckStatus++;
						}
					}

				} else {
					wsErrorDesc = "FTP FILE IMPORT DOCUMENT FAIL";
					statusUpload = false;
					emailservice.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_FTP, wsErrorDesc );
					break;
				}
				countftp++;
			}

			if (statusUpload) {
				int upldateResult = checkReqDetailDao.updateECMFlag(certificateID);
				log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS!! ");
			} else {
//				emailservice.sendEmailFailSendDoc(reqVo,new Date(),wsErrorDesc);
				log.error("END PROCESS UPLOAD CERTIFIACTE CERTIFICATE FAIL ", wsErrorDesc);
//				throw new Exception(wsErrorDesc);
			}

//		} catch (Exception e) {
//			emailservice.sendEmailFailSendDoc(reqVo,new Date(),e.toString());
//			log.error("END PROCESS UPLOAD CERTIFIACTE CERTIFICATE FAIL ", e);
//		}

	}

	private String convertCostomerSegment(String segmentcode) {
		if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_1.equals(segmentcode)) {
			return "1";
		} else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_2.equals(segmentcode)) {
			return "2";
		} else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_3.equals(segmentcode)) {
			return "3";
		} else if (StatusConstant.IMPORT_ECM_WS.SEGMENTCODE_MAP_4.equals(segmentcode)) {
			return "4";
		} else {
			return "";
		}
	}

	private String ramdomKey(String chanel) {
		String str = chanel + reqidGenKeyService.getNextKey();
		return str;

	}

	private ImportDocumentResponse callImportWS(RequestForm reqVo, String reqID, String channelid, String userid,
			List<SftpFileVo> files, String docTyep) {

		String endPoint = ApplicationCache
				.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_IMPORT_DOCUMENT);
		RestTemplate restTemplate = new RestTemplate();
		ImportDocumentRequest req = new ImportDocumentRequest();
		List<FileImportRequest> fileslist = new ArrayList<>();
		FileImportRequest fileImport;
		for (int j = 0; j < files.size(); j++) {
			fileImport = new FileImportRequest();
			String fileName = reqID + "/" + files.get(j).getFileName();

			fileImport.setFileName(fileName);
			fileImport.setCusName(reqVo.getCompanyName());
			fileImport.setDocTypeCode(docTyep);
			fileImport.setImportDate(EcerDateUtils.formatDDMMYYYYDate(new Date()));
			fileImport.setRegistrationId(reqVo.getOrganizeId());
			fileImport.setRefAppNo(reqVo.getTmbRequestNo());

			fileslist.add(fileImport);
		}
		req.setFiles(fileslist);
		req.setReqId(reqID);
		req.setCaNumber(reqVo.getCaNumber());
		req.setChannelId(channelid);
		req.setReqUserId(userid);
		req.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));

//		req.setCusName(reqVo.getCompanyName());

		HttpEntity<ImportDocumentRequest> request = new HttpEntity<>(req);
		ResponseEntity<ImportDocumentResponse> response = restTemplate.exchange(StringUtils.trim(endPoint),
				HttpMethod.POST, request, ImportDocumentResponse.class);

		ImportDocumentResponse resVo = response.getBody();
		return resVo;
	}

	private CheckStatusDocumentResponse CheckStatusWS(RequestForm reqVo, String reqID, String channelid, String userid,
			String docTyep) {
		String endPoint = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHECK_STATUS);
		RestTemplate restTemplate = new RestTemplate();
		CheckStatusDocumentRequest checkReq = new CheckStatusDocumentRequest();
		checkReq.setReqId(reqID);
		checkReq.setChannelId(channelid);
		checkReq.setReqUserId(userid);
		checkReq.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));
		checkReq.setCaNumber(reqVo.getCaNumber());

		HttpEntity<CheckStatusDocumentRequest> chekcRequest = new HttpEntity<>(checkReq);
		ResponseEntity<CheckStatusDocumentResponse> checkResponse = restTemplate.exchange(StringUtils.trim(endPoint),
				HttpMethod.POST, chekcRequest, CheckStatusDocumentResponse.class);
		CheckStatusDocumentResponse checkStatusVo = checkResponse.getBody();
		return checkStatusVo;
	}

}
