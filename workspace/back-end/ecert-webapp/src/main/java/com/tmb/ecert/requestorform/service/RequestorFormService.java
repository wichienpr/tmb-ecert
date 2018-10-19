package com.tmb.ecert.requestorform.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000CerVo;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;

import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class RequestorFormService {

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	private static String PATH = "tmb-requestor/";

	@Autowired
	private UploadService upload;

	@Autowired
	private DownloadService download;

	@Autowired
	private RequestorDao dao;

	@Autowired
	private CheckRequestDetailDao daoCrs;
	
	@Autowired
	private RequestHistoryDao daoHst;

	@Autowired
	private RequestGenKeyService gen;

	public CommonMessage<String> update(Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		RequestForm req = daoCrs.findReqFormById(form.getReqFormId(), false);
		if ("10005".equals(form.getStatus())) {
			if (req.getMakerById() != null) {
				msg.setData("HASMAKER");
				msg.setMessage("ERROR");
				return msg;
			}
			if ("false".equals(form.getHasAuthed())) {
				if (form.getAmount().doubleValue() > Double.parseDouble(ApplicationCache.getParamValueByName("payment.amount.limit"))) {
					msg.setData("NEEDLOGIN");
					msg.setMessage("ERROR");
					return msg;
				}
			}
		}
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getUsername();
		String folder = PATH;
		Long nextId = form.getReqFormId();

		String requestFileName = form.getRequestFileName();
		String copyFile = form.getCopyFileName();
		String changeNameFile = form.getChangeNameFileName();
		upload.createFolder(folder); // Create Folder
		try {
			if (BeanUtils.isNotEmpty(form.getRequestFile())) {
				String ext = FilenameUtils.getExtension(form.getRequestFile().getOriginalFilename());
				requestFileName = "RECEIPT_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getRequestFile().getBytes(), folder, requestFileName);
			}
			if (BeanUtils.isNotEmpty(form.getCopyFile())) {
				String ext = FilenameUtils.getExtension(form.getCopyFile().getOriginalFilename());
				copyFile = "IDCARD_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getCopyFile().getBytes(), folder, copyFile);
			}
			if (BeanUtils.isNotEmpty(form.getChangeNameFile())) {
				String ext = FilenameUtils.getExtension(form.getChangeNameFile().getOriginalFilename());
				changeNameFile = "NCHANGE_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getChangeNameFile().getBytes(), folder, changeNameFile);
			}
			try {
				Type listType = new TypeToken<List<Nrq02000CerVo>>() {
				}.getType();
				List<Nrq02000CerVo> cers = new Gson().fromJson(form.getCertificates(), listType);
				req.setRejectReasonCode(form.getRejectReasonCode());
				req.setRejectReasonOther(form.getRejectReasonOther());
				req.setRef1(form.getRef1());
				req.setRef2(form.getRef2());
				req.setAmountTmb(form.getAmountTmb());
				req.setAmountDbd(form.getAmountDbd());
				req.setAmount(form.getAmount());
				req.setReqFormId(nextId);
				req.setAccountName(form.getAccName());
				req.setAccountNo(form.getAccNo());
				req.setTmbRequestNo(form.getTmbReqFormNo());
				req.setAccountType(form.getAccountType());
				req.setAddress(form.getAddress());
				req.setBranch("");
				req.setCaNumber(form.getAcceptNo());
				req.setCertificateFile("");
				req.setCerTypeCode(form.getReqTypeSelect());
				req.setChangeNameFile(changeNameFile);
				req.setStatus(form.getStatus());
				req.setTranCode(form.getTranCode());
				req.setCustomerName(form.getCorpName());
				req.setCustomerNameReceipt(form.getCorpName1());
				req.setCompanyName(form.getCorpName());
				req.setCustsegmentCode(form.getCustomSegSelect());
				req.setDebitAccountType(form.getSubAccMethodSelect());
				req.setDepartment(form.getDepartmentName());
				req.setGlType(form.getGlType());
				req.setIdCardFile(copyFile);
				if ("MAKER".equals(form.getUserStatus())) {
					req.setMakerById(userId);
					req.setMakerByName(userName);
				}
				req.setOrganizeId(form.getCorpNo());
				req.setPaidTypeCode(form.getPayMethodSelect());
				req.setRequestFormFile(requestFileName);
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				try {
					dao.update(req); // SAVE REQUEST FORM
				} catch (Exception e) {
					logger.error("REQUESTFORM UPDATE => ", e);
					msg.setMessage("ERROR");
					return msg;
				}
				try {
					daoHst.save(req); // ADD HISTORY
				} catch (Exception e) {
					logger.error("REQUESTFORM ADD HISTORY STATUS({}) => {}", req.getStatus(), e);
					msg.setMessage("ERROR");
					return msg;
				}
				for (Nrq02000CerVo cer : cers) {
					if (cer.getCheck()) {
						RequestCertificate cert = new RequestCertificate();
						cert.setReqCertificateId(cer.getReqcertificateId());
						cert.setReqFormId(nextId);
						cert.setCertificateCode(cer.getCode());
						cert.setTotalNumber(cer.getValue());
						cert.setCreatedById(userId);
						cert.setCreatedByName(userName);
						cert.setRegisteredDate(cer.getRegisteredDate());
						cert.setStatementYear(cer.getStatementYear());
						cert.setAcceptedDate(cer.getAcceptedDate());
						cert.setOther(cer.getOther());
						if (cer.getReqcertificateId() == null) {
							dao.saveCertificates(cert); // SAVE REQUEST CERTIFICATES
						} else {
							dao.updateCertificates(cert); // UPDATE REQUEST CERTIFICATES
						}
					}
				}
				msg.setMessage("SUCCESS");
			} catch (Exception e) {
				logger.error("REQUESTFORM UPDATE => ", e);
			}
			return msg;
		} catch (IOException e) {
			logger.error("REQUESTFORM UPDATE => ", e);
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
				String ext = FilenameUtils.getExtension(form.getRequestFile().getOriginalFilename());
				requestFileName = "RECEIPT_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getRequestFile().getBytes(), folder, requestFileName);
			}
			if (BeanUtils.isNotEmpty(form.getCopyFile())) {
				String ext = FilenameUtils.getExtension(form.getCopyFile().getOriginalFilename());
				copyFile = "IDCARD_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getCopyFile().getBytes(), folder, copyFile);
			}
			if (BeanUtils.isNotEmpty(form.getChangeNameFile())) {
				String ext = FilenameUtils.getExtension(form.getChangeNameFile().getOriginalFilename());
				changeNameFile = "NCHANGE_" + form.getTmbReqFormNo() + "." + ext;
				upload.createFile(form.getChangeNameFile().getBytes(), folder, changeNameFile);
			}
			try {
				Long nextId = 0L;
				Type listType = new TypeToken<List<Nrq02000CerVo>>() {
				}.getType();
				List<Nrq02000CerVo> cers = new Gson().fromJson(form.getCertificates(), listType);
				RequestForm req = new RequestForm();
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
				req.setChangeNameFile(BeanUtils.isNotEmpty(form.getChangeNameFile()) ? changeNameFile : null);
				req.setTranCode(form.getTranCode());
				req.setCreatedById(userId);
				req.setCreatedByName(userName);
				req.setCreatedDateTime(timestamp);
				req.setCustomerName(form.getCorpName());
				req.setCustomerNameReceipt(form.getCorpName1());
				req.setCompanyName(form.getCorpName());
				req.setCustsegmentCode(form.getCustomSegSelect());
				req.setDebitAccountType(form.getSubAccMethodSelect());
				req.setDepartment(form.getDepartmentName());
				req.setGlType(form.getGlType());
				req.setIdCardFile(BeanUtils.isNotEmpty(form.getCopyFile()) ? copyFile : null);
				req.setMakerById(userId);
				req.setMakerByName(userName);
				req.setOrganizeId(form.getCorpNo());
				req.setPaidTypeCode(form.getPayMethodSelect());
				req.setRequestDate(timestamp);
				req.setRequestFormFile(BeanUtils.isNotEmpty(form.getRequestFile()) ? requestFileName : null);
				req.setStatus("10001");
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				try {
					nextId = dao.save(req); // SAVE REQUEST FORM
				} catch (Exception e) {
					logger.info("REQUESTFORM SAVE => ", e);
					msg.setMessage("ERROR");
					return msg;
				}
				try {
					req.setReqFormId(nextId);
					daoHst.save(req); // ADD HISTORY
				} catch (Exception e) {
					logger.info("REQUESTFORM ADD HISTORY STATUS({}) => {}", req.getStatus(), e);
					msg.setMessage("ERROR");
					return msg;
				}
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
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
			req.setCreatedDateTime(timestamp);
			req.setCustomerName(null);
			req.setCustomerNameReceipt(null);
			req.setCustsegmentCode(null);
			req.setDebitAccountType(null);
			req.setDepartment(null);
			req.setGlType(null);
			req.setIdCardFile(null);
			req.setMakerById(null);
			req.setMakerByName(null);
			req.setOrganizeId(null);
			req.setPaidTypeCode(null);
			req.setRequestDate(timestamp);
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

	public RequestForm findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		RequestForm reqForm = daoCrs.findReqFormById(reqFormId);
		return reqForm;
	}

}
