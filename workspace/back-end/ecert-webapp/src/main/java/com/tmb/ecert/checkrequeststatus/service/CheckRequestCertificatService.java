package com.tmb.ecert.checkrequeststatus.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestCertificateDao;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.CertificateVo;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;


@Service
public class CheckRequestCertificatService {

	private static String PATH_UPLOAD = "tmb-requestor/";

	@Autowired
	private UploadService upload;

	@Autowired
	private CheckRequestCertificateDao certificateDao;

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	@Autowired
	private RequestHistoryDao historyDao;
	
	@Autowired
	private UploadCertificateService uploadCerService;
	
	@Autowired
	private AuditLogService auditLogService;
	
	@Autowired 
	private EmailService emailService;
	
	public CommonMessage<String> upLoadCertificateByCk(CertificateVo certificateVo) {
		Date currentDate = new Date();
		CommonMessage<String> msg = new CommonMessage<String>();
		RequestForm req = null;
		UserDetails user = null;
		String folder = PATH_UPLOAD;
		String certificates = "";
		upload.createFolder(folder); // Create Folder

		try {
			req = checkReqDetailDao.findReqFormById(certificateVo.getId(), false);
			user = UserLoginUtils.getCurrentUserLogin();
			if (BeanUtils.isNotEmpty(certificateVo.getCertificatesFile())) {
				// set_name				
				certificates = "CERTIFICATE_" + req.getTmbRequestNo() + ".pdf";
				upload.createFile(certificateVo.getCertificatesFile().getBytes(), folder, certificates);
			}
			req.setCertificateFile(certificates);
			req.setReqFormId(certificateVo.getId());
			req.setStatus(StatusConstant.SUCCEED);
			if (certificateDao.upDateCertificateByCk(req) == true) {
				historyDao.save(req);
//				call webservice
				if (StringUtils.isNotBlank(req.getCaNumber())) {
					uploadCerService.uploadEcertificate(certificateVo.getId(),user.getUserId());
				}
//				SEND EMAIL 
				emailService.sendEmailSendLinkForDownload(req.getCompanyName(), req.getCustomerName(), req.getTmbRequestNo());
				msg.setMessage("SUCCESS");
			} else {
				msg.setMessage("PRESS_UPLOAD_RECIEPTTAX");
			}
			return msg;
		} catch (Exception e) {
//			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS, e.toString());
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_PRINT_UPLOADCERTIFICATE, e.toString());
			e.printStackTrace();
			msg.setMessage("Error");
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.UPLOADCERTIFICATE_CODE, ACTION_AUDITLOG_DESC.UPLOADCERTIFICATE,
					(req != null ? req.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}

		return msg;

	}

}
