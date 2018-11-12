package com.tmb.ecert.checkrequeststatus.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.ReqidGenKeyDao;

@Service
public class ReqidGenKeyService {
	
	@Autowired
	private ReqidGenKeyDao reqidGenKeyDao;
	
	public synchronized String getNextKey() {
		String keyyear = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String keyyearDisplay = DateFormatUtils.format(new Date(), "yyyyMMdd");
		Integer currentRunningNo = reqidGenKeyDao.getRecNextKey(keyyear);
		int nextRunning = currentRunningNo + 1;
		if (nextRunning == 1) {
			reqidGenKeyDao.insertRecKeyrunning(nextRunning, keyyear);
		} else {
			reqidGenKeyDao.updateRecKeyrunning(nextRunning, keyyear);
		}
		return keyyearDisplay + StringUtils.leftPad(String.valueOf(nextRunning), 6, "0");
	}

}
