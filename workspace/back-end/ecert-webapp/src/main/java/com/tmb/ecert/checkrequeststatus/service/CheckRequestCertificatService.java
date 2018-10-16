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

@Service
public class CheckRequestCertificatService {

	private static String PATH_UPLOAD = "tmb-requestor/";

	@Autowired
	private UploadService upload;

	@Autowired
	private CheckRequestCertificateDao certificateDao;

	@Autowired
	private CheckRequestDetailDao checkReqDetailDao;
	
	public CommonMessage<String> upLoadCertificateByCk(CertificateVo certificateVo) {
		CommonMessage<String> msg = new CommonMessage<String>();

		String folder = PATH_UPLOAD;
		String certificates = "";
		upload.createFolder(folder); // Create Folder

		try {
			if (BeanUtils.isNotEmpty(certificateVo.getCertificatesFile())) {
				// set_name
				RequestForm reqFrom = checkReqDetailDao.findReqFormById(certificateVo.getId(), false).get(0);				
				certificates = "CERTIFICATE_" + reqFrom.getTmbRequestNo() + ".pdf";
				upload.createFile(certificateVo.getCertificatesFile().getBytes(), folder, certificates);
			}
			try {
				RequestForm req = new RequestForm();
				req.setCertificateFile(certificates);
				req.setReqFormId(certificateVo.getId());
				req.setStatus(StatusConstant.SUCCEED);
				if (certificateDao.upDateCertificateByCk(req) == true) {
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
