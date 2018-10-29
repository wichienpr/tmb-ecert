package th.co.baiwa.buckwaframework.common.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EcerDateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EcerDateUtils.class);
	
	public static String DDMMYYYY_EN_FORMAT = "dd/MM/yyyy";
	public static String DDMMYYYYHHMMSS_EN_FORMAT = "dd/MM/yyyy HH:mm:ss" ;
	public static String yyMMdd_EN_FORMAT = "yyMMdd";
	public static String HHmm_EN_FORMAT = "HH:mm";
	
	public static Date parseDateEN(String date) {
		if(StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, DDMMYYYY_EN_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ",date);
			return null;
		}
	}
	
	public static String formatLogDate(Date date) {
		if (date == null) {
			return null;
		}
		
		return DateFormatUtils.format(date, DDMMYYYYHHMMSS_EN_FORMAT);
	}
	
	public static String formatDDMMYYYYDate(Date date) {
		if (date == null) {
			return null;
		}
		
		return DateFormatUtils.format(date, DDMMYYYY_EN_FORMAT);
	}
	
	public static Date parseDDMMYYYYEN(String date) {
		if(StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, DDMMYYYY_EN_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ",date);
			return null;
		}
	}
	
	public static String formatYYMMDDDate(Date date) {
		if (date == null) {
			return null;
		}
		
		return DateFormatUtils.format(date, yyMMdd_EN_FORMAT);
	}
	
	public static String formatHHMM(Date date) {
		if (date == null) {
			return null;
		}
		
		return DateFormatUtils.format(date, HHmm_EN_FORMAT);
	}
	
	
}
