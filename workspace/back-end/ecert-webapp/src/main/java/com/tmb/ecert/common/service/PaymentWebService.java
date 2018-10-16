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

import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentResponse;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.StatusConstant;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestForm;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class PaymentWebService {

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_PAYMENT);

	@Value("${web.service.url}")
	String WS_URL;

	public CommonMessage<FeePaymentRequest> feePayment(RequestForm reqF) {
		logger.info("PaymentWebService::feePayment");
		CommonMessage<FeePaymentRequest> commonMsg = new CommonMessage<FeePaymentRequest>();
		String uuid = UUID.randomUUID().toString();
		logger.info("UUID => {}", uuid);
		RestTemplate restTemplate = new RestTemplate();
		FeePaymentRequest req = new FeePaymentRequest();
		req.setRqUid(uuid);
		req.setClientDt(DateConstant.convertDateToStrDDMMYYYYHHmmss(new Date()));
		req.setCustLangPref(ApplicationCache.getParamValueByName("feepayment.customer.language"));
		req.setClientAppOrg(ApplicationCache.getParamValueByName("feepayment.clientapp.org"));
		req.setClientAppName(ApplicationCache.getParamValueByName("feepayment.clientapp.name"));
		req.setClientAppVersion(ApplicationCache.getParamValueByName("feepayment.clientapp.version"));
		req.setSpName(ApplicationCache.getParamValueByName("feepayment.SPName"));
		req.setTranCode(ApplicationCache.getParamValueByName("feepayment.transaction.code"));
		req.setFromAccountIdent(reqF.getAccountNo()); // ECERT_REQUEST_FORM.ACCOUNT_NO
		// ECERT_REQUEST_FORM. ACCOUNTTYPE
		req.setFromAccountType(reqF.getAccountType() != null ? reqF.getAccountType()
				: ApplicationCache.getParamValueByName("feepayment.fromacctref.account.type"));
		req.setToAccountIdent(ApplicationCache.getParamValueByName("dbd.accountno"));
		req.setToAccountType(ApplicationCache.getParamValueByName("feepayment.toacctref.account.type"));
		req.setTransferAmount(reqF.getAmountDbd()); // ECERT_REQUEST_FORM.AMOUNT_DBD
		req.setFee(new BigDecimal(0.0)); // ECERT_REQUEST_FORM.AMOUNT_TMB
		req.setRef1(reqF.getRef1()); // ECERT_REQUEST_FORM.REF1
		req.setRef2(reqF.getRef2()); // ECERT_REQUEST_FORM.REF2
		req.setPostedDate(DateConstant.convertDateToStrDDMMYYYY(new Date()));
		req.setEpayCode(ApplicationCache.getParamValueByName("feepayment.epay.code"));
		req.setBranchIdent(ApplicationCache.getParamValueByName("feepayment.branchIdent"));
		req.setCompCode(ApplicationCache.getParamValueByName("feepayment.compcode"));
		req.setPostedTime(DateConstant.convertDateToStrHHmmss(new Date()));
		try {
			HttpEntity<FeePaymentRequest> request = new HttpEntity<>(req);
			ResponseEntity<FeePaymentResponse> response = restTemplate.exchange(WS_URL + "api-payment/add",
					HttpMethod.POST, request, FeePaymentResponse.class);
			FeePaymentResponse res = response.getBody();
			if (StatusConstant.PAYMENT_STATUS.SUCCESS.equals(res.getStatusCode())) {
				commonMsg.setData(req);
				commonMsg.setMessage("SUCCESS");
			} else {
				commonMsg.setData(req);
				throw new Exception(res.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return commonMsg;
	}

}
