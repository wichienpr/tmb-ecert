package com.tmb.ecert.requestorform.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000CerVo;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class RequestorFormService {

	private static final Logger logger = LoggerFactory.getLogger(RequestorFormService.class);
	private static String PATH = "requestor/";

	@Autowired
	private UploadService upload;

	@Autowired
	private DownloadService download;

	@Autowired
	private RequestorDao dao;

	public CommonMessage<String> save(Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		String userId = "000";
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
			try {
				Long nextId = 0L;
				Type listType = new TypeToken<List<Nrq02000CerVo>>() {
				}.getType();
				List<Nrq02000CerVo> cers = new Gson().fromJson(form.getCertificates(), listType);
				for (Nrq02000CerVo cer : cers) {
					if (cer.getCheck()) {
						RequestForm req = new RequestForm();
						req.setAccountName(form.getAccName());
						req.setAccountNo(form.getAccNo());
						req.setAccountType("OOA");
						req.setAddress(form.getAddress());
						req.setBranch("");
						req.setCaNumber(form.getAcceptNo());
						req.setCertificateFile("");
						req.setCerTypeCode(cer.getCode());
						req.setChangeNameFile(BeanUtils.isNotEmpty(form.getChangeNameFile())
								? form.getChangeNameFile().getOriginalFilename()
								: null);
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
						req.setIdCardFile(
								BeanUtils.isNotEmpty(form.getCopyFile()) ? form.getCopyFile().getOriginalFilename()
										: null);
						req.setMakerById(userId);
						req.setMakerByName(userName);
						req.setOrganizeId(form.getCorpNo());
						req.setPaidTypeCode(form.getPayMethodSelect());
						req.setRequestDate(null);
						req.setRequestFormFile(BeanUtils.isNotEmpty(form.getRequestFile())
								? form.getRequestFile().getOriginalFilename()
								: null);
						req.setStatus("");
						req.setRemark(form.getNote());
						req.setTelephone(form.getTelReq());
						nextId = dao.save(req); // SAVE REQUEST FORM
						RequestCertificate cert = new RequestCertificate();
						cert.setReqFormId(nextId);
						cert.setCertificateCode(cer.getCode());
						cert.setTotalNumber(cer.getValue());
						cert.setCreatedById(userId);
						cert.setCreatedByName(userName);
						dao.saveCertificates(cert); // SAVE REQUEST CERTIFICATES
					}
				}
				msg.setMessage("SUCCESS");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setMessage("ERROR");
			return msg;
		}
	}
	
	public List<RequestForm> findAll() {
		return dao.findAll();
	}
	
	public List<RequestCertificate> findCertByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestCertificate> reqForm = dao.findCertByReqFormId(reqFormId);
		return reqForm;
	}
	
	public List<RequestForm> findById(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = dao.findById(reqFormId);
		if (reqForm.size()>0) {
			RequestForm req = reqForm.get(0);
			req.setStatus(ApplicationCache.getLovByCode(req.getStatus()).getName());
			req.setCerTypeCode(ApplicationCache.getCerByCode(req.getCerTypeCode()).getTypeDesc());
			req.setPaidTypeCode(ApplicationCache.getLovByCode(req.getPaidTypeCode()).getName());
			req.setCustsegmentCode(ApplicationCache.getLovByCode(req.getCustsegmentCode()).getName());
			reqForm.set(0, req);
		}
		return reqForm;
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
