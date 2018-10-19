package com.tmb.ecert.checkrequeststatus.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;
import com.tmb.ecert.common.utils.SftpUtils;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class UploadCertificateService {

	private static final Logger log = LoggerFactory.getLogger(UploadCertificateService.class);

	@Value("${app.datasource.path.upload}")
	private String pathUploadfiel;

	@Value("${app.datasource.ftp.path}")
	private String ftpPath;

	@Value("${app.datasource.ftp.url}")
	private String ftpHost;

	@Value("${app.datasource.ftp.username}")
	private String ftpUsername;

	@Value("${app.datasource.ftp.password}")
	private String ftpPassword;
	
	
	@Value("${app.datasource.ws.url}")
	private String WSROOT;
	
	@Value("${app.datasource.ws.importdoc}")
	private String WSIMPORTURL;

	@Value("${app.datasource.ws.checkstatus}")
	private String WSCHECKURL;


	private static String PATH_UPLOAD = "tmb-requestor/";
	private static String CODE_SUCCESS = "0000";
	private static String CODE_PARTIAL_SUCCESS = "0001";
	private static String CODE_CHECK_SUCCESS = "0";
	
	private static String SEGMENTCODE_1 = "20005";
	private static String SEGMENTCODE_2 = "20004";
	private static String SEGMENTCODE_3 = "20003";
	private static String SEGMENTCODE_4 = "20002";
	
//	private static String WSIMPORTURL = "http://150.95.24.42:8080/tmp-ws/api-payment/importDocument";
//	private static String WSCHECKURL = "http://150.95.24.42:8080/tmp-ws/api-payment/checkStatusDocument";

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;

	@Async
	public void uploadEcertificate(Long certificateID, String userid) {
		String channelid = ApplicationCache.getParamValueByName("ecm.channelid");
		String docTyep = ApplicationCache.getParamValueByName("ecm.doctype");


		int countftp = 0;
		boolean statusUpload[] = new boolean[3];
		boolean statusftp = false;
		try {
			log.info(" START PROCESS UPLOAD CERTIFIACTE... ");
			int timesleep = Integer.parseInt(ApplicationCache.getParamValueByName("ecer.upload.certificate.sleep"));
			int timeLoop = Integer.parseInt(ApplicationCache.getParamValueByName("ecer.upload.certificate.time"));
			uploadEcerLoop: 
			while (countftp < timeLoop) {

				// STEP 1 : read 3 file from db
				RequestForm reqVo = checkReqDetailDao.findCertificateFileByReqID(certificateID);
				String pathReq = pathUploadfiel + PATH_UPLOAD + reqVo.getRequestFormFile();
				String pathCer = pathUploadfiel + PATH_UPLOAD + reqVo.getCertificateFile();
				String pathRec = pathUploadfiel + PATH_UPLOAD + reqVo.getReceiptFile();
				// STEP 1. SFTP File and save log fail or success !!
				List<SftpFileVo> files = new ArrayList<>();
				files.add(new SftpFileVo(new File(pathReq), ftpPath, reqVo.getCertificateFile()));
				files.add(new SftpFileVo(new File(pathCer), ftpPath, reqVo.getRequestFormFile()));
				files.add(new SftpFileVo(new File(pathRec), ftpPath, reqVo.getReceiptFile()));

				SftpVo sftpVo = new SftpVo(files, ftpHost, ftpUsername, ftpPassword);
				boolean isSuccess = SftpUtils.putFile(sftpVo);
				if (isSuccess) {
					for (int i = 0; i < files.size(); i++) {
						
						Thread.sleep(10000);
						int countImport = 0;
//						call ws import document
						wsimportLoop: while ( countImport < timeLoop ) {
							
							int countCheckStatus = 0;
							Thread.sleep(timesleep);
							log.info("FTP FILE UPLOAD CERTIFICATE SUCCESS....");
							String reqID = ramdomKey(channelid);
							String fileName = reqID + "/" + files.get(i).getFileName();
							log.info(" IMPORT DOCUMENT REQID IS {} ", reqID, "filename {}", fileName);
							
							ImportDocumentResponse impResp = callImportWS(reqVo,reqID,channelid,userid,fileName,docTyep);

							if (CODE_SUCCESS.equals(impResp.getResCode())) {
								log.info("CALL WS IMPORT SUCCESS ");
								while (countCheckStatus < timeLoop) {
//									call ws checkstatus document
									Thread.sleep(timesleep);
									CheckStatusDocumentResponse checkStatusVo  = CheckStatusWS(reqVo,reqID,channelid,userid,fileName,docTyep);
									log.info("CALL WS CHECK STATUS {}", checkStatusVo.getDescription());
									
									if (CODE_CHECK_SUCCESS.equals(checkStatusVo.getStatusCode())) {
										statusUpload[i] = true;
//										break loop ws_import and then select file for call ws_import  
										break wsimportLoop;

									}else {
										statusUpload[i] = false;
										countCheckStatus++;
									}
								}
							} else {
								countImport++;
								log.info("CALL WS IMPORT FIAL {} ", impResp.getDescription());
							}
						}
					}
					boolean checkFail = true;
					for (boolean  arr : statusUpload) {
						checkFail = checkFail && arr;
					}
					if (checkFail == true) {
						statusftp = true;
						break uploadEcerLoop;
					}else {
						statusftp = false;
					}
					
				}
				countftp++;
			}
		
			if (statusftp) {
				int upldateResult = checkReqDetailDao.updateECMFlag(certificateID);
				log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS!! ");
			}else {
				log.info(" END PROCESS UPLOAD CERTIFIACTE FAILED!! ");
			}

		} catch (Exception e) {
			log.error("FTP FILE UPLOAD CERTIFICATE ERR ", e);
		}

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
	
	private ImportDocumentResponse  callImportWS(RequestForm reqVo,String reqID, String channelid, String userid, String  fileName ,String docTyep) {
		RestTemplate restTemplate = new RestTemplate();
		ImportDocumentRequest req = new ImportDocumentRequest();
		req.setReqId(null);
		req.setCaNumber(reqVo.getCaNumber());
		req.setChannelId(channelid);
		req.setReqUserId(userid);
		req.setFileName(fileName);
		req.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));
		req.setDocTypeCode(docTyep);
		req.setImportDate(EcerDateUtils.formatDDMMYYYYDate(new Date()));
		req.setRegistrationId(reqVo.getOrganizeId());
		req.setRefAppNo(reqVo.getTmbRequestNo());
		req.setCusName(reqVo.getCompanyName());

		HttpEntity<ImportDocumentRequest> request = new HttpEntity<>(req);
		ResponseEntity<ImportDocumentResponse> response = restTemplate.exchange(WSROOT+WSIMPORTURL,
				HttpMethod.POST, request, ImportDocumentResponse.class);

		ImportDocumentResponse resVo = response.getBody();
		return resVo;
	}
	
	private CheckStatusDocumentResponse CheckStatusWS(RequestForm reqVo,String reqID, String channelid, String userid, String  fileName ,String docTyep) {
		RestTemplate restTemplate = new RestTemplate();
		CheckStatusDocumentRequest checkReq = new CheckStatusDocumentRequest();
		checkReq.setReqId(reqID);
		checkReq.setChannelId(channelid);
		checkReq.setReqUserId(userid);
		checkReq.setSegmentCode(convertCostomerSegment(reqVo.getCustsegmentCode()));
		checkReq.setCaNumber(reqVo.getCaNumber());

		HttpEntity<CheckStatusDocumentRequest> chekcRequest = new HttpEntity<>(checkReq);
		ResponseEntity<CheckStatusDocumentResponse> checkResponse = restTemplate.exchange(
				WSROOT+WSCHECKURL, HttpMethod.POST, chekcRequest,
				CheckStatusDocumentResponse.class);
		CheckStatusDocumentResponse checkStatusVo = checkResponse.getBody();
		return checkStatusVo;
	}

}
