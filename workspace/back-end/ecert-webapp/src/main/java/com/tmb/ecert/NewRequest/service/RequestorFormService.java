package com.tmb.ecert.NewRequest.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.CheckRequestStatus.persistence.entity.RequestForm;
import com.tmb.ecert.NewRequest.persistence.dao.RequestorDao;
import com.tmb.ecert.NewRequest.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.NewRequest.persistence.vo.Nrq02000Vo;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;

@Service
public class RequestorFormService {

	private static final Logger logger = LoggerFactory.getLogger(RequestorFormService.class);
	private static String PATH = "nrq02000/";

	@Autowired
	private UploadService upload;

	@Autowired
	private DownloadService download;

	@Autowired
	private RequestorDao dao;

	public CommonMessage<Nrq02000Vo> save(Nrq02000FormVo form) {
		CommonMessage<Nrq02000Vo> msg = new CommonMessage<Nrq02000Vo>();
		String userId = "admin";
		String userName = "Administrator";
		String folder = PATH;
		String requestFileName = "";
		String copyFile = "";
		String changeNameFile = "";
		upload.createFolder(folder); // Create Folder
		try {
			if (BeanUtils.isNotEmpty(form.getRequestFile())) {
				requestFileName = form.getRequestFile().getOriginalFilename();
				upload.createFile(form.getRequestFile().getBytes(), folder, requestFileName);
			}
			if (BeanUtils.isNotEmpty(form.getCopyFile())) {
				copyFile = form.getCopyFile().getOriginalFilename();
				upload.createFile(form.getCopyFile().getBytes(), folder, copyFile);
			}
			if (BeanUtils.isNotEmpty(form.getChangeNameFile())) {
				changeNameFile = form.getChangeNameFile().getOriginalFilename();
				upload.createFile(form.getChangeNameFile().getBytes(), folder, changeNameFile);
			}
			RequestForm req = new RequestForm();
			req.setAccountName(form.getAccName());
			req.setAccountNo(form.getAccNo());
			req.setAccountType("OOA");
			req.setAddress(form.getAddress());
			req.setBranch("");
			req.setCaNumber(form.getAcceptNo());
			req.setCertificateFile("");
			req.setCerTypeCode(form.getReqTypeSelect());
			req.setChangeNameFile(
					BeanUtils.isNotEmpty(form.getChangeNameFile()) ? form.getChangeNameFile().getOriginalFilename() : null);
			req.setCompanyName(form.getCorpName());
			req.setCreatedById(userId);
			req.setCreatedByName(userName);
			req.setCreatedDateTime(null);
			req.setCustomerName(form.getCorpName1());
			req.setCustomerNameReceipt(form.getTmbReceiptChk() ? form.getCorpName1() : "");
			req.setCustsegmentCode(form.getCustomSegSelect());
			req.setDebitAccountType(form.getSubAccMethodSelect());
			req.setDepartment(form.getDepartmentName());
			req.setGlType("B533");
			req.setIdCardFile(BeanUtils.isNotEmpty(form.getCopyFile()) ? form.getCopyFile().getOriginalFilename() : null);
			req.setMakerById(userId);
			req.setMakerByName(userName);
			req.setOrganizeId(form.getCorpNo());
			req.setPaidTypeCode(form.getPayMethodSelect());
			req.setRequestDate(null);
			req.setRequestFormFile(
					BeanUtils.isNotEmpty(form.getRequestFile()) ? form.getRequestFile().getOriginalFilename() : null);
			req.setStatus("");
			req.setRemark(form.getNote());
			req.setTelephone(form.getTelReq());
			dao.save(req);
			msg.setMessage("SUCCESS");
			return msg;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setMessage("ERROR");
			return msg;
		}
	}

	public void download(String fileName, HttpServletResponse response) {
		String pathName = PATH + fileName;
		download.download(pathName, response);
	}

	public void pdf(String name, HttpServletResponse response) {
		String pathName = PATH + name;
		download.pdf(pathName, response);
	}

}
