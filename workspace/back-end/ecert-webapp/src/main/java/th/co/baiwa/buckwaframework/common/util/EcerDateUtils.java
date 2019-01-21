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
	public static String DDMMYYYYHHMMSS_EN_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static String EXPRESS_DATE_FORMAT = "dd/MM/yyyy HHmmss";
	public static String yyMMdd_EN_FORMAT = "yyMMdd";
	public static String HHmm_EN_FORMAT = "HH:mm";
	public static String YYYY_EN_FORMAT = "YYYY";
	public static String M_EN_FORMAT = "M";
	public static String MMYYYY_EN_FORMAT = "MM/YYYY";
	public static String YYYYMMDDHHmm_EN_FORMAT = "YYYYMMDDHHmm";

	public static final String[] MONTH_NAMES = { "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" };
	
	
	public static Date parseTranDateFromExpress(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, EXPRESS_DATE_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
			return null;
		}
	}

	public static Date parseDateEN(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, DDMMYYYY_EN_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
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
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, DDMMYYYY_EN_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
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

	public static String formatYYYY_TH(Date date) {
		if (date == null) {
			return null;
		}
		try {
			int yearEN = Integer.parseInt(DateFormatUtils.format(date, YYYY_EN_FORMAT));
			return Integer.toString(yearEN+543);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
			return null;
		}

	}
	public static String formatYYYY_EN(Date date) {
		if (date == null) {
			return null;
		}
		try {
			int yearEN = Integer.parseInt(DateFormatUtils.format(date, YYYY_EN_FORMAT));
			return Integer.toString(yearEN);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
			return null;
		}

	}
	
	public static String formatMMM_TH(Date date) {
		if (date == null) {
			return null;
		}
		
		return MONTH_NAMES[Integer.parseInt(formatM_EN(date))-1];
	}
	
	public static String formatM_EN(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, M_EN_FORMAT);
	}
	
	public static Date parseDateMMYYYYEN(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date, MMYYYY_EN_FORMAT);
		} catch (Exception e) {
			logger.error(" Invalid date format {} ", date);
			return null;
		}
	}
	public static String formatYYYYMMDDHHmm_EN(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, YYYYMMDDHHmm_EN_FORMAT);
	}


}
