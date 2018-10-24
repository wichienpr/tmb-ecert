package com.tmb.ecert.checkrequeststatus.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentRequest;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.RoleConstant;
import com.tmb.ecert.common.constant.RoleConstant.ROLES;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.PaymentWebService;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@Service
public class CheckRequestDetailService {

	private static String PATH = "tmb-requestor/";

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private DownloadService download;

	@Autowired
	private CheckRequestDetailDao dao;

	@Autowired
	private RequestorDao reqDao;

	@Autowired
	private RequestHistoryDao hstDao;

	@Autowired
	private PaymentWebService paymentWs;

	@Autowired
	private AuditLogService auditLogService;

	public List<Certificate> findCertListByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		String cerTypeCode = dao.findReqFormById(reqFormId, false).getCerTypeCode();
		return dao.findCerByCerTypeCode(cerTypeCode);
	}

	public List<RequestCertificate> findCertByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestCertificate> reqForm = dao.findCertByReqFormId(reqFormId);
		return reqForm;
	}

	public RequestForm findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		RequestForm reqForm = dao.findReqFormById(reqFormId);
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

	public CommonMessage<String> reject(RequestForm req, UserDetails user) {
		CommonMessage<String> commonMsg = new CommonMessage<String>();
		Date currentDate = new Date();
		RequestForm newReq = null;
		String rejectStatusAction = null;
		String rejectDesAction = null;
		try {
			logger.info("CheckRequestDetailService::reject REQFORM_ID => {}", req.getReqFormId());
			newReq = dao.findReqFormById(req.getReqFormId(), false);
			newReq.setRejectReasonCode(req.getRejectReasonCode());
			newReq.setRejectReasonOther(req.getRejectReasonOther());

			// Get Role of user login
			StringBuilder roleName = new StringBuilder();
			newReq.setUpdatedById(user.getUserId());
			newReq.setUpdatedByName(user.getUsername());

			if (StatusConstant.WAIT_PAYMENT_APPROVAL.equals(newReq.getStatus())) {
				rejectStatusAction = ACTION_AUDITLOG.REJECT_PAYMENT_CODE;
				rejectDesAction = ACTION_AUDITLOG_DESC.REJECT_PAYMENT;
				newReq.setStatus(StatusConstant.REJECT_PAYMENT);
			} else if (StatusConstant.NEW_REQUEST.equals(newReq.getStatus())
					&& UserLoginUtils.ishasRole(user, ROLES.REQUESTOR)) {
				rejectStatusAction = ACTION_AUDITLOG.CANCEL_REQ_CODE;
				rejectDesAction = ACTION_AUDITLOG_DESC.CANCEL_REQ;
				newReq.setStatus(StatusConstant.CANCEL_REQUEST);
			} else {
				rejectStatusAction = ACTION_AUDITLOG.REJECT_REQ_CODE;
				rejectDesAction = ACTION_AUDITLOG_DESC.REJECT_REQ;
				newReq.setStatus(StatusConstant.REFUSE_REQUEST);

			}
			reqDao.update(newReq);
			hstDao.save(newReq);
			commonMsg.setMessage("SUCCESS");
			logger.info("CheckRequestDetailService::reject finished...");
		} catch (Exception e) {
			commonMsg.setMessage("ERROR");
			logger.error("CheckRequestDetailService::reject ERROR => {}", e);
		} finally {
			auditLogService.insertAuditLog(rejectStatusAction, rejectDesAction,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY), user, currentDate);
		}
		return commonMsg;
	}

	public CommonMessage<String> approve(String reqFormId, UserDetails user) {
		logger.info("CheckRequestDetailService::approve REQFORM_ID => {}", reqFormId);
		Date currentDate = new Date();
		Long id = Long.valueOf(reqFormId);
		CommonMessage<String> response = new CommonMessage<String>();
		RequestForm newReq = new RequestForm();
		try {
			newReq = dao.findReqFormById(id, false);
			logger.info("CheckRequestDetailService::approve PAYMENT_TYPE => {}", newReq.getPaidTypeCode());
			switch (newReq.getPaidTypeCode()) {
			case "30001":
				if (isSuccess(paymentWs.feePayment(newReq, "TMB").getMessage())) {
					if (isSuccess(paymentWs.approveBeforePayment(newReq).getMessage())) {
						if (isSuccess(paymentWs.feePayment(newReq, "DBD").getMessage())) {
							if (isSuccess(paymentWs.realtimePayment(newReq).getMessage())) {
								newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
								updateForm(newReq, user);
								response.setMessage(StatusConstant.PAYMENT_STATUS.SUCCESS_MSG);
							} else {
								handlerErrorReq(response, newReq, user);
								break;
							}
						} else {
							handlerErrorReq(response, newReq, user);
							break;
						}
					} else {
						handlerErrorReq(response, newReq, user);
						break;
					}
				} else {
					handlerErrorReq(response, newReq, user);
					break;
				}
			}
			logger.error("CheckRequestDetailService::approve finished...");
		} catch (Exception e) {
			response.setMessage("ERROR");
		} finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.APPROVE_PAYMENT_CODE, ACTION_AUDITLOG_DESC.APPROVE_PAYMENT,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}
		return response;
	}

	private boolean isSuccess(String value) {
		return StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(value);
	}

	private void updateForm(RequestForm req, UserDetails user) {
		req.setUpdatedById(user.getUserId());
		req.setUpdatedByName(user.getUsername());
		reqDao.update(req);
		hstDao.save(req);
	}
	
	private CommonMessage<String> handlerErrorReq(CommonMessage<String> msg, RequestForm req, UserDetails user) {
		req.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
		updateForm(req, user);
		msg.setMessage(StatusConstant.PAYMENT_STATUS.ERROR_MSG);
		return msg;
	}

}
