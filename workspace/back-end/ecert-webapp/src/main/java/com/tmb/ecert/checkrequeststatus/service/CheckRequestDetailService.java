package com.tmb.ecert.checkrequeststatus.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentRequest;
import com.tmb.ecert.checkrequeststatus.persistence.vo.ws.FeePaymentResponse;
import com.tmb.ecert.common.constant.DateConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.CommonMessage;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.requestorform.persistence.dao.RequestorDao;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class CheckRequestDetailService {

	private static String PATH = "tmb-requestor/";

	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);
	
	@Value("${web.service.url}")
	String WS_URL;

	@Autowired
	private DownloadService download;

	@Autowired
	private CheckRequestDetailDao dao;

	@Autowired
	private RequestorDao reqDao;

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
			commonMsg.setMessage("SUCCESS");
			return commonMsg;
		} catch (Exception e) {
			e.printStackTrace();
			commonMsg.setMessage("ERROR");
			return commonMsg;
		}
	}

	public CommonMessage<FeePaymentResponse> approve() {
		logger.info("CheckRequestDetailService::approve");
		CommonMessage<FeePaymentResponse> commonMsg = new CommonMessage<FeePaymentResponse>();
		try {
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
			req.setFromAccountIdent(""); // ECERT_REQUEST_FORM.ACCOUNT_NO
			// ECERT_REQUEST_FORM. ACCOUNTTYPE
			req.setFromAccountType(ApplicationCache.getParamValueByName("feepayment.fromacctref.account.type"));
			req.setToAccountIdent(ApplicationCache.getParamValueByName("dbd.accountno"));
			req.setToAccountType(ApplicationCache.getParamValueByName("feepayment.toacctref.account.type"));
			req.setTransferAmount(new BigDecimal(0.0)); // ECERT_REQUEST_FORM.AMOUNT_DBD
			req.setFee(new BigDecimal(0.0)); // ECERT_REQUEST_FORM.AMOUNT_TMB
			req.setRef1(""); // ECERT_REQUEST_FORM.REF1
			req.setRef2(""); // ECERT_REQUEST_FORM.REF2
			req.setPostedDate(DateConstant.convertDateToStrDDMMYYYY(new Date()));
			req.setEpayCode(ApplicationCache.getParamValueByName("feepayment.epay.code"));
			req.setBranchIdent(ApplicationCache.getParamValueByName("feepayment.branchIdent"));
			req.setCompCode(ApplicationCache.getParamValueByName("feepayment.compcode"));
			req.setPostedTime(DateConstant.convertDateToStrHHmmss(new Date()));
			HttpEntity<FeePaymentRequest> request = new HttpEntity<>(req);
			ResponseEntity<FeePaymentResponse> response = restTemplate.exchange(
					WS_URL + "api-payment/add", HttpMethod.POST, request,
					FeePaymentResponse.class);
			FeePaymentResponse foo = response.getBody();
			if ("0".equals(foo.getStatusCode())) {
				commonMsg.setMessage("SUCCESS");
			} else {
				commonMsg.setData(foo);
				throw new Exception(foo.getDescription());
			}
			return commonMsg;
		} catch (Exception e) {
			commonMsg.setMessage("ERROR");
			return commonMsg;
		}
	}

	public CommonMessage<String> approve(RequestForm req) {
		CommonMessage<String> commonMsg = new CommonMessage<String>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<FeePaymentRequest> request = new HttpEntity<>(new FeePaymentRequest());
			ResponseEntity<FeePaymentResponse> response = restTemplate.exchange(
					"http://150.95.24.42:8080/tmp-ws/api-payment/add", HttpMethod.POST, request,
					FeePaymentResponse.class);

			FeePaymentResponse foo = response.getBody();
//			RequestForm newReq = dao.findReqFormById(req.getReqFormId(), false).get(0);
//			newReq.setStatus("10005");
//			reqDao.update(newReq);
//			commonMsg.setMessage("SUCCESS");
			return commonMsg;
		} catch (Exception e) {
			e.printStackTrace();
			commonMsg.setMessage("ERROR");
			return commonMsg;
		}
	}

}
