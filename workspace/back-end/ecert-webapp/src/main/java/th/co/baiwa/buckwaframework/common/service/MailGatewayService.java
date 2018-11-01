package th.co.baiwa.buckwaframework.common.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
@ConditionalOnProperty(name = "mail.gateway.host")
public class MailGatewayService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${mail.gateway.sendFlag}")
	private boolean sendFalg;

	/*
	 * Doc
	 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html
	 * /mail.html see 33.3.1 Sending attachments and inline resources
	 */
	@Autowired
	private JavaMailSenderImpl mailGateWaySender;

	/**
	 * @return
	 */
	public MimeMessageHelper newMail() {
		MimeMessage message = mailGateWaySender.createMimeMessage();
		
		String appHost = ApplicationCache.getParamValueByName(ProjectConstant.EMAIL_SERVICE.EMAIL_GATEWAY_HOST);
		String appport = (ApplicationCache.getParamValueByName(ProjectConstant.EMAIL_SERVICE.EMAIL_GATEWAY_PORT));

		mailGateWaySender.setHost(appHost);
		mailGateWaySender.setPort(Integer.parseInt(appport));
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);
		} catch (MessagingException e) {
			log.error("newMail error = ",e);
		}
		return helper;
	}

	/**
	 * @param messageHelper
	 */
	
	public void sendEmail(MimeMessageHelper messageHelper) {
		if(sendFalg){
			 mailGateWaySender.send(messageHelper.getMimeMessage());
			log.debug("sendEmail ....OK");
		}else{
			log.info("mail.gateway.sendFlag=false , Mail not send to server.");
		}
	}
	
	/**
	 * @param groupName
	 */
