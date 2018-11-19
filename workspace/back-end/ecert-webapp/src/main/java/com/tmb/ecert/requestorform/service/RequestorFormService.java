package com.tmb.ecert.requestorform.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.WEB_SERVICE_PARAMS;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.service.UploadService;
import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000CerVo;
import com.tmb.ecert.requestorform.persistence.vo.Nrq02000FormVo;
import com.tmb.ecert.requestorform.persistence.vo.ReqUser;

import th.co.baiwa.buckwaframework.security.constant.ADConstant;
import th.co.baiwa.buckwaframework.security.domain.TMBPerson;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.provider.TMBLDAPManager;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class RequestorFormService {

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	private static String PATH = "";

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
	private AuditLogService auditLogService;

	@Autowired
	private RequestGenKeyService gen;

	@Autowired
	private EmailService emailSerivce;
	
	@Autowired
	private TMBLDAPManager ldap;

	public CommonMessage<String> pageActive(RequestForm vo) {
		CommonMessage<String> msg = new CommonMessage<String>();
		try {
			String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
			String userName = UserLoginUtils.getCurrentUserLogin().getFirstName()
					.concat(" " + UserLoginUtils.getCurrentUserLogin().getLastName());
			vo.setMakerById(userId);
			vo.setMakerByName(userName);
			vo.setUpdatedById(userId);
			vo.setUpdatedByName(userName);
			dao.updateLockStatus(vo);
			msg.setMessage("SUCCESS");
		} catch (Exception e) {
			msg.setData(e.getMessage());
			msg.setMessage("ERROR");
			e.printStackTrace();
		}
		return msg;
	}

	public CommonMessage<String> update(Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		RequestForm req = daoCrs.findReqFormById(form.getReqFormId(), false);
		if ("10005".equals(form.getStatus())) {
			if (req.getMakerById() != null) {
				if (!req.getMakerById().equalsIgnoreCase(UserLoginUtils.getCurrentUserLogin().getUserId())) {
					msg.setData("HASMAKER");
					msg.setMessage("ERROR");
					return msg;
				}
			}
			if ("false".equals(form.getHasAuthed())) {
				if (form.getAmount() != null) {
					if (form.getAmount().doubleValue() > Double
							.parseDouble(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.AMOUNT_LIMIT))) {
						msg.setData("NEEDLOGIN");
						msg.setMessage("ERROR");
						return msg;
					}
				}
			}
		}
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getFirstName()
				.concat(" " + UserLoginUtils.getCurrentUserLogin().getLastName());
		String branchCode = UserLoginUtils.getCurrentUserLogin().getBranchCode();
		String folder = PATH;
		Long nextId = form.getReqFormId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String requestFileName = form.getRequestFileName();
		String copyFile = form.getCopyFileName();
		String changeNameFile = form.getChangeNameFileName();
		upload.createFolder(folder); // Create Folder
		try {
			if (BeanUtils.isNotEmpty(form.getRequestFile())) {
				String ext = FilenameUtils.getExtension(form.getRequestFile().getOriginalFilename());
				requestFileName = "REQFORM_" + form.getTmbReqFormNo() + "." + ext;
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
				req.setPaymentBranchCode(branchCode);
				req.setCaNumber(form.getAcceptNo());
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
				if (ADConstant.ROLE_MAKER.equals(form.getUserStatus())) {
					req.setMakerById(userId);
					req.setMakerByName(userName);
					req.setPaymentDate(timestamp);
				}
				req.setUpdatedById(userId);
				req.setUpdatedByName(userName);
				req.setOrganizeId(form.getCorpNo());
				req.setPaidTypeCode(form.getPayMethodSelect());
				req.setRequestFormFile(requestFileName);
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				req.setLockFlag(0);
				try {
					dao.update(req); // SAVE REQUEST FORM
					// CHECK FOR SEND EMAIL
					String fullName = UserLoginUtils.getCurrentUserLogin().getFirstName() + " "
							+ UserLoginUtils.getCurrentUserLogin().getLastName();
					if (StatusConstant.WAIT_PAYMENT_APPROVAL.equals(req.getStatus())) {
						emailSerivce.sendEmailPaymentOrder(req.getCompanyName(), req.getTmbRequestNo(), fullName);
					}
				} catch (Exception e) {
					emailSerivce.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS, e.toString());
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
				List<RequestCertificate> forUpdateCerts = new ArrayList<>();
				for (Nrq02000CerVo cer : cers) {
					if (cer.getCheck()) {
						RequestCertificate cert = new RequestCertificate();
						cert.setReqCertificateId(cer.getReqcertificateId());
						cert.setReqFormId(nextId);
						cert.setCertificateCode(cer.getCode());
						cert.setTotalNumber(cer.getValue());
						cert.setRegisteredDate(cer.getRegisteredDate());
						cert.setStatementYear(cer.getStatementYear());
						cert.setAcceptedDate(cer.getAcceptedDate());
						cert.setOther(cer.getOther());
						if (cer.getReqcertificateId() == null) {
							cert.setCreatedById(userId);
							cert.setCreatedByName(userName);
							dao.saveCertificates(cert); // SAVE REQUEST CERTIFICATES
						} else {
							cert.setUpdateById(userId);
							cert.setUpdateByName(userName);
							forUpdateCerts.add(cert);
							dao.updateCertificates(cert); // UPDATE REQUEST CERTIFICATES
						}
					} else {
						if (cer.getReqcertificateId() != null) {
							dao.deleteCertificates(cer.getReqcertificateId()); // DELETE REQUEST CERTIFICATES
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
		String userName = UserLoginUtils.getCurrentUserLogin().getFirstName()
				.concat(" " + UserLoginUtils.getCurrentUserLogin().getLastName());
		String branchCode = UserLoginUtils.getCurrentUserLogin().getBranchCode();
		String folder = PATH;

		String requestFileName = "";
		String copyFile = "";
		String changeNameFile = "";
		upload.createFolder(folder); // Create Folder
		try {
			if (BeanUtils.isNotEmpty(form.getRequestFile())) {
				String ext = FilenameUtils.getExtension(form.getRequestFile().getOriginalFilename());
				requestFileName = "REQFORM_" + form.getTmbReqFormNo() + "." + ext;
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
				req.setPaymentBranchCode(branchCode);
				req.setCaNumber(form.getAcceptNo());
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
				req.setOrganizeId(form.getCorpNo());
				req.setPaidTypeCode(form.getPayMethodSelect());
				req.setRequestDate(timestamp);
				req.setRequestFormFile(BeanUtils.isNotEmpty(form.getRequestFile()) ? requestFileName : null);
				req.setStatus("10001");
				req.setLockFlag(0);
				req.setDeleteFlag(0);
				req.setRemark(form.getNote());
				req.setTelephone(form.getTelReq());
				try {
					nextId = dao.save(req); // SAVE REQUEST FORM
				} catch (Exception e) {
					emailSerivce.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_CREATE_REQUESTFORM, e.toString());
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
		String pathName = fileName;
		download.download(pathName, response);
	}

	public void pdf(String name, HttpServletResponse response) {
		String pathName = name;
		download.pdf(pathName, response);
	}

	public CommonMessage<String> saveBySelf() {
		String reqTmbNo = gen.getNextKey();
		CommonMessage<String> msg = new CommonMessage<String>();
		String userId = UserLoginUtils.getCurrentUserLogin().getUserId();
		String userName = UserLoginUtils.getCurrentUserLogin().getFirstName()
				.concat(" " + UserLoginUtils.getCurrentUserLogin().getLastName());
		String branchCode = UserLoginUtils.getCurrentUserLogin().getBranchCode();
		try {
			RequestForm req = new RequestForm();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			req.setTmbRequestNo(reqTmbNo);
			req.setCreatedById(userId);
			req.setCreatedByName(userName);
			req.setCreatedDateTime(timestamp);
			req.setRequestDate(timestamp);
			req.setPaymentBranchCode(branchCode);
			req.setStatus("10011");
			req.setLockFlag(0);
			req.setDeleteFlag(0);
			dao.save(req); // SAVE REQUEST FORM
			msg.setData(reqTmbNo);
			msg.setMessage("SUCCESS");
			return msg;
		} catch (Exception e) {
			emailSerivce.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS, e.toString());
			e.printStackTrace();
			msg.setData(null);
			msg.setMessage("ERROR");
			return msg;
		} finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.REQFORM_01_CODE, ACTION_AUDITLOG_DESC.REQFORM_01, reqTmbNo,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), new Date());
		}
	}

	public RequestForm findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		RequestForm reqForm = daoCrs.findReqFormById(reqFormId, false);
		return reqForm;
	}
	
//	public boolean confirmAD(ReqUser user) {
//		if ("superchecker".equalsIgnoreCase(user.getUsername())&&"password".equalsIgnoreCase(user.getPassword())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	public CommonMessage<String> validate( Nrq02000FormVo form) {
		CommonMessage<String> msg = new CommonMessage<String>();
		int result = daoCrs.checkDuplicate(form);
		if (result > 0) {
			msg.setMessage("DUPLICATE");
		}else {
			msg.setMessage("SUCCESS");
		}
		return msg;
	}
	
	public boolean confirmAD(ReqUser user) {
		boolean isLogged = false;
		try {
			TMBPerson tmb = ldap.isAuthenticate(user.getUsername(), user.getPassword());
			if (ADConstant.ROLE_SUPER.equals(tmb.getMemberOfs().get(0))) {
				isLogged = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isLogged = false;
		}
		return isLogged;
	}

}
