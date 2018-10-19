package com.tmb.ecert.checkrequeststatus.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentRequest;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.service.PaymentWebService;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;

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

	public List<Certificate> findCertListByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		String cerTypeCode = dao.findReqFormById(reqFormId, false).get(0).getCerTypeCode();
		logger.info(cerTypeCode);
		return dao.findCerByCerTypeCode(cerTypeCode);
	}

	public List<RequestCertificate> findCertByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestCertificate> reqForm = dao.findCertByReqFormId(reqFormId);
		return reqForm;
	}

	public List<RequestForm> findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = dao.findReqFormById(reqFormId);
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

	public CommonMessage<String> reject(RequestForm req) {
		CommonMessage<String> commonMsg = new CommonMessage<String>();
		try {
			RequestForm newReq = dao.findReqFormById(req.getReqFormId(), false).get(0);
			newReq.setRejectReasonCode(req.getRejectReasonCode());
			newReq.setRejectReasonOther(req.getRejectReasonOther());
			newReq.setStatus("10003");
			reqDao.update(newReq);
			hstDao.save(newReq);
			commonMsg.setMessage("SUCCESS");
			return commonMsg;
		} catch (Exception e) {
			e.printStackTrace();
			commonMsg.setMessage("ERROR");
			return commonMsg;
		}
	}

	public CommonMessage<RealtimePaymentRequest> approve(String reqFormId) {
		logger.info("CheckRequestDetailService::approve");
		Long id = Long.valueOf(reqFormId);
		RequestForm newReq = dao.findReqFormById(id, false).size() > 0 ? dao.findReqFormById(id, false).get(0)
				: new RequestForm();
		logger.info("CheckRequestDetailService::approve PAYMENT_TYPE => {}", newReq.getPaidTypeCode());
		CommonMessage<RealtimePaymentRequest> response = new CommonMessage<RealtimePaymentRequest>();
		newReq.setStatus("10009");
		reqDao.update(newReq);
		hstDao.save(newReq);
		response.setMessage("SUCCESS");
		return response;
		/*switch (newReq.getPaidTypeCode()) {
			case "30001":
				CommonMessage<FeePaymentRequest> responseFeeTmb = paymentWs.feePayment(newReq,"TMB");
				if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseFeeTmb.getMessage())) {
					
					CommonMessage<ApproveBeforePayRequest> responseApproveBefore = paymentWs.approveBeforePayment(newReq);
					if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseApproveBefore.getMessage())) {
						
						CommonMessage<FeePaymentRequest> responseFeeDbd = paymentWs.feePayment(newReq,"DBD");
						if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseFeeDbd.getMessage())) {
							
							CommonMessage<RealtimePaymentRequest> responseRealtime = paymentWs.realtimePayment(newReq);
							if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseRealtime.getMessage())) {
								return responseRealtime;
							} else {
								return responseRealtime;
							}
							
						} else {
							return null;
						}
						
					} else {
						return null;
					}
					
				} else {
					return null;
				}
			case "30002":
				CommonMessage<ApproveBeforePayRequest> responseApproveBefore = paymentWs.approveBeforePayment(newReq);
				if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseApproveBefore.getMessage())) {
					
					CommonMessage<FeePaymentRequest> responseFeeDbd = paymentWs.feePayment(newReq,"DBD");
					if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseFeeDbd.getMessage())) {
						
						CommonMessage<RealtimePaymentRequest> responseRealtime = paymentWs.realtimePayment(newReq);
						if (StatusConstant.PAYMENT_STATUS.SUCCESS_MSG.equals(responseRealtime.getMessage())) {
							return responseRealtime;
						} else {
							return responseRealtime;
						}
						
					} else {
						return null;
					}
					
				} else {
					return null;
				}
			default:
					return null;
		}*/
	}

}
