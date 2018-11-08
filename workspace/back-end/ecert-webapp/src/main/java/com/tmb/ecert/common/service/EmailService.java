package com.tmb.ecert.common.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.setup.dao.EmailTemplateDao;
import com.tmb.ecert.setup.vo.Sup03000Vo;
import com.tmb.ecert.setup.vo.Sup03100Vo;

import th.co.baiwa.buckwaframework.common.service.MailGatewayService;
import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;

@Service
public class EmailService {

	@Autowired
	private MailGatewayService mailGateWay;

	@Autowired
	private EmailTemplateDao emailTemplatDao;

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

//	private static String emailFrom = "keadtisak.test@gmail.com";
//	private static String emailTo = "keadtisak.chai@gmail.com,chaiyawanlive@gmail.com";

	public void sendEmailPaymentOrder(String paymentCompanyName, String tmbRequsetNo, String makerName) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("7", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Object[] subjectParam = { paymentCompanyName };
			Object[] param = { tmbRequsetNo, paymentCompanyName, makerName };

			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);

		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL PAYMENTORDER FAIL {}", e.toString());
		}

	}

	public void sendEmailRejectPayment(String paymentCompanyName, String requseterName, String tmbRequsetNo,
			String reason, String remark) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("8", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Object[] subjectParam = { paymentCompanyName };
			Object[] param = { requseterName, tmbRequsetNo, reason, remark };

			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL REJECTPAYMENT FAIL {}", e.toString());
		}

	}

	public void sendEmailSendLinkForDownload(String paymentCompanyName, String requseterName, String tmbRequsetNo) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("9", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Object[] subjectParam = { tmbRequsetNo, requseterName };
			Object[] param = { requseterName, tmbRequsetNo };

			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);

		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL SENDLINKFORDOWNLOAD FAIL {}", e.toString());
		}
	}

	public void sendEmailCancleRequest(String paymentCompanyName, String requseterName, String tmbRequsetNo,
			String reason, String remark) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("10", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Object[] subjectParam = { tmbRequsetNo, requseterName };
			Object[] param = { requseterName, tmbRequsetNo, reason, remark };
			
			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);

		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL CANCLEREQUEST FAIL {}", e.toString());
		}
	}

	public void sendEmailFailApproveBeforePay(RequestForm reqF, String serviceName, Date errorTime, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("1", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Date reqDate = new Date(reqF.getRequestDate().getTime());
			Date now = new Date();

			Object[] param = { EcerDateUtils.formatDDMMYYYYDate(reqDate), EcerDateUtils.formatHHMM(reqDate),
					serviceName, reqF.getTmbRequestNo(), reqF.getRef1(), reqF.getRef2(),
					EcerDateUtils.formatDDMMYYYYDate(new Date(reqF.getPaymentDate().getTime())), reqF.getAmount(),
					EcerDateUtils.formatDDMMYYYYDate(now), EcerDateUtils.formatHHMM(now), rootCase };
			Object[] subjectParam = {};
			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);
			
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL  FAILAPPROVEBEFOREPAY FAIL {}", e.toString());
		}
	}

	public void sendEmailFailRealtimePayment(RequestForm reqF, String serviceName, Date errorTime, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("3", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Date reqDate = new Date(reqF.getRequestDate().getTime());
			Date now = new Date();
			
			Object[] param = { 
					EcerDateUtils.formatDDMMYYYYDate(reqDate), 
					EcerDateUtils.formatHHMM(reqDate),
					reqF.getRef1(),
					EcerDateUtils.formatDDMMYYYYDate(now), 
					EcerDateUtils.formatHHMM(now), 
					rootCase };
			Object[] subjectParam = {};
			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);
			
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL FAILREALTIMEPAYMENT FAIL {}", e.toString());
		}
	}

	public void sendEmailFailFeePayment(RequestForm reqF, String serviceCode, Date errorTime, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("2", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Date reqDate = new Date(reqF.getRequestDate().getTime());
			Date now = new Date();

			Object[] bodyParam = { EcerDateUtils.formatDDMMYYYYDate(reqDate), EcerDateUtils.formatHHMM(reqDate),
					serviceCode, reqF.getTmbRequestNo(), reqF.getRef1(), reqF.getRef2(),
					EcerDateUtils.formatDDMMYYYYDate(new Date(reqF.getPaymentDate().getTime())), reqF.getAmount(),
					EcerDateUtils.formatDDMMYYYYDate(now), EcerDateUtils.formatHHMM(now), rootCase };
			Object[] subjectParam = {};
			
			sendEmailToEmailGateWay(emailtemplate, bodyParam ,subjectParam);
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL FAILFEEPAYMENT FAIL {}", e.toString());
		}

	}

	public void sendEmailFailSendDoc(RequestForm req, Date errorTime, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("4", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);

			Date reqDate = new Date(req.getRequestDate().getTime());
			Date now = new Date();

			Object[] bodyParam = { EcerDateUtils.formatDDMMYYYYDate(reqDate), EcerDateUtils.formatHHMM(reqDate),
					req.getTmbRequestNo(), EcerDateUtils.formatDDMMYYYYDate(errorTime),
					EcerDateUtils.formatHHMM(errorTime), rootCase };
			Object[] subjectParam = {};

			sendEmailToEmailGateWay(emailtemplate, bodyParam ,subjectParam);
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL FAILSENDDOC FAIL {}", e.toString());
		}

	}

	public void sendEmailFailPaymentOrder(RequestForm reqF, String serviceCode, Date errorTime, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("5", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);
			Date reqDate = new Date(reqF.getRequestDate().getTime());
			Date now = new Date();
			
			Object[] bodyParam = { EcerDateUtils.formatDDMMYYYYDate(reqDate), EcerDateUtils.formatHHMM(reqDate),
					serviceCode, reqF.getTmbRequestNo(), reqF.getRef1(), reqF.getRef2(),
					EcerDateUtils.formatDDMMYYYYDate(new Date(reqF.getPaymentDate().getTime())), reqF.getAmount(),
					EcerDateUtils.formatDDMMYYYYDate(now), EcerDateUtils.formatHHMM(now), rootCase };
			Object[] subjectParam = {};

			sendEmailToEmailGateWay(emailtemplate, bodyParam ,subjectParam);
			
		} catch (Exception e) {
			sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_SEND_EMAIL, e.toString());
			log.error("SEND EMAIL FAILPAYMENTORDER FAIL {}", e.toString());
		}
	}
	
	public void sendEmailAbnormal(Date errDate , String functionName, String rootCase) {
		try {
			Sup03000Vo form = new Sup03000Vo();
			form.setEmailConfig_id(Long.parseLong("6", 10));
			Sup03100Vo emailtemplate = emailTemplatDao.getEmailDetail(form);
			Date now = new Date();
			Object[] subjectParam = {functionName};
			Object[] param = {EcerDateUtils.formatDDMMYYYYDate(now), EcerDateUtils.formatHHMM(now),
					functionName, EcerDateUtils.formatDDMMYYYYDate(errDate), EcerDateUtils.formatHHMM(errDate),rootCase };
			
			sendEmailToEmailGateWay(emailtemplate, param ,subjectParam);
		} catch (Exception e) {
			log.error("SEND EMAIL ABNORMAL FAIL {}", e.toString());
		}
	}
	
	@Async
	public void sendEmailToEmailGateWay(Sup03100Vo emailtemplate , Object[] bodyParam ,Object[] bodySubject) {
		
		try {
			log.info("SEND EAIL TO EMAILGATEWAY ");
			MimeMessageHelper mail = mailGateWay.newMail();
			mail.setSubject(emailtemplate.getSubject());
			String body = "<!DOCTYPE html><html ><body><pre style='font-size:16px;'>";
			body += emailtemplate.getBody();
			body += "</pre></body></html>";
			List<String> ccReceiver = new ArrayList<>();
			List<String> receiver = Arrays.asList(emailtemplate.getTo().split(","));
			
			for (int i = 0; i < receiver.size(); i++) {
				if (i == 0 ) {
					mail.setTo(receiver.get(i));
				}else {
					ccReceiver.add(receiver.get(i));
				}
			}
			mail.setCc(ccReceiver.toArray(new String[0]));
			mail.setFrom(emailtemplate.getFrom());
			mail.setSubject(MessageFormat.format(emailtemplate.getSubject(), bodySubject));
			mail.setText(MessageFormat.format(body, bodyParam), true);
			mailGateWay.sendEmail(mail);

			log.info("SEND EAIL TO EMAILGATEWAY SUCCESS");
			
		} catch (Exception e) {
			log.error("SEND EAIL TO EMAILGATEWAY FAIL {} ",e.toString());
		}
		
	}
	
	
	
	
	
	

}
