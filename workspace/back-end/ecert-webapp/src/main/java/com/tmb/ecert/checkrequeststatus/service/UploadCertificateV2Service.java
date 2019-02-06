package com.tmb.ecert.checkrequeststatus.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.ECM_MASTER;
import com.tmb.ecert.batchjob.domain.EcmMasterData;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestCertificateDao;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUploadResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ECMUuploadRequest;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.EmailService;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class UploadCertificateV2Service {
	
	private static final Logger log = LoggerFactory.getLogger(UploadCertificateService.class);
	
	@Value("${app.datasource.path.upload}")
	private String pathUploadfile;
	
	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	@Autowired
	private CheckRequestCertificateDao checkRequestCerDao;
	
	@Autowired
	private EmailService emailservice;
	
	public void uploadCertificate(Long certificateID, String userid) throws Exception {
		
		log.info(" START PROCESS UPLOAD CERTIFIACTE BY CERTIFICATE ID "+Long.toString(certificateID));
		RequestForm reqVo =  null;
		String wsErrorDesc = "";
		try {

			reqVo = checkReqDetailDao.findCertificateFileByReqID(certificateID);

			List<String> listFile = new ArrayList<String>();
			String pathReq = pathUploadfile +"/" + reqVo.getRequestFormFile();
			String pathCer = pathUploadfile +"/" + reqVo.getCertificateFile();
			String pathRec = pathUploadfile +"/" + reqVo.getReceiptFile();
			String pathIdCard = "";
			String pathOther = "";
			
			listFile.add(pathRec);
			listFile.add(pathCer);
			listFile.add(pathReq);

			
			if (StringUtils.isNotBlank(reqVo.getIdCardFile())) {
				pathIdCard = reqVo.getIdCardFile();
				listFile.add(pathIdCard);
			}
			if (StringUtils.isNotBlank(reqVo.getChangeNameFile())) {
				pathOther = reqVo.getChangeNameFile();
				listFile.add(pathOther);
			}
			
			List<ECMUuploadRequest> listRequest = createRequest(reqVo, userid);
//			log.info(" prepair cmis request success total file : "+Integer.toString(listRequest.size()));
			boolean statusWS = false;
			statusWS = CallECMWevserviceV2(listRequest,userid,certificateID);
			
			if (!statusWS) {
				checkReqDetailDao.updateECMFlag(certificateID,StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
				log.info(" END PROCESS UPLOAD CERTIFIACTE FAIL!! ");
				throw new Exception();
			}
			checkReqDetailDao.updateECMFlag(certificateID,StatusConstant.ECM_CONFIGURATION.ECM_SUCCESS);
			log.info(" END PROCESS UPLOAD CERTIFIACTE SUCCESS.. ");
			
		} catch (Exception e) {
			checkReqDetailDao.updateECMFlag(certificateID,StatusConstant.ECM_CONFIGURATION.ECM_FAIL);
			emailservice.sendEmailFailSendDoc(reqVo,new Date(),e.toString());
			log.error("END PROCESS UPLOAD CERTIFIACTE CERTIFICATE ERROR ", e);
			throw new Exception(wsErrorDesc);
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
				ecmMaster = null;
				ecmUploadRequest.setRepositoryId(repositoryId);
				ecmUploadRequest.setObjectTypeId(objTypeId);
				//NAME : YYYYMMDDHHMM-DocTypeCode-ShortDocName-FileName.xxx
				ecmUploadRequest.setTmbSource(tmbSource);
				ecmUploadRequest.setTmbCreatorId(userid); //user
				ecmUploadRequest.setTmbIdentificationId(req.getOrganizeId());
				ecmUploadRequest.setTmbIdentificationType(tmbIdType);
				ecmUploadRequest.setChannel(chanelId);
				ecmUploadRequest.setApplicationId(req.getTmbRequestNo());
				ecmUploadRequest.setCustomerFirstNameThai(req.getCompanyName());
//				ecmUploadRequest.setCustomerFirstNameEng(req.getCustomerName());
				
				if (i==0) {
					byte[] bty = FileUtils.readFileToByteArray(new File(pathReq));
					ecmUploadRequest.setTmbDocTypeCode(docTypeReq);
					ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
//					ecmMaster = this.mockEcmMaster();
					
					ecmUploadRequest.setName(this.convertFilenameForECM(req.getRequestFormFile(), docTypeReq, ecmMaster.getTypeShortName()));
					ecmUploadRequest.setFile(bty);
					ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
					ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
				}else if(i == 1) {
					ecmUploadRequest.setTmbDocTypeCode(docTypeCer);
					ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
//					ecmMaster = this.mockEcmMaster();
					
					ecmUploadRequest.setName(this.convertFilenameForECM(req.getCertificateFile(), docTypeCer, ecmMaster.getTypeShortName()));
					byte[] bty = FileUtils.readFileToByteArray(new File(pathCer));
					ecmUploadRequest.setFile(bty);
					ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
					ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());

				}else if(i == 2) {
					ecmUploadRequest.setTmbDocTypeCode(docTypeRec);
					ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
//					ecmMaster = this.mockEcmMaster();
					
					ecmUploadRequest.setName(this.convertFilenameForECM(req.getReceiptFile(), docTypeRec, ecmMaster.getTypeShortName()));
					byte[] bty = FileUtils.readFileToByteArray(new File(pathRec));
					ecmUploadRequest.setFile(bty);
					ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
					ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
				}else if (i==3) {
					if(StringUtils.isNotBlank(req.getIdCardFile())) {
						ecmUploadRequest.setTmbDocTypeCode(docTypeId);
						ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
//						ecmMaster = this.mockEcmMaster();
						
						ecmUploadRequest.setName(this.convertFilenameForECM(req.getIdCardFile(), docTypeId, ecmMaster.getTypeShortName()));
						byte[] bty = FileUtils.readFileToByteArray(new File(pathUploadfile +"/" +req.getIdCardFile()));
						ecmUploadRequest.setFile(bty);
						ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
						ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
					}
				}else if (i==4) {
					if(StringUtils.isNotBlank(req.getChangeNameFile())) {
						ecmUploadRequest.setTmbDocTypeCode(docTypeOther);
						ecmMaster = checkReqDetailDao.findECMMaster(ecmUploadRequest);
//						ecmMaster = this.mockEcmMaster();
						
						ecmUploadRequest.setName(this.convertFilenameForECM(req.getChangeNameFile(), docTypeOther, ecmMaster.getTypeShortName()));
						byte[] bty = FileUtils.readFileToByteArray(new File(pathUploadfile +"/" +req.getChangeNameFile()));
						ecmUploadRequest.setFile(bty);
						ecmUploadRequest.setArchival(ecmMaster.getArchivalPeriod());
						ecmUploadRequest.setDisposal(ecmMaster.getDisposalPeriod());
						
					}
				}
				listRequest.add(ecmUploadRequest);
			}
		} catch (Exception e) {
			log.info(" PREPAIR REQUEST CMIS FIAL {} "+e);
		}
		return listRequest;
	}

	public boolean CallECMWevserviceV2(List<ECMUuploadRequest> listReq,String userid,Long certificateID) throws URISyntaxException {
		ECMUploadResponse response = new ECMUploadResponse();
		RestTemplate restTemplate = new RestTemplate();
		URI fooResourceUrl = new URI( ApplicationCache.getParamValueByName(ProjectConstant.WEB_SERVICE_ENDPOINT.ECM_CREATE_DOC));
		boolean statusWS = false;
//		log.info(" call webserivce process .. ");
		for (int i = 0; i < listReq.size(); i++) {
			if (listReq.get(i).getFile() != null) {
				HttpEntity<ECMUuploadRequest> request = new HttpEntity<>(listReq.get(i));
				ResponseEntity<ECMUploadResponse> wsresponse = restTemplate.exchange(fooResourceUrl , HttpMethod.POST, request, ECMUploadResponse.class);
				response = wsresponse.getBody();
				if (!StatusConstant.IMPORT_ECM_WS.CHECK_STATUS_SUCCESS.equals(response.getStatusCode())) {
					log.info(" call ws create doc {} ",response.getDescription());
					statusWS = false;
					break;
				}
				statusWS = true;
				checkRequestCerDao.insertECMHistory(certificateID, listReq.get(i), response.getObjectId(), userid);
				log.info("call ws create doc {}", response.getDescription());
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
	public EcmMasterData mockEcmMaster() {
		EcmMasterData data = new EcmMasterData();
		data.setArchivalPeriod(1);
		data.setDisposalPeriod(10);
		data.setTypeShortName("TypeShotname");
		data.setTypeNameTh("ประเภทเอกสาร");
		data.setTypeNameEn("TypeDoc");
		return data;
		
	}

}
 