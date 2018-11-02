package com.tmb.ecert.checkrequeststatus.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ResponseVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ApproveBeforePayResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentResponse;
import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.RoleConstant.ROLES;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.StatusConstant.PAYMENT_STATUS;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.ListOfValue;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.AuditLogService;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.EmailService;
import com.tmb.ecert.common.service.PaymentWebService;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

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

	@Autowired
	private EmailService emailService;

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
			// StringBuilder roleName = new StringBuilder();
			newReq.setUpdatedById(user.getUserId());
			newReq.setUpdatedByName(user.getFirstName().concat(" " + user.getLastName()));

			if (StatusConstant.WAIT_PAYMENT_APPROVAL.equals(newReq.getStatus())) {
				rejectStatusAction = ACTION_AUDITLOG.REJECT_PAYMENT_CODE;
				rejectDesAction = ACTION_AUDITLOG_DESC.REJECT_PAYMENT;
				if (UserLoginUtils.ishasRole(user, ROLES.CHECKER)) {
					newReq.setCheckerById(user.getUserId());
					newReq.setCheckerByName(user.getFirstName().concat(" " + user.getLastName()));
				}
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
			// send email reject request
			ListOfValue reason = ApplicationCache.getLovByCode(newReq.getRejectReasonCode());
			emailService.sendEmailRejectPayment(newReq.getCompanyName(), newReq.getCustomerName(),
					newReq.getTmbRequestNo(), reason.getName(), newReq.getRejectReasonOther());
			commonMsg.setMessage("SUCCESS");
			logger.info("CheckRequestDetailService::reject finished...");
		} catch (Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS, e.toString());
			commonMsg.setMessage("ERROR");
			logger.error("CheckRequestDetailService::reject ERROR => {}", e);
			reqDao.updateErrorDescription("REJECT:" + e.getMessage(), newReq.getReqFormId());
		} finally {
			auditLogService.insertAuditLog(rejectStatusAction, rejectDesAction,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY), user, currentDate);
		}
		return commonMsg;
	}

	public CommonMessage<ResponseVo> approve(String reqFormId, UserDetails user) {
		logger.info("CheckRequestDetailService::approve REQFORM_ID => {}", reqFormId);
		Date currentDate = new Date();
		Long id = Long.valueOf(reqFormId);
		CommonMessage<ResponseVo> response = new CommonMessage<ResponseVo>();
		RequestForm newReq = new RequestForm();
		try {
			newReq = dao.findReqFormById(id, false);
			logger.info("CheckRequestDetailService::approve PAYMENT_TYPE => {}", newReq.getPaidTypeCode());
			if (UserLoginUtils.ishasRole(user, ROLES.CHECKER)) {
				newReq.setCheckerById(user.getUserId());
				newReq.setCheckerByName(user.getFirstName().concat(" " + user.getLastName()));
			}
			switch (newReq.getPaidTypeCode()) {
			case PAYMENT_STATUS.PAY_TMB_DBD: // 10001
				CommonMessage<FeePaymentResponse> tmbStep = paymentWs.feePaymentTMB(newReq);
				if (isSuccess(tmbStep.getMessage())) {

					CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePayment(newReq);
					if (isSuccess(approveStep.getMessage())) {

						CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePaymentDBD(newReq);
						if (isSuccess(dbdStep.getMessage())) {

							CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePayment(newReq);
							if (isSuccess(realtimeStep.getMessage())) {

								newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
								updateForm(newReq, user);
								response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);

							} else {
								response.setData(new ResponseVo(realtimeStep.getData().getDescription(), realtimeStep.getData().getStatusCode()));
								response = handlerErrorReq(response, newReq, user);
								throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
							}
						} else {
							response.setData(new ResponseVo(dbdStep.getData().getDescription(), dbdStep.getData().getStatusCode()));
							response = handlerErrorReq(response, newReq, user);
							throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
						}
					} else {
						response.setData(new ResponseVo(approveStep.getData().getDescription(), approveStep.getData().getStatusCode()));
						response = handlerErrorReq(response, newReq, user);
						throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
					}
				} else {
					response.setData(new ResponseVo(tmbStep.getData().getDescription(), tmbStep.getData().getStatusCode()));
					response = handlerErrorReq(response, newReq, user);
					throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
				}
				break;
			case PAYMENT_STATUS.PAY_DBD: // 10002
				CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePayment(newReq);
				if (isSuccess(approveStep.getMessage())) {

					CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePaymentDBD(newReq);
					if (isSuccess(dbdStep.getMessage())) {

						CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePayment(newReq);
						if (isSuccess(realtimeStep.getMessage())) {

							newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
							updateForm(newReq, user);
							response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);

						} else {
							response.setData(new ResponseVo(realtimeStep.getData().getDescription(), realtimeStep.getData().getStatusCode()));
							response = handlerErrorReq(response, newReq, user);
							throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
						}
					} else {
						response.setData(new ResponseVo(dbdStep.getData().getDescription(), dbdStep.getData().getStatusCode()));
						response = handlerErrorReq(response, newReq, user);
						throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
					}
				} else {
					response.setData(new ResponseVo(approveStep.getData().getDescription(), approveStep.getData().getStatusCode()));
					response = handlerErrorReq(response, newReq, user);
					throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
				}
				break;
			case PAYMENT_STATUS.PAY_TMB: // 10003
				CommonMessage<FeePaymentResponse> tmbOnlyStep = paymentWs.feePaymentDBD(newReq);
				if (isSuccess(tmbOnlyStep.getMessage())) {
					newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
					updateForm(newReq, user);
					response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
				} else {
					response.setData(new ResponseVo(tmbOnlyStep.getData().getDescription(), tmbOnlyStep.getData().getStatusCode()));
					response = handlerErrorReq(response, newReq, user);
					throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
				}
				break;
			case PAYMENT_STATUS.PAY_NONE: // 10004
				newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
				updateForm(newReq, user);
				response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
				break;
			}
			logger.info("CheckRequestDetailService::approve finished...");
		} catch (Exception e) {
//			emailService.sendEmailFailFeePayment(newReq, ApplicationCache.getParamValueByName("tmb.servicecode"),
//					new Date(), e.toString());
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS, e.toString());
			reqDao.updateErrorDescription(e.getMessage(), newReq.getReqFormId());
			logger.error(e.getMessage());
		} finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.APPROVE_PAYMENT_CODE, ACTION_AUDITLOG_DESC.APPROVE_PAYMENT,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}
		return response;
	}

	private boolean isSuccess(String value) {
		return PAYMENT_STATUS.SUCCESS_MSG.equals(value);
	}

	private void updateForm(RequestForm req, UserDetails user) {
		req.setUpdatedById(user.getUserId());
		req.setUpdatedByName(user.getFirstName().concat(" " + user.getLastName()));
		reqDao.update(req);
		hstDao.save(req);
	}

	private CommonMessage<ResponseVo> handlerErrorReq(CommonMessage<ResponseVo> msg, RequestForm req, UserDetails user) {
		req.setStatus(StatusConstant.PAYMENT_FAILED);
		msg.setMessage(PAYMENT_STATUS.ERROR_MSG);
		updateForm(req, user);
		return msg;
	}

}
