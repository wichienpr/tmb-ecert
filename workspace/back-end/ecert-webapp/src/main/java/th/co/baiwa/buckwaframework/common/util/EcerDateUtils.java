package th.co.baiwa.buckwaframework.common.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;

public class EcerDateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EcerDateUtils.class);
	
	public static String DDMMYYYY_EN_FORMAT = "dd/MM/yyyy";
	public static String DDMMYYYYHHMMSS_EN_FORMAT = "dd/MM/yyyy HH:mm:SS" ;
	
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
}
