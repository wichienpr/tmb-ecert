package com.tmb.ecert.common.constant;

public class ProjectConstant {

	public static class APPLICATION_LOG_NAME{
		public static final String ECERT_REQFORM = "e-certificate-requestform.log";
		public static final String ECERT_SEARCH_REQFORM = "e-certificate-search-requesform.log";
		public static final String ECERT_PAYMENT = "e-certificate-payment.log";
		public static final String ECERT_REPORT = "e-certificate-report.log";
		public static final String ECERT_BATCHMONITORING = "e-certificate-batchmonitoring.log";
		public static final String ECERT_AUDITLOG = "e-certificate-auditlog.log";
		public static final String ECERT_ROLEMANAGEMENT = "e-certificate-rolemangement.log";	
		public static final String ECERT_PARAMETERCONFIG = "e-certificate-parameterconfig.log";
		public static final String ECERT_EMAILCONFIG = "e-certificate-emailconfig.log";
	}
	
	public static class BACHJOB_LOG_NAME{
		public static final String ECERT_AUTOPAYMENT = "e-certificate-autoretrypayment.log";
		public static final String ECERT_PAYMENTSUMMARYDBD = "e-certificate-jobpaymentdbd.log";
		public static final String ECERT_ONDEMAND = "e-certificate-jobondemand.log";
		public static final String ECERT_GL = "e-certificate-jobGL.log";
		public static final String ECERT_AUDITLOG = "e-certificate-jobauditlog.log";
		public static final String ECERT_HOUSEKEEPING = "e-certificate-jobhousekeeping.log";
		public static final String ECERT_HROFFICECODE = "e-certificate-hrofficecode.log";	
	}
	
	public static class PARAMETER_CONFIG{
		// Parameter Config of House Keeping Batch Job 
		public static final String BATCH_HOUSEKEEPING_DAYS = "hoursekeeping.archive.day";
		public static final String BATCH_HOUSEKEEPING_PATH = "housekeeping.archive.path";
		public static final String BATCH_HOUSEKEEPING_FILENAME = "housekeeping.archive.file";
		public static final String BATCH_HOUSEKEEPING_TARFILE = "housekeeping.archive.tarfile";
		public static final String BATCH_HOUSEKEEPING_AFTERDAY = "housekeeping.archive.afterday";
		public static final String BATCH_HOUSEKEEPING_ROOTPATH = "housekeeping.archivefile.root.path";
		
	}
}
