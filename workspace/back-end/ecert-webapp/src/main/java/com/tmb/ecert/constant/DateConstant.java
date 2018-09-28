package com.tmb.ecert.constant;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DateConstant {

	private static Logger log = LoggerFactory.getLogger(DateConstant.class);
	
	public static final Locale LOCAL_TH = new Locale("th", "TH");
	public static final Locale LOCAL_EN = new Locale("en", "US");

	public final static String DD_MM_YYYY_HH_mm = "dd/MM/yyyy HH:mm";
	public final static String DD_MM_YYYY = "dd/MM/yyyy";
	public static final String MM_YYYY = "MM/yyyy";
	public final static String YYYY = "yyyy";
	
	public static final String YYYYMMDD = "yyyyMMdd";
	
//	public static final String[] MONTH_SHOT_NAMES = { "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.","ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค." };
//	public static final String[] MONTH_NAMES = { "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" };
	
	public static Date  convertStringDDMMYYYYHHmmToDate(String DDMMYYYYHHmm) {
		Date date = null;
		try {
			if (DDMMYYYYHHmm != null) {
				date = DateUtils.parseDate(DDMMYYYYHHmm, DD_MM_YYYY_HH_mm);
			}
		} catch (Exception e) {
			log.error("Error convertDateToStrDDMMYYYY : ", e);
		}
		return date;
	}

	public static String convertDateToStrDDMMYYYYHHmm(Date date) {
		String dateString = "";
		try {
			if (date != null) {
				dateString = DateFormatUtils.format(date, DD_MM_YYYY_HH_mm);
			}
		} catch (Exception e) {
			log.error("Error convertDateToStrDDMMYYYY : ", e);
		}
		return dateString;
	}

	public static String convertStrDDMMYYYYToStrYYYYMMDD(String ddmmyyyy) {
		String dateString = "";
		try {
			if (StringUtils.isNotBlank(ddmmyyyy)) {
				Date date = DateUtils.parseDate(StringUtils.trim(ddmmyyyy), DD_MM_YYYY);
				dateString = DateFormatUtils.format(date, YYYYMMDD);
			}
		} catch (Exception e) {
			log.error("Error convertStrDDMMYYYYToStrYYYYMMDD : ", e);
			e.printStackTrace();
		}
		return dateString;
	}

	public static String convertDateToStrDDMMYYYY(Date date) {
		String dateString = "";
		try {
			if (date != null) {
				dateString = DateFormatUtils.format(date, DD_MM_YYYY);
			}
		} catch (Exception e) {
			log.error("Error convertDateToStrDDMMYYYY : ", e);
		}
		return dateString;
	}

	public static Date convertStrDDMMYYYYToDate(String ddMMyyyy) {
		Date dateString = null;
		try {
			if (StringUtils.isNotBlank(ddMMyyyy)) {
				dateString = DateUtils.parseDate(ddMMyyyy, DD_MM_YYYY);
			}
		} catch (Exception e) {
			log.error("Error convertStrDDMMYYYYToDate : ", e);
		}
		return dateString;
	}

	public static Date convertStrYYYYToDate(String yyyy) {
		Date date = null;
		try {
			if (StringUtils.isNotBlank(yyyy)) {
				date = DateUtils.parseDate(yyyy, YYYY);
			}
		} catch (Exception e) {
			log.error("Error convertStrYYYYToDate : ", e);
		}
		return date;
	}
	



}
