package com.tmb.ecert.requestorform.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000CerVo;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;

import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class RequestorFormService {

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	private static String PATH = "requestor/";

	@Autowired
	private UploadService upload;

	@Autowired
	private DownloadService download;

	@Autowired
	private RequestorDao dao;

	@Autowired
	private CheckRequestDetailDao daoCrs;

	@Autowired
	private RequestGenKeyService gen;

	public CommonMessage<String> update(Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getUsername();
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
				Long nextId = form.getReqFormId();
				Type listType = new TypeToken<List<Nrq02000CerVo>>() {
				}.getType();
				List<Nrq02000CerVo> cers = new Gson().fromJson(form.getCertificates(), listType);
				RequestForm req = new RequestForm();
				req.setReqFormId(form.getReqFormId());
				req.setAccountName(form.getAccName());
				req.setAccountNo(form.getAccNo());
				req.setTmbRequestNo(form.getTmbReqFormNo());
				req.setAccountType(form.getAccountType());
				req.setAddress(form.getAddress());
				req.setBranch("");
				req.setCaNumber(form.getAcceptNo());
				req.setCertificateFile("");
				req.setCerTypeCode(form.getReqTypeSelect());
				req.setChangeNameFile(
						BeanUtils.isNotEmpty(form.getChangeNameFile()) ? form.getChangeNameFile().getOriginalFilename()
								: null);
				req.setTranCode(form.getTranCode());
				req.setCreatedById(userId);
				req.setCreatedByName(userName);
				req.setCreatedDateTime(null);
				req.setCustomerName(form.getCorpName());
				if (form.getTmbReceiptChk() != null) {
					req.setCustomerNameReceipt(form.getCorpName1());
					req.setCompanyName(form.getCorpName());
				} else {
					req.setCustomerNameReceipt("");
					req.setCompanyName("");
				}
				req.setCustsegmentCode(form.getCustomSegSelect());
				req.setDebitAccountType(form.getSubAccMethodSelect());
				req.setDepartment(form.getDepartmentName());
				req.setGlType(form.getGlType());
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
				req.setStatus("10001");
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				dao.update(req); // SAVE REQUEST FORM
				for (Nrq02000CerVo cer : cers) {
					if (cer.getCheck()) {
						RequestCertificate cert = new RequestCertificate();
						cert.setReqFormId(nextId);
						cert.setCertificateCode(cer.getCode());
						cert.setTotalNumber(cer.getValue());
						cert.setCreatedById(userId);
						cert.setCreatedByName(userName);
						cert.setRegisteredDate(cer.getRegisteredDate());
						cert.setStatementYear(cer.getStatementYear());
						cert.setAcceptedDate(cer.getAcceptedDate());
						cert.setOther(cer.getOther());
						dao.saveCertificates(cert); // SAVE REQUEST CERTIFICATES
					}
				}
				msg.setMessage("SUCCESS");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
			msg.setMessage("ERROR");
			return msg;
		}
	}

	public CommonMessage<String> save(Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getUsername();
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
				RequestForm req = new RequestForm();
				req.setReqFormId(form.getReqFormId());
				req.setAccountName(form.getAccName());
				req.setAccountNo(form.getAccNo());
				req.setTmbRequestNo(form.getTmbReqFormNo());
				req.setAccountType(form.getAccountType());
				req.setAddress(form.getAddress());
				req.setBranch("");
				req.setCaNumber(form.getAcceptNo());
				req.setCertificateFile("");
				req.setCerTypeCode(form.getReqTypeSelect());
				req.setChangeNameFile(
						BeanUtils.isNotEmpty(form.getChangeNameFile()) ? form.getChangeNameFile().getOriginalFilename()
								: null);
				req.setTranCode(form.getTranCode());
				req.setCreatedById(userId);
				req.setCreatedByName(userName);
				req.setCreatedDateTime(null);
				req.setCustomerName(form.getCorpName());
				if (form.getTmbReceiptChk() != null) {
					req.setCustomerNameReceipt(form.getCorpName1());
					req.setCompanyName(form.getCorpName());
				} else {
					req.setCustomerNameReceipt("");
					req.setCompanyName("");
				}
				req.setCustsegmentCode(form.getCustomSegSelect());
				req.setDebitAccountType(form.getSubAccMethodSelect());
				req.setDepartment(form.getDepartmentName());
				req.setGlType(form.getGlType());
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
				req.setStatus("10001");
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				nextId = dao.save(req); // SAVE REQUEST FORM
				for (Nrq02000CerVo cer : cers) {
					if (cer.getCheck()) {
						RequestCertificate cert = new RequestCertificate();
						cert.setReqFormId(nextId);
						cert.setCertificateCode(cer.getCode());
						cert.setTotalNumber(cer.getValue());
						cert.setCreatedById(userId);
						cert.setCreatedByName(userName);
						cert.setRegisteredDate(cer.getRegisteredDate());
						cert.setStatementYear(cer.getStatementYear());
						cert.setAcceptedDate(cer.getAcceptedDate());
						cert.setOther(cer.getOther());
						dao.saveCertificates(cert); // SAVE REQUEST CERTIFICATES
					}
				}
				msg.setMessage("SUCCESS");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		} catch (IOException e) {
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

	public CommonMessage<String> saveBySelf() {
		String reqTmbNo = gen.getNextKey();
		CommonMessage<String> msg = new CommonMessage<String>();
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getUsername();
		try {
			RequestForm req = new RequestForm();
			req.setAccountName(null);
			req.setAccountNo(null);
			req.setTmbRequestNo(reqTmbNo);
			req.setAccountType(null);
			req.setAddress(null);
			req.setBranch(null);
			req.setCaNumber(null);
			req.setCertificateFile(null);
			req.setCerTypeCode(null);
			req.setChangeNameFile(null);
			req.setCompanyName(null);
			req.setCreatedById(userId);
			req.setCreatedByName(userName);
			req.setCreatedDateTime(null);
			req.setCustomerName(null);
			req.setCustomerNameReceipt(null);
			req.setCustsegmentCode(null);
			req.setDebitAccountType(null);
			req.setDepartment(null);
			req.setGlType(null);
			req.setIdCardFile(null);
			req.setMakerById(userId);
			req.setMakerByName(userName);
			req.setOrganizeId(null);
			req.setPaidTypeCode(null);
			req.setRequestDate(null);
			req.setRequestFormFile(null);
			req.setStatus("10011");
			req.setRemark(null);
			req.setTelephone(null);
			dao.save(req); // SAVE REQUEST FORM
			msg.setData(reqTmbNo);
			msg.setMessage("SUCCESS");
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			msg.setData(null);
			msg.setMessage("ERROR");
			return msg;
		}
	}

	public List<RequestForm> findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = daoCrs.findReqFormById(reqFormId);
		return reqForm;
	}

}