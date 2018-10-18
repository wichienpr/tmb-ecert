package com.tmb.ecert.common.service;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tmb.ecert.auditlog.persistence.dao.AuditLogsDao;
import com.tmb.ecert.batchjob.domain.AuditLog;

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
}
