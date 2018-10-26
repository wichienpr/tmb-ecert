package com.tmb.ecert.common.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ApproveBeforePayRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.ApproveBeforePayResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentResponse;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.RealtimePaymentResponse;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.ProjectConstant.WEB_SERVICE_ENDPOINT;
import com.tmb.ecert.common.constant.ProjectConstant.WEB_SERVICE_PARAMS;
import com.tmb.ecert.common.constant.StatusConstant.PAYMENT_STATUS;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestForm;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class PaymentWebService {

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_PAYMENT);

	public CommonMessage<FeePaymentResponse> feePaymentTMB(RequestForm reqF) {
		logger.info("PaymentWebService::feePaymentTMB");
		CommonMessage<FeePaymentResponse> commonMsg = new CommonMessage<FeePaymentResponse>();
		String uuid = UUID.randomUUID().toString();
		logger.info("PaymentWebService::feePayment UUID => {}", uuid);
		FeePaymentRequest req = new FeePaymentRequest();
		req.setRqUid(uuid);
		req.setClientDt(DateConstant.convertDateToStrDDMMYYYYHHmmss(new Date()));
		req.setCustLangPref(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CUST_LANG));
		req.setClientAppOrg(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_ORG));
		req.setClientAppName(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_NAME));
		req.setClientAppVersion(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_VERSION));
		req.setSpName(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_SPNAME));
		req.setTranCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_TRANS_CODE));
		req.setFromAccountIdent(reqF.getAccountNo()); // ECERT_REQUEST_FORM.ACCOUNT_NO
		// ECERT_REQUEST_FORM. ACCOUNTTYPE
		req.setFromAccountType(reqF.getAccountType() != null ? reqF.getAccountType()
				: ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_FROM_ACCOUNT_TYPE));
		req.setToAccountIdent(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.DBD_ACCOUNT));
		req.setToAccountType(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_TO_ACCOUNT_TYPE));
		req.setCurAmt(new BigDecimal(0.0)); // ECERT_REQUEST_FORM.AMOUNT_DBD
		req.setBillPmtFee(reqF.getAmountTmb()); // ECERT_REQUEST_FORM.AMOUNT_TMB
		req.setRef1(reqF.getRef1()); // ECERT_REQUEST_FORM.REF1
		req.setRef2(reqF.getRef2()); // ECERT_REQUEST_FORM.REF2
		req.setPostedDate(DateConstant.convertDateToStrDDMMYYYY(new Date()));
		req.setEpayCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_EPAY));
		req.setBranchIdent(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_BRANCH));
		req.setCompCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_COMP_CODE));
		req.setPostedTime(DateConstant.convertDateToStrHHmmss(new Date()));
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<FeePaymentRequest> request = new HttpEntity<>(req);
			ResponseEntity<FeePaymentResponse> response = restTemplate.exchange(ApplicationCache.getParamValueByName(WEB_SERVICE_ENDPOINT.XPRESS_BILLPAYMENT),
					HttpMethod.POST, request, FeePaymentResponse.class);
			FeePaymentResponse res = response.getBody();
			if (PAYMENT_STATUS.SUCCESS.equalsIgnoreCase(res.getStatusCode())) {
				commonMsg.setData(res);
				commonMsg.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			} else {
				commonMsg.setData(res);
				throw new Exception(res.getStatusCode() + " - " + res.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			commonMsg.setMessage(e.getMessage());
			return commonMsg;
		}
		return commonMsg;
	}

	public CommonMessage<FeePaymentResponse> feePaymentDBD(RequestForm reqF) {
		logger.info("PaymentWebService::feePaymentDBD");
		CommonMessage<FeePaymentResponse> commonMsg = new CommonMessage<FeePaymentResponse>();
		String uuid = UUID.randomUUID().toString();
		logger.info("PaymentWebService::feePaymentDBD UUID => {}", uuid);
		FeePaymentRequest req = new FeePaymentRequest();
		req.setRqUid(uuid);
		req.setClientDt(DateConstant.convertDateToStrDDMMYYYYHHmmss(new Date()));
		req.setCustLangPref(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CUST_LANG));
		req.setClientAppOrg(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_ORG));
		req.setClientAppName(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_NAME));
		req.setClientAppVersion(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_CLIENT_VERSION));
		req.setSpName(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_SPNAME));
		req.setTranCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_TRANS_CODE));
		req.setFromAccountIdent(reqF.getAccountNo()); // ECERT_REQUEST_FORM.ACCOUNT_NO
		// ECERT_REQUEST_FORM. ACCOUNTTYPE
		req.setFromAccountType(reqF.getAccountType() != null ? reqF.getAccountType()
				: ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_FROM_ACCOUNT_TYPE));
		req.setToAccountIdent(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.DBD_ACCOUNT));
		req.setToAccountType(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_TO_ACCOUNT_TYPE));
		req.setCurAmt(reqF.getAmountDbd()); // ECERT_REQUEST_FORM.AMOUNT_DBD
		req.setBillPmtFee(new BigDecimal(0.0)); // ECERT_REQUEST_FORM.AMOUNT_TMB
		req.setRef1(reqF.getRef1()); // ECERT_REQUEST_FORM.REF1
		req.setRef2(reqF.getRef2()); // ECERT_REQUEST_FORM.REF2
		req.setPostedDate(DateConstant.convertDateToStrDDMMYYYY(new Date()));
		req.setEpayCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_EPAY));
		req.setBranchIdent(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_BRANCH));
		req.setCompCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.FEE_COMP_CODE));
		req.setPostedTime(DateConstant.convertDateToStrHHmmss(new Date()));
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<FeePaymentRequest> request = new HttpEntity<>(req);
			ResponseEntity<FeePaymentResponse> response = restTemplate.exchange(ApplicationCache.getParamValueByName(WEB_SERVICE_ENDPOINT.XPRESS_BILLPAYMENT),
					HttpMethod.POST, request, FeePaymentResponse.class);
			FeePaymentResponse res = response.getBody();
			if (PAYMENT_STATUS.SUCCESS.equalsIgnoreCase(res.getStatusCode())) {
				commonMsg.setData(res);
				commonMsg.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			} else {
				commonMsg.setData(res);
				throw new Exception(res.getStatusCode() + " - " + res.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			commonMsg.setMessage(e.getMessage());
			return commonMsg;
		}
		return commonMsg;
	}

	public CommonMessage<ApproveBeforePayResponse> approveBeforePayment(RequestForm reqF) {
		logger.info("PaymentWebService::approveBeforePayment");
		CommonMessage<ApproveBeforePayResponse> commonMsg = new CommonMessage<ApproveBeforePayResponse>();
		ApproveBeforePayRequest req = new ApproveBeforePayRequest();
		req.setBankCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_BANKCODE));
		req.setServiceCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_SERVICECODE));
		req.setRef1(reqF.getRef1());
		req.setRef2(reqF.getRef2());
		req.setAmount(reqF.getAmountDbd());
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<ApproveBeforePayRequest> request = new HttpEntity<>(req);
			ResponseEntity<ApproveBeforePayResponse> response = restTemplate.exchange(
					ApplicationCache.getParamValueByName(WEB_SERVICE_ENDPOINT.APPROVE_BEFOREPAYMENT), HttpMethod.POST, request, ApproveBeforePayResponse.class);
			ApproveBeforePayResponse res = response.getBody();
			if (PAYMENT_STATUS.SUCCESS.equalsIgnoreCase(res.getStatusCode())) {
				commonMsg.setData(res);
				commonMsg.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			} else {
				commonMsg.setData(res);
				throw new Exception(res.getStatusCode() + " - " + res.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			commonMsg.setMessage(e.getMessage());
			return commonMsg;
		}
		return commonMsg;
	}

	public CommonMessage<RealtimePaymentResponse> realtimePayment(RequestForm reqF) {
		logger.info("PaymentWebService::realtimePayment");
		CommonMessage<RealtimePaymentResponse> commonMsg = new CommonMessage<RealtimePaymentResponse>();
		RealtimePaymentRequest req = new RealtimePaymentRequest();
		req.setServiceCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_SERVICECODE));
		req.setTransactionNo(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_TRANSACTION_NO));
		req.setRef1(reqF.getRef1());
		req.setRef2(reqF.getRef2());
		req.setBankCode(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_BANKCODE));
		req.setBranchCode(reqF.getPaymentBranchCode());
		req.setPaymentType(ApplicationCache.getParamValueByName(WEB_SERVICE_PARAMS.TMB_PAYMENT_TYPE));
		req.setPayAmount(reqF.getAmountDbd());
		req.setPaymentDate(DateConstant.convertDateToStrDDMMYYYYHHmmss(reqF.getPaymentDate()));
		req.setPaymentName(reqF.getCustomerName());
		req.setTransactionDate(DateConstant.convertDateToStrDDMMYYYY(reqF.getPostDate()));
		req.setPayloadTS(DateConstant.convertDateToStrDDMMYYYYHHmmss(new Date()));
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<RealtimePaymentRequest> request = new HttpEntity<>(req);
			ResponseEntity<RealtimePaymentResponse> response = restTemplate.exchange(
					ApplicationCache.getParamValueByName(WEB_SERVICE_ENDPOINT.REALTIME_PAYMENT), HttpMethod.POST, request, RealtimePaymentResponse.class);
			RealtimePaymentResponse res = response.getBody();
			if (PAYMENT_STATUS.SUCCESS.equalsIgnoreCase(res.getStatusCode())) {
				commonMsg.setData(res);
				commonMsg.setMessage(PAYMENT_STATUS.SUCCESS_MSG);
			} else {
				commonMsg.setData(res);
				throw new Exception(res.getStatusCode() + " - " + res.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			commonMsg.setMessage(e.getMessage());
			return commonMsg;
		}
		return commonMsg;
	}

}
