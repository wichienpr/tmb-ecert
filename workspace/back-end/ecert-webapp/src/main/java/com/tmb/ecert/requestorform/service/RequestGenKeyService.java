package com.tmb.ecert.requestorform.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

@Service
public class RequestGenKeyService {
	
	private Map<String,Integer> autokey = new HashMap<>();
	
	public synchronized String getNextKey() {
		String keyyear = DateFormatUtils.format(new Date(), "yyyyMM");
		
		Integer curr = autokey.get(keyyear);
		int rnd = 0;
		if(curr == null) {
			rnd = new Random().nextInt(1000);
			autokey.put(keyyear, rnd);
		}else {
			rnd = curr + 1;
			autokey.remove(keyyear);
			autokey.put(keyyear, rnd);
		}
		
		return keyyear + StringUtils.leftPad(String.valueOf(rnd), 5, "0");
	}
	
}
