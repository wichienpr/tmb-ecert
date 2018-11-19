package com.tmb.ecert.batchjob.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.dao.FeepaymentAutoRetryBatchDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ResponseVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ApproveBeforePayResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentResponse;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.constant.StatusConstant.PAYMENT_STATUS;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.PaymentWebService;
import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;

@Service
@ConditionalOnProperty(name = "job.autoRetryFeePayment.cornexpression", havingValue = "", matchIfMissing = false)
public class FeepaymentAutoRetryBatchService {

	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_AUTOPAYMENT);

	@Autowired
	private PaymentWebService paymentWs;

	@Autowired
	private FeepaymentAutoRetryBatchDao dao;

	@Autowired
	private RequestHistoryDao history;

	public void run() {
		List<RequestForm> forms = dao.findByStatus(StatusConstant.PAYMENT_FAILED);
		for (RequestForm form : forms) {
			retry(form);
		}
	}

	public CommonMessage<ResponseVo> retry(RequestForm newReq) {
		CommonMessage<ResponseVo> response = new CommonMessage<ResponseVo>();
		try {
			if (newReq.getCountPayment() <= 3) {
				response = switchPayment(response, newReq);
			}
//			else {
//				CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePayment(newReq);
//				if (isSuccess(dbdStep.getMessage())) {
//
//					newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
//					response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
//					dao.updateRequestForm(newReq);
//					history.save(newReq);
//
//				} else {
//					newReq.setPaymentStatus("DBDF");
//					response.setData(new ResponseVo(dbdStep.getData().getDescription(),
//							dbdStep.getData().getStatusCode()));
//					response = handlerErrorReq(response, newReq);
//					throw new Exception("DBD => " + response.getData());
//				}
//			}
		} catch (Exception e) {
			int count = 0;
			if (newReq.getCountPayment() != null) {
				count = newReq.getCountPayment();
			}
			log.error(e.getMessage() + " FROM REQ_ID:" + newReq.getReqFormId() + ", COUNT: " + count);
		}
		return response;
	}

	private CommonMessage<ResponseVo> switchPayment(CommonMessage<ResponseVo> response, RequestForm newReq)
			throws Exception {
		if (PAYMENT_STATUS.PAY_NONE.equalsIgnoreCase(newReq.getPaidTypeCode())) {
			newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
			response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			dao.updateRequestForm(newReq);
			history.save(newReq);
		} else {
			CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePayment(newReq);
			if (isSuccess(approveStep.getMessage())) {

				CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePayment(newReq);
				if (isSuccess(dbdStep.getMessage())) {

					// UUID => TransactionNo
					newReq.setUuid(dbdStep.getData().getUuid());

					CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePayment(newReq);
					if (isSuccess(realtimeStep.getMessage())) {

						newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
						response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
						dao.updateRequestForm(newReq);
						history.save(newReq);

					} else {
						response.setData(new ResponseVo(realtimeStep.getData().getDescription(),
								realtimeStep.getData().getStatusCode()));
						response = handlerErrorReq(response, newReq);
						throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
					}
				} else {
					response.setData(
							new ResponseVo(dbdStep.getData().getDescription(), dbdStep.getData().getStatusCode()));
					response = handlerErrorReq(response, newReq);
					throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
				}
			} else {
				response.setData(
						new ResponseVo(approveStep.getData().getDescription(), approveStep.getData().getStatusCode()));
				response = handlerErrorReq(response, newReq);
				throw new Exception(response.getData().getStatus() + " : " + response.getData().getMessage());
			}
		}
		return response;
	}

	private boolean isSuccess(String value) {
		return PAYMENT_STATUS.SUCCESS_MSG.equals(value);
	}

	private CommonMessage<ResponseVo> handlerErrorReq(CommonMessage<ResponseVo> msg, RequestForm req) {
		int count = 0;
		if (req.getCountPayment() != null) {
			count = req.getCountPayment();
		}
		req.setCountPayment(count + 1);
		req.setStatus(StatusConstant.PAYMENT_FAILED);
		dao.updateRequestForm(req);
		msg.setMessage(PAYMENT_STATUS.ERROR_MSG);
		return msg;
	}
}
