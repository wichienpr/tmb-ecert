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
		public static final String BATCH_AUDITLOG_FTPPATH = "auditlog.ftp.path";
		public static final String BATCH_AUDITLOG_FTPHOST = "auditlog.ftp.host";
		public static final String BATCH_AUDITLOG_FTPUSERNAME = "auditlog.ftp.username";
		public static final String BATCH_AUDITLOG_FTPPWD = "auditlog.ftp.password";
		public static final String BATCH_AUDITLOG_FILENAME = "auditlog.filename";
		
		//Parameter Config of Payment DBD Summary Batch Job
		public static final String BATCH_DBDSUMMARY_FTPPATH = "dbd.ftp.path";
		public static final String BATCH_DBDSUMMARY_FTPHOST = "dbd.ftp.ip";
		public static final String BATCH_DBDSUMMARY_FTPUSERNAME = "dbd.ftp.username";
		public static final String BATCH_DBDSUMMARY_FTPPWD = "dbd.ftp.password";
		public static final String BATCH_DBDSUMMARY_ARCHIVEPATH = "dbd.summary.archive.path";
		
		//Parameter Config of Ondemand Payment Summary Batch Job
		public static final String BATCH_ONDEMANDSUMMARY_FTPPATH = "ondemand.ftp.path";
		public static final String BATCH_ONDEMANDSUMMARY_FTPHOST = "ondemand.ftp.ip";
		public static final String BATCH_ONDEMANDSUMMARY_FTPUSERNAME = "ondemand.ftp.username";
		public static final String BATCH_ONDEMANDSUMMARY_FTPPWD = "ondemand.ftp.password";
		public static final String BATCH_ONDEMANDSUMMARY_ARCHIVEPATH = "ondemand.summary.archive.path";
		public static final String BATCH_ONDEMANDSUMMARY_FILENAME = "ondemand.summary.filename";
		public static final String BATCH_ONDEMANDSUMMARY_SYSNAME = "ondemand.sysname";
		
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
	
	public static class ONDEMAND{
		public static final String REQ_DATE = "REQ DATE";
		public static final String TMB_REQ_NO = "TMB REQ NO.";
		public static final String ORG_ID = "ORG ID";
		public static final String SEGMENT = "SEGMENT";
		public static final String PAID_TYPE = "PAID TYPE";
		public static final String ACCT_NO = "ACCT NO.";
		public static final String AMOUNT = "AMOUNT";
		public static final String RUN_DATE = "RUN DATE";
		public static final String AS_DATE = "AS DATE";
		public static final String PAGE="PAGE";
		public static final String TMBBANK="ondemand.bank.name";
		public static final String SYSNAME="ondemand.sysname";
		public static final String REPORTNAME="ondemand.reportname";
		public static final String OFFICECODE="ondemand.officecode";
		public static final String BRANCHNAME="ondemand.branchname";
		public static final String REPORTID = "ondemand.reportid";
		public static final int NUMBER_OF_PAGE = 43;
	}
	
	public static class JOBMONITORING_TYPE{
		public final static String DBDSUMMARY_TYPE ="60001";
		public final static String ONDEMAND_TYPE ="60002";
		public final static String AUDITLOG_TYPE ="60005";
		public final static String HOUSEKEEPING_TYPE ="60006";
		
	}
}
