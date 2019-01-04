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
import com.tmb.ecert.common.constant.ProjectConstant.WEB_SERVICE_PARAMS;
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
import com.tmb.ecert.requestorform.service.ReceiptGenKeyService;

import th.co.baiwa.buckwaframework.security.constant.ADConstant;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class CheckRequestDetailService {

	private static String PATH = "";

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
	
	@Autowired
	private ReceiptGenKeyService receiptGenKeyService;

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
			newReq.setStatus(req.getStatus());

			// Get Role of user login
			// StringBuilder roleName = new StringBuilder();
			newReq.setUpdatedById(user.getUserId());
			newReq.setUpdatedByName(user.getFirstName().concat(" " + user.getLastName()));

			if (StatusConstant.WAIT_PAYMENT_APPROVAL.equals(newReq.getStatus())) {
				rejectStatusAction = ACTION_AUDITLOG.REJECT_PAYMENT_CODE;
				rejectDesAction = ACTION_AUDITLOG_DESC.REJECT_PAYMENT;
				if (UserLoginUtils.ishasRoleName(user, ADConstant.ROLE_CHECKER)) {
					// UserLoginUtils.ishasRole(user, ROLES.APPROVER)
					newReq.setCheckerById(user.getUserId());
					newReq.setCheckerByName(user.getFirstName().concat(" " + user.getLastName()));
				}
				newReq.setStatus(StatusConstant.REJECT_PAYMENT);
			} else if (StatusConstant.NEW_REQUEST.equals(newReq.getStatus())
					&& UserLoginUtils.ishasRoleName(user, ADConstant.ROLE_REQUESTER)) {
				// UserLoginUtils.ishasRole(user, ROLES.REQUESTER)
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
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS,
					e.toString());
			commonMsg.setMessage("ERROR");
			logger.error("CheckRequestDetailService::reject ERROR => {}", e);
			reqDao.updateErrorDescription("REJECT:" + e.getMessage(), newReq.getReqFormId());
		} finally {
			auditLogService.insertAuditLog(rejectStatusAction, rejectDesAction,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY), user, currentDate);
		}
		return commonMsg;
	}

	public CommonMessage<ResponseVo> approve(String reqFormId, UserDetails user, String authed) {
		logger.info("CheckRequestDetailService::approve REQFORM_ID => {}", reqFormId);
		Date currentDate = new Date();
		Long id = Long.valueOf(reqFormId);
		CommonMessage<ResponseVo> response = new CommonMessage<ResponseVo>();
		RequestForm newReq = new RequestForm();
		try {
			newReq = dao.findReqFormById(id, false);
			newReq.setOfficeCode(user.getOfficeCode());
			logger.info("CheckRequestDetailService::approve PAYMENT_TYPE => {}", newReq.getPaidTypeCode());
			// Checked by SuperChecker
			if ("false".equals(authed)) {
				if (newReq.getAmount() != null) {
					if (newReq.getAmount().doubleValue() > Double
							.parseDouble(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.AMOUNT_LIMIT))) {
						response.setData(new ResponseVo("NEEDLOGIN", "ERROR"));
						response.setMessage("ERROR");
						return response;
					}
				}
			}
			if (UserLoginUtils.ishasRoleName(user, ADConstant.ROLE_CHECKER)) {
				// UserLoginUtils.ishasRole(user, ROLES.CHECKER)
				newReq.setCheckerById(user.getUserId());
				newReq.setCheckerByName(user.getFirstName().concat(" " + user.getLastName()));
			}
			if (PAYMENT_STATUS.PAY_NONE.equalsIgnoreCase(newReq.getPaidTypeCode())) {
				newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
				updateForm(newReq, user);
				response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			} else {
				CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePayment(newReq);
				if (isSuccess(approveStep.getMessage())) {

					CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePayment(newReq);
					if (isSuccess(dbdStep.getMessage())) {

						// UUID => TransactionNo
						newReq.setUuid(dbdStep.getData().getUuid());

						CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePayment(newReq);
						newReq.setPayLoadTs(realtimeStep.getData().getPayLoadTs()); // UPDATE PAY_LOAD_TS
						if (isSuccess(realtimeStep.getMessage())) {
							
							// UPDATE RECEIPT ID WHEN PAYMENT SUCCESS.
							if (StringUtils.isEmpty(newReq.getReceiptNo())) {
								String receiptNo = receiptGenKeyService.getNextKey();
								newReq.setReceiptNo(receiptNo);
							}
							
							newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
							updateForm(newReq, user);
							response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);

						} else {
							response.setData(new ResponseVo(realtimeStep.getData().getDescription(),
									realtimeStep.getData().getStatusCode()));
							response = handlerErrorReq(response, newReq, user);
							throw new Exception(
									response.getData().getStatus() + " : " + response.getData().getMessage());
						}
					} else {
						response.setData(
								new ResponseVo(dbdStep.getData().getDescription(), dbdStep.getData().getStatusCode()));
						response = handlerErrorReq(response, newReq, user);
						throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
					}
				} else {
					response.setData(new ResponseVo(approveStep.getData().getDescription(),
							approveStep.getData().getStatusCode()));
					//response = handlerErrorReq(response, newReq, user); 
					throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
				}
			}
			logger.info("CheckRequestDetailService::approve finished...");
		} catch (Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_UPDATE_STATUS,
					e.toString());
			reqDao.updateErrorDescription(e.getMessage(), newReq.getReqFormId());
			response = handlerErrorReq(response, newReq, user);
			logger.error(e.getMessage());
		} finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.APPROVE_PAYMENT_CODE, ACTION_AUDITLOG_DESC.APPROVE_PAYMENT,
					(newReq != null ? newReq.getTmbRequestNo() : StringUtils.EMPTY),
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), currentDate);
		}
		return response;
	}
	
	public CommonMessage<ResponseVo> retryPayment(String reqFormId, UserDetails user) {
		CommonMessage<ResponseVo> messageRes = new CommonMessage<>();
		try {
			RequestForm req  = dao.findReqFormById(Long.valueOf(reqFormId), false);
			req.setStatus(StatusConstant.WAIT_PAYMENT_APPROVAL);
			
			reqDao.update(req);
//			hstDao.save(req);
			
			messageRes.setMessage("SUCCESS");
		} catch (Exception e) {
			messageRes.setMessage("ERROR");
		}
	
		return messageRes;
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

	private CommonMessage<ResponseVo> handlerErrorReq(CommonMessage<ResponseVo> msg, RequestForm req,
			UserDetails user) {
		req.setStatus(StatusConstant.PAYMENT_FAILED);
		msg.setMessage(PAYMENT_STATUS.ERROR_MSG);
		updateForm(req, user);
		return msg;
	}

}