//	@Async
//	public void sendEmailGroup(String groupName) {
//		log.debug("send group name = " + groupName);
//
//		List<FCGroupMailJoinMailTo> groupMailList = fiGroupEmailDao.getFiGroupMailJoinMailTo(groupName);
//		if (!groupMailList.isEmpty()) {
//			try {
//				List<String> recivers = new ArrayList<>();
//				for (FCGroupMailJoinMailTo item : groupMailList) {
//					recivers.add(item.getEmailTo());
//					log.debug(item.getEmailTo());
//				}
//				final FCGroupMailJoinMailTo master = groupMailList.get(0);
//				MimeMessageHelper mail = newMail();
//				mail.setSubject(master.getEmailSubject());
//				mail.setFrom(master.getEmailFrom());
//				mail.setTo(recivers.toArray(new String[recivers.size()]));
//				mail.setText(master.getEmailBody());
//				sendEmail(mail);
//				log.debug("Sent message successfully....");
//			} catch (MessagingException e) {
//				e.printStackTrace();
//				log.info("fail = " + e.getMessage());
//			}
//
//		} else {
//			log.info("Group Email not found = " + groupName);
//		}
//
//	}
//
//	/**
//	 * @param groupName
//	 * @return
//	 */
//	public EmailVo initMail(String groupName) {
//		EmailVo returnM = null;
//		List<FCGroupMailJoinMailTo> groupMailList = fiGroupEmailDao.getFiGroupMailJoinMailTo(groupName);
//		if (!groupMailList.isEmpty()) {
//			try {
//				List<String> recivers = new ArrayList<>();
//				for (FCGroupMailJoinMailTo item : groupMailList) {
//					recivers.add(item.getEmailTo());
//					log.debug("Mail to : " + item.getEmailTo());
//				}
//				final FCGroupMailJoinMailTo master = groupMailList.get(0);
//				MimeMessageHelper mail = newMail();
//				mail.setSubject(master.getEmailSubject());
//				mail.setFrom(master.getEmailFrom());
//				mail.setTo(recivers.toArray(new String[recivers.size()]));
//				String body = "<!DOCTYPE html><html><body><pre style='font-size:16px;'>";
//				SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
//				SimpleDateFormat formatterTime = new SimpleDateFormat("HH.mm");
//				body += master.getEmailBody();
//				body += "</pre></body></html>";
//				body = body.replace("$CURRENT_DATE", formatterDay.format(new Date())).replace("$CURRENT_TIME",
//						formatterTime.format(new Date()));
//
//				//log.debug("BODY : " + body);
//				log.debug("Create message successfully....");
//
//				returnM = new EmailVo();
//				returnM.setMessageHelper(mail); 
//				returnM.setBody(body);
//				returnM.setIsAttachment(master.getEmailIsAttach().intValue());
//			} catch (MessagingException e) {
//				e.printStackTrace();
//				log.info("fail = " + e.getMessage());
//			}
//		} else {
//			log.info("initMail , Group Email not found = " + groupName);
//		}
//
//		return returnM;
//	}
//	
//	/**
//	 * @param groupName
//	 * @return
//	 */
//	public List<EmailVo> initMailList(String groupName) {
//		List<EmailVo> returnMailList = new ArrayList<>();
//		List<FCGroupEmail> groupMailList = fiGroupEmailDao.getFCGroupEmail(groupName);
//		if (!groupMailList.isEmpty()) {
//			try {
//				for (FCGroupEmail master : groupMailList) {					
//					List<FCGroupMailJoinMailTo> groupMailToList = fiGroupEmailDao.getFCGroupMailJoinMailToByGroupMailID(master.getGroupemailId());
//					if (!groupMailToList.isEmpty()) {
//						
//						//Add Mail To
//						List<String> recivers = new ArrayList<>();
//						for (FCGroupMailJoinMailTo item : groupMailToList) {
//							recivers.add(item.getEmailTo());
//							log.debug("Mail to : " + item.getEmailTo());
//						}
//						
//						MimeMessageHelper mail = newMail();
//						mail.setSubject(master.getEmailSubject());
//						mail.setFrom(master.getEmailFrom());
//						mail.setTo(recivers.toArray(new String[recivers.size()]));
//						String body = "<!DOCTYPE html><html><body><pre style='font-size:16px;'>";
//						SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
//						SimpleDateFormat formatterTime = new SimpleDateFormat("HH.mm");
//						body += master.getEmailBody();
//						body += "</pre></body></html>";
//						body = body.replace("$CURRENT_DATE", formatterDay.format(new Date())).replace("$CURRENT_TIME",
//								formatterTime.format(new Date()));
//
//						//log.debug("BODY : " + body);
//						log.debug("Create message successfully....");
//
//						EmailVo returnM = new EmailVo();
//						returnM.setMessageHelper(mail); 
//						returnM.setBody(body);
//						returnM.setIsAttachment(master.getEmailIsattach().intValue());
//						
//						returnMailList.add(returnM);
//					}
//				}
//				
//			} catch (MessagingException e) {
//				e.printStackTrace();
//				log.info("fail = " + e.getMessage());
//			}
//		} else {
//			log.info("initMail , Group Email not found = " + groupName);
//		}
//
//		return returnMailList;
//	}
//
//	/**
//	 * @param groupName
//	 * @param comsRequest
//	 * @return
//	 */
//	public EmailVo initMailToBu(String groupName, String comsRequest) {
//		log.debug("Start  initMailToBu :" +groupName +" : "+comsRequest);
//		EmailVo returnM = null;
//		List<FCGroupMailJoinMailTo> groupMailList = fiGroupEmailDao.getFiGroupMail(groupName);
//		if (!groupMailList.isEmpty()) {
//			try {
//				List<String> recivers = new ArrayList<>();
//				// Mail BU
//				List<SentEmailToBU> sentEmailToBUs = fiGroupEmailDao.getEmployeeEmail(comsRequest);
//				
//				if(sentEmailToBUs.isEmpty() ){
//					log.debug("sentEmailToBUs  null, no employee in comsRequest = "+ comsRequest);
//					return null;
//				}
//				
//				for (SentEmailToBU item : sentEmailToBUs) {
//					if (!StringUtils.isEmpty(item.getEmpEmail())) {
//						recivers.add(item.getEmpEmail());
//						log.debug("Mail to : " + item.getEmpEmail());
//					}else{
//						log.debug("Emp_Email is null , Emp_Id = " + item.getEmpId() );
//					}
//				}
//
//				if (recivers.isEmpty()) {
//					log.debug("recivers  null, no employee in comsRequest = "+ comsRequest);
//					return null;
//				}
//				
//				final FCGroupMailJoinMailTo master = groupMailList.get(0);
//				MimeMessageHelper mail = newMail();
//				mail.setSubject(master.getEmailSubject());
//				mail.setFrom(master.getEmailFrom());
//				mail.setTo(recivers.toArray(new String[recivers.size()]));
//				String body = "<!DOCTYPE html><html><body><pre style='font-size:16px;'>";
//				SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
//				SimpleDateFormat formatterTime = new SimpleDateFormat("HH.mm");
//				body += master.getEmailBody();
//				body += "</pre></body></html>";
//				body = body.replace("$CURRENT_DATE", formatterDay.format(new Date())).replace("$CURRENT_TIME",
//						formatterTime.format(new Date()));
//
////				log.debug("BODY : " + body);
//				log.debug("Create message successfully....");
//
//				returnM = new EmailVo();
//				returnM.setMessageHelper(mail);
//				returnM.setBody(body);
//				returnM.setIsAttachment(master.getEmailIsAttach().intValue());
//			} catch (MessagingException e) {
//				e.printStackTrace();
//				log.info("fail = " + e.getMessage());
//			}
//		} else {
//			log.info("initMail , Group Email not found = " + groupName);
//		}
//
//		return returnM;
//	}
//
//	/**
//	 * @param groupName
//	 * @param comsRequest
//	 * @return
//	 */
//	public List<EmailVo> initMailToBuList(String groupName, String comsRequest) {
//		log.debug("Start  initMailToBu :" +groupName +" : "+comsRequest);
//		List<EmailVo> returnMailList = new ArrayList<>();
//		List<FCGroupMailJoinMailTo> groupMailList = fiGroupEmailDao.getFiGroupMail(groupName);
//		if (!groupMailList.isEmpty()) {
//			try {
//				List<String> recivers = new ArrayList<>();
//				// Mail BU
//				List<SentEmailToBU> sentEmailToBUs = fiGroupEmailDao.getEmployeeEmail(comsRequest);
//				
//				List<SentEmailToBU> sentEmailToDept = fiGroupEmailDao.getDeptEmail(comsRequest);
//				
//				if(sentEmailToBUs.isEmpty() ){
//					log.debug("sentEmailToBUs  null, no employee in comsRequest = "+ comsRequest);
//					return null;
//				}
//				
//				for (SentEmailToBU item : sentEmailToBUs) {
//					if (!StringUtils.isEmpty(item.getEmpEmail())) {
//						recivers.add(item.getEmpEmail());
//						log.debug("Mail to : " + item.getEmpEmail());
//					}else{
//						log.debug("Emp_Email is null , Emp_Id = " + item.getEmpId() );
//					}
//				}
//				
//				if(sentEmailToDept!=null && sentEmailToDept.size()>0){
//					for (SentEmailToBU item : sentEmailToDept) {
//						if (!StringUtils.isEmpty(item.getDepteMAIL())) {
//							log.debug("Mail to : " + item.getDepteMAIL());
//							String[] deptEmails = item.getDepteMAIL().split(",");
//							if(deptEmails!=null && deptEmails.length>0){
//								for(String email : deptEmails){
//									recivers.add(email);
//								}
//							}
//						}
//					}
//				}
//				
//
//				if (recivers.isEmpty()) {
//					log.debug("recivers  null, no employee in comsRequest = "+ comsRequest);
//					return null;
//				}
//				for (FCGroupMailJoinMailTo master : groupMailList) {	
//					MimeMessageHelper mail = newMail();
//					mail.setSubject(master.getEmailSubject());
//					mail.setFrom(master.getEmailFrom());
//					mail.setTo(recivers.toArray(new String[recivers.size()]));
//					String body = "<!DOCTYPE html><html><body><pre style='font-size:16px;'>";
//					SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
//					SimpleDateFormat formatterTime = new SimpleDateFormat("HH.mm");
//					body += master.getEmailBody();
//					body += "</pre></body></html>";
//					body = body.replace("$CURRENT_DATE", formatterDay.format(new Date())).replace("$CURRENT_TIME",
//							formatterTime.format(new Date()));
//
////					log.debug("BODY : " + body);
//					log.debug("Create message successfully....");
//
//					EmailVo email = new EmailVo();
//					email.setMessageHelper(mail);
//					email.setBody(body);
//					email.setSubject(master.getEmailSubject());
//					email.setIsAttachment(master.getEmailIsAttach().intValue());
//					returnMailList.add(email);
//				}
//				
//				
//			} catch (MessagingException e) {
//				e.printStackTrace();
//				log.info("fail = " + e.getMessage());
//			}
//		} else {
//			log.info("initMail , Group Email not found = " + groupName);
//		}
//
//		return returnMailList;
//	}
}
