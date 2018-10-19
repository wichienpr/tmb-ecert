package com.tmb.ecert.common.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.auditlog.persistence.dao.AuditLogsDao;
import com.tmb.ecert.batchjob.domain.AuditLog;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class AuditLogService {
	
	private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);
	
	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy HH:mm:ss", Locale.US);
	
	@Autowired
	private AuditLogsDao auditLogsDao;
	
	public boolean insertAuditLog(AuditLog auditLog) {
		boolean result = false;
		try{
			Long id = auditLogsDao.insertAuditLog(auditLog);
			if(id!=null)
				result =  true;
		}catch(Exception ex) {
			log.error("AuditLogService Error = "+ ex.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public boolean insertAuditLog(String actionCode,String actionDesc,UserDetails user,Date actionDate) {
		boolean result = false;
		try{
			AuditLog auditLog = new AuditLog(); 
			auditLog.setActionCode(actionCode);
			String description = String.format(ApplicationCache.getParamValueByName(actionDesc),
					StringUtils.defaultString(user.getUserId()),EcerDateUtils.formatLogDate(actionDate));
			auditLog.setDescription(description);
			auditLog.setCreateById(user.getUserId());
			auditLog.setCreatedByName(user.getFirstName().concat(StringUtils.EMPTY).concat(user.getLastName()));
			Long id = auditLogsDao.insertAuditLog(auditLog);
			if(id!=null)
				result =  true;
		}catch(Exception ex) {
			log.error("AuditLogService Error = "+ ex.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public boolean insertAuditLog(String actionCode,String actionDesc,String tmbReqNo,UserDetails user,Date actionDate) {
		boolean result = false;
		try{
			AuditLog auditLog = new AuditLog(); 
			auditLog.setActionCode(actionCode);
			String description = String.format(ApplicationCache.getParamValueByName(actionDesc),
					StringUtils.defaultString(user.getUserId()),tmbReqNo,EcerDateUtils.formatLogDate(actionDate));
			auditLog.setDescription(description);
			auditLog.setCreateById(user.getUserId());
			auditLog.setCreatedByName(user.getFirstName().concat(StringUtils.EMPTY).concat(user.getLastName()));
			Long id = auditLogsDao.insertAuditLog(auditLog);
			if(id!=null)
				result =  true;
		}catch(Exception ex) {
			log.error("AuditLogService Error = "+ ex.getMessage());
			result = false;
		}
		
		return result;
	}
}
