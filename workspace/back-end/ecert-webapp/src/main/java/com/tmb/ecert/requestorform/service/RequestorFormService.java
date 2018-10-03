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
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000CerVo;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;

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
		String userId = "admin";
		String userName = "Administrator";
		String folder = PATH;
		String requestFileName = "";
		String copyFile = "";
		String changeNameFile = "";
		Long nextId = 0L;
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
				Type listType = new TypeToken<List<Nrq02000CerVo>>() {}.getType();
				List<Nrq02000CerVo> cers = new Gson().fromJson(form.getCertificates(), listType);
				for(Nrq02000CerVo cer: cers) {
					RequestForm req = new RequestForm();
					req.setAccountName(form.getAccName());
					req.setAccountNo(form.getAccNo());
					req.setAccountType("OOA");
					req.setAddress(form.getAddress());
					req.setBranch("");
					req.setCaNumber(form.getAcceptNo());
					req.setCertificateFile("");
					req.setCerTypeCode(cer.getCode());
					req.setChangeNameFile(
							BeanUtils.isNotEmpty(form.getChangeNameFile()) ? form.getChangeNameFile().getOriginalFilename()
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
							BeanUtils.isNotEmpty(form.getCopyFile()) ? form.getCopyFile().getOriginalFilename() : null);
					req.setMakerById(userId);
					req.setMakerByName(userName);
					req.setOrganizeId(form.getCorpNo());
					req.setPaidTypeCode(form.getPayMethodSelect());
					req.setRequestDate(null);
					req.setRequestFormFile(
							BeanUtils.isNotEmpty(form.getRequestFile()) ? form.getRequestFile().getOriginalFilename()
									: null);
					req.setStatus("");
					req.setRemark(form.getNote());
					req.setTelephone(form.getTelReq());
					nextId = dao.save(req);
					
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

	public void download(String fileName, HttpServletResponse response) {
		String pathName = PATH + fileName;
		download.download(pathName, response);
	}

	public void pdf(String name, HttpServletResponse response) {
		String pathName = PATH + name;
		download.pdf(pathName, response);
	}

}
