package com.tmb.ecert.batchjob.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.BACHJOB_LOG_NAME;
import com.tmb.ecert.batchjob.dao.FeepaymentAutoRetryBatchDao;
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

	public CommonMessage<String> retry(RequestForm newReq) {
		CommonMessage<String> response = new CommonMessage<String>();
		try {
			if (newReq.getCountPayment() <= 3) {
				response = switchPayment(response, newReq);
			} else {
				CommonMessage<FeePaymentResponse> tmbOnlyStep = paymentWs.feePaymentDBDBatch(newReq);
				if (isSuccess(tmbOnlyStep.getMessage())) {
					
					newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
					response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
					dao.updateRequesstForm(newReq);
					history.save(newReq);
					
				} else {
					newReq.setPaymentStatus("DBDF");
					response = handlerErrorReq(response, newReq);
					response.setData(tmbOnlyStep.getData().getDescription());
					throw new Exception("DBD => " + response.getData());
				}
			}
		} catch (Exception e) {
			int count = 0;
			if (newReq.getCountPayment() != null) {
				count = newReq.getCountPayment();
			}
			log.error(e.getMessage() + " FROM REQ_ID:" + newReq.getReqFormId() + ", COUNT: " + count);
		}
		return response;
	}

	private CommonMessage<String> switchPayment(CommonMessage<String> response, RequestForm newReq) throws Exception {
		switch (newReq.getPaidTypeCode()) {
		case PAYMENT_STATUS.PAY_TMB_DBD: // 10001
			CommonMessage<FeePaymentResponse> tmbStep = paymentWs.feePaymentTMBBatch(newReq);
			if (isSuccess(tmbStep.getMessage())) {

				CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePaymentBatch(newReq);
				if (isSuccess(approveStep.getMessage())) {

					CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePaymentDBDBatch(newReq);
					if (isSuccess(dbdStep.getMessage())) {

						CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePaymentBatch(newReq);
						if (isSuccess(realtimeStep.getMessage())) {

							newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
							response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
							dao.updateRequesstForm(newReq);
							history.save(newReq);

						} else {
							newReq.setPaymentStatus("DBDF");
							response = handlerErrorReq(response, newReq);
							response.setData(realtimeStep.getData().getDescription());
							throw new Exception("REALTIME => " + response.getData());
						}
					} else {
						response = handlerErrorReq(response, newReq);
						response.setData(dbdStep.getData().getDescription());
						throw new Exception("DBD => " + response.getData());
					}
				} else {
					response = handlerErrorReq(response, newReq);
					response.setData(approveStep.getData().getDescription());
					throw new Exception("APPROVE => " + response.getData());
				}
			} else {
				response = handlerErrorReq(response, newReq);
				response.setData(tmbStep.getData().getDescription());
				throw new Exception("TMB => " + response.getData());
			}
			break;
		case PAYMENT_STATUS.PAY_DBD: // 10002
			CommonMessage<ApproveBeforePayResponse> approveStep = paymentWs.approveBeforePaymentBatch(newReq);
			if (isSuccess(approveStep.getMessage())) {

				CommonMessage<FeePaymentResponse> dbdStep = paymentWs.feePaymentDBDBatch(newReq);
				if (isSuccess(dbdStep.getMessage())) {

					CommonMessage<RealtimePaymentResponse> realtimeStep = paymentWs.realtimePaymentBatch(newReq);
					if (isSuccess(realtimeStep.getMessage())) {

						newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
						response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
						dao.updateRequesstForm(newReq);
						history.save(newReq);

					} else {
						newReq.setPaymentStatus("DBDF");
						response = handlerErrorReq(response, newReq);
						response.setData(realtimeStep.getData().getDescription());
						throw new Exception("REALTIME => " + response.getData());
					}
				} else {
					response = handlerErrorReq(response, newReq);
					response.setData(dbdStep.getData().getDescription());
					throw new Exception("DBD => " + response.getData());
				}
			} else {
				response = handlerErrorReq(response, newReq);
				response.setData(approveStep.getData().getDescription());
				throw new Exception("APPROVE => " + response.getData());
			}
			break;
		case PAYMENT_STATUS.PAY_TMB: // 10003
			CommonMessage<FeePaymentResponse> tmbOnlyStep = paymentWs.feePaymentDBDBatch(newReq);
			if (isSuccess(tmbOnlyStep.getMessage())) {

				newReq.setStatus(StatusConstant.WAIT_UPLOAD_CERTIFICATE);
				response.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
				dao.updateRequesstForm(newReq);
				history.save(newReq);
				
			} else {
				newReq.setPaymentStatus("DBDF");
				response = handlerErrorReq(response, newReq);
				response.setData(tmbOnlyStep.getData().getDescription());
				throw new Exception("DBD => " + response.getData());
			}
			break;
		}
		return response;
	}

	private boolean isSuccess(String value) {
		return PAYMENT_STATUS.SUCCESS_MSG.equals(value);
	}

	private CommonMessage<String> handlerErrorReq(CommonMessage<String> msg, RequestForm req) {
		int count = 0;
		if (req.getCountPayment() != null) {
			count = req.getCountPayment();
		}
		req.setCountPayment(count+1);
		req.setStatus(StatusConstant.PAYMENT_FAILED);
		dao.updateRequesstForm(req);
		msg.setMessage(PAYMENT_STATUS.ERROR_MSG);
		return msg;
	}
}
