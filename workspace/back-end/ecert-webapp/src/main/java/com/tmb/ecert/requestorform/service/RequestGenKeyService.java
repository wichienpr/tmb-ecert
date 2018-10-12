package com.tmb.ecert.requestorform.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.requestorform.persistence.dao.RequestGenKeyDao;

@Service
public class RequestGenKeyService {
	
	@Autowired
	private RequestGenKeyDao requestGenKeyDao;
	
	public synchronized String getNextKey() {
		String keyyear = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String keyyearDisplay = DateFormatUtils.format(new Date(), "yyyyMMMdd");
		Integer currentRunningNo = requestGenKeyDao.getNextKey(keyyear);
		int nextRunning = currentRunningNo + 1;
		if(nextRunning == 1) {
		requestGenKeyDao.insertKeyrunning(nextRunning,keyyear);					
		}else {
			requestGenKeyDao.updateKeyrunning(nextRunning,keyyear);					
		}
		return keyyearDisplay + StringUtils.leftPad(String.valueOf(nextRunning), 5, "0");
	}
	
	public synchronized String getNextKeyByChannelID(String channelid) {
		String keyyear = DateFormatUtils.format(new Date(), "yyyyMM");
		Integer currentRunningNo = requestGenKeyDao.getNextKey(keyyear);
		int nextRunning = currentRunningNo + 1;
		if(nextRunning == 1) {
			requestGenKeyDao.insertKeyrunning(nextRunning,keyyear);					
		}else {
			requestGenKeyDao.updateKeyrunning(nextRunning,keyyear);					
		}		return channelid + keyyear + StringUtils.leftPad(String.valueOf(nextRunning), 5, "0");
	}
	
}
