package com.tmb.ecert.checkrequeststatus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestCertificateDao;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.CertificateVo;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestForm;
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
	
	public CommonMessage<String> upLoadCertificateByCk(CertificateVo certificateVo) {
		CommonMessage<String> msg = new CommonMessage<String>();

		String folder = PATH_UPLOAD;
		String certificates = "";
		upload.createFolder(folder); // Create Folder

		RequestForm req = checkReqDetailDao.findReqFormById(certificateVo.getId(), false).get(0);
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		
		try {
			if (BeanUtils.isNotEmpty(certificateVo.getCertificatesFile())) {
				// set_name				
				certificates = "CERTIFICATE_" + req.getTmbRequestNo() + ".pdf";
				upload.createFile(certificateVo.getCertificatesFile().getBytes(), folder, certificates);
			}
			try {
				req.setCertificateFile(certificates);
				req.setReqFormId(certificateVo.getId());
				req.setStatus(StatusConstant.SUCCEED);
				if (certificateDao.upDateCertificateByCk(req) == true) {
					historyDao.save(req);
//					call webservice
					uploadCerService.uploadEcertificate(certificateVo.getId(),user.getUserId());
					msg.setMessage("SUCCESS");
				} else {
					msg.setMessage("PRESS_UPLOAD_RECIEPTTAX");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMessage("Error");
		}

		return msg;

	}

}
