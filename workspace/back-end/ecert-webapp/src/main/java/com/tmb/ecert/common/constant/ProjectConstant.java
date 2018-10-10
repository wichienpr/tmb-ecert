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
		public static final String BATCH_HOUSEKEEPING_RMFILE = "housekeeping.archive.removefile";
		
		//Parameter Config of Audit Log Batch Job
		public static final String BATCH_AUDITLOG_ACTIONCODE = "auditlog.actioncode";
		public static final String BATCH_AUDITLOG_LOCALPATH = "auditlog.local.path";
		public static final String BATCH_AUDITLOG_FTPPATH = "auditlog.ftpserver.path";
		public static final String BATCH_AUDITLOG_FTPHOST = "auditlog.ftpserver.host";
		public static final String BATCH_AUDITLOG_FTPUSERNAME = "auditlog.ftpserver.username";
		public static final String BATCH_AUDITLOG_FTPPWD = "auditlog.ftpserver.password";
		public static final String BATCH_AUDITLOG_FILENAME = "auditlog.filename";
		
		//Parameter Config of Payment DBD Summary Batch Job
		public static final String BATCH_DBDSUMMARY_FTPPATH = "ftp.dbd.path";
		public static final String BATCH_DBDSUMMARY_FTPHOST = "ftp.dbd.ip";
		public static final String BATCH_DBDSUMMARY_FTPUSERNAME = "ftp.dbd.username";
		public static final String BATCH_DBDSUMMARY_FTPPWD = "ftp.dbd.password";
		public static final String BATCH_DBDSUMMARY_ARCHIVEPATH = "payment.summary.archive.path";
		
	}
	
	public static class CHANNEL {
		public static final String BATCH = "BATCH";
	}
	
	public static class SYSTEM{
		public static final String TMB_BANKCODE = "tmb.bankcode";
		public static final String TMB_TRANSACTION_CODE = "tmb.transactiono";
		public static final String TMB_SERVICE_CODE = "tmb.servicecode";
		public static final String DBD_ACCOUNT_NAME = "dbd.account.name";
		public static final String DBD_ACCOUNT_NUMBER = "dbd.accountno ";
		public static final String TMB_TRAN_TYPE = "tmb.transaction.type ";
	}
	
	public static class ENCODING {
		public static final String TIS_620 = "TIS-620";
	}
}
