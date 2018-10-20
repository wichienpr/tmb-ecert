package com.tmb.ecert.checkrequeststatus.service;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.CheckStatusDocumentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FileImportRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ImportDocumentResponse;
import com.tmb.ecert.common.constant.ProjectConstant;
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

	private static String PATH_UPLOAD = "tmb-requestor/";
	private static String CODE_SUCCESS = "0000";
	private static String CODE_PARTIAL_SUCCESS = "0001";
	private static String CODE_CHECK_SUCCESS = "0";

	private static String SEGMENTCODE_1 = "20005";
	private static String SEGMENTCODE_2 = "20004";
	private static String SEGMENTCODE_3 = "20003";
	private static String SEGMENTCODE_4 = "20002";

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;

	@Async
	public void uploadEcertificate(Long certificateID, String userid) {
		String channelid = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CHANNELID);
		String docTyep = ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_DOCTYPE);
		int countftp = 0;
//		boolean statusUpload[] = new boolean[3];
		boolean statusUpload = false;
		boolean statusCheck = false;
		try {
			log.info(" START PROCESS UPLOAD CERTIFIACTE... ");
			int timesleep = Integer.parseInt(
					ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP));
			int timeLoop = Integer.parseInt(
					ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME));

			Assert.isTrue(timesleep > 0,
					ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_SLEEP + " is greater than 0");
			Assert.isTrue(timeLoop > 0,
					ProjectConstant.WEB_SERVICE_ENDPOINT.UPLOADCERTIFICARE_TIME + " is greater than 0");
			uploadEcerLoop: while (countftp < timeLoop) {

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
					Thread.sleep(3000);
					int countImport = 0;
					int countCheckStatus = 0;
					String reqID = ramdomKey(channelid);
					log.info(" IMPORT DOCUMENT BY REQID {} ", reqID);
//						call ws import document
					while (countImport < timeLoop) {
						Thread.sleep(timesleep);

						ImportDocumentResponse impResp = callImportWS(reqVo, reqID, channelid, userid, files, docTyep);
						if (CODE_SUCCESS.equals(impResp.getResCode())) {
							statusUpload = true;
							log.info("CALL WS IMPORT DOCUMENT SUCCESS ");
							break ;
						} else {
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
							CheckStatusDocumentResponse checkStatusVo = CheckStatusWS(reqVo, reqID, channelid, userid,
									docTyep);
							log.info("CALL WS CHECK STATUS {}", checkStatusVo.getDescription());

							if (CODE_CHECK_SUCCESS.equals(checkStatusVo.getStatusCode())) {
//									statusUpload[i] = true;
								statusUpload = true;
								break uploadEcerLoop;

							} else if (CODE_PARTIAL_SUCCESS.equals(checkStatusVo.getStatusCode())){
								statusUpload = false;
//								statusUpload[i] = false;
								countCheckStatus++;
							}else {
								statusUpload = false;
//									statusUpload[i] = false;
								countCheckStatus++;
							}
						}
					}

				} else {
					statusUpload = false;
					break;
				}
				countftp++;
			}

			if (statusUpload) {
				int upldateResult = checkReqDetailDao.updateECMFlag(certificateID);
				log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS!! ");
			} else {
				log.info(" END PROCESS UPLOAD CERTIFIACTE FAILED!! ");
			}

		} catch (Exception e) {
			log.error("FTP FILE UPLOAD CERTIFICATE ERR ", e);
		}

	}

	private String convertCostomerSegment(String segmentcode) {
		if (SEGMENTCODE_1.equals(segmentcode)) {
			return "1";
		} else if (SEGMENTCODE_2.equals(segmentcode)) {
			return "2";
		} else if (SEGMENTCODE_3.equals(segmentcode)) {
			return "3";
		} else if (SEGMENTCODE_4.equals(segmentcode)) {
			return "4";
		} else {
			return "0";
		}
	}

	private String ramdomKey(String chanel) {
		int random = new Random().nextInt(999999);
		String str = chanel + EcerDateUtils.formatYYMMDDDate(new Date())
				+ StringUtils.leftPad(Integer.toString(random), 6, "0");
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
		checkReq.setReqId(null);
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
