package com.tmb.ecert.requestorform.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.requestorform.persistence.dao.ReceiptGenKeyDao;

@Service
public class ReceiptGenKeyService {

	@Autowired
	private ReceiptGenKeyDao receiptGenKeyDao;

	public synchronized String getNextKey() {
		String keyyear = DateFormatUtils.format(new Date(), "yyyyMM");
		keyyear = keyyear + "00";
		String keyyearDisplay = DateFormatUtils.format(new Date(), "yyyyMM");
		Integer currentRunningNo = receiptGenKeyDao.getRecNextKey(keyyear);
		int nextRunning = currentRunningNo + 1;
		if (nextRunning == 1) {
			receiptGenKeyDao.insertRecKeyrunning(nextRunning, keyyear);
		} else {
			receiptGenKeyDao.updateRecKeyrunning(nextRunning, keyyear);
		}
		return keyyearDisplay + StringUtils.leftPad(String.valueOf(nextRunning), 4, "0");
	}

}
