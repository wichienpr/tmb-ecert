package com.tmb.ecert.batchjob.constant;

public class BatchJobConstant {
	
	public static class PARAMETER_CONFIG {
		public static final String BATCH_GL_SUMMARY_IP = "gl.ftpserver.ip";
		public static final String BATCH_GL_SUMMARY_PORT = "gl.ftpserver.port";
		public static final String BATCH_GL_SUMMARY_USERNAME = "gl.ftpserver.username";
		public static final String BATCH_GL_SUMMARY_PASSWORD = "gl.ftpserver.password";
		public static final String BATCH_GL_SUMMARY_PATH = "gl.ftpserver.path";
		public static final String BATCH_GL_ARCHIVE_FILE_PATH = "gl.archive.file.path";

		public static final String VAT_PERCENT = "vat.percent";
		
		public static final String BATCH_HROFFICECODE_IP = "hrofficecode.ftpserver.ip";
		public static final String BATCH_HROFFICECODE_PORT = "hrofficecode.ftpserver.port";
		public static final String BATCH_HROFFICECODE_USERNAME = "hrofficecode.ftpserver.username";
		public static final String BATCH_HROFFICECODE_PASSWORD = "hrofficecode.ftpserver.password";
		public static final String BATCH_HROFFICECODE_PATH = "hrofficecode.ftpserver.path";
		public static final String BATCH_HROFFICECODE_FILENAME = "hrofficecode.ftpserver.filename";
		public static final String BATCH_HROFFICECODE_ARCHIVE_FILE_PATH = "hrofficecode.archive.file.path";
		
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
	
	public static class BACHJOB_LOG_NAME{
		public static final String ECERT_AUTOPAYMENT = "e-certificate-autoretrypayment.log";
		public static final String ECERT_PAYMENTSUMMARYDBD = "e-certificate-jobpaymentdbd.log";
		public static final String ECERT_ONDEMAND = "e-certificate-jobondemand.log";
		public static final String ECERT_GL = "e-certificate-jobGL.log";
		public static final String ECERT_AUDITLOG = "e-certificate-jobauditlog.log";
		public static final String ECERT_HOUSEKEEPING = "e-certificate-jobhousekeeping.log";
		public static final String ECERT_HROFFICECODE = "e-certificate-jobhrofficecode.log";	
		public static final String ECERT_IMPORTECM = "e-certificate-jobimportecm.log";	
	}
	
	public static class PAYMENT_GL_SUMMARY {
		public static final String BATCH_GL_INDICATOR1 = "batch.gl.indicator1";
		public static final String BATCH_GL_LEDGER1 = "batch.gl.ledger1";
		public static final String BATCH_GL_SOURCECODE1 = "batch.gl.sourcecode1";
		public static final String BATCH_GL_EVENTDESC1 = "batch.gl.eventdesc1";
		public static final String BATCH_GL_ENTITYCODE1 = "batch.gl.entitycode1";
		public static final String BATCH_GL_INTERCODE1 = "batch.gl.intercode1";
		public static final String BATCH_GL_OWNDER_BRANCHCODE1 = "batch.gl.owner.branchcode1";
		public static final String BATCH_GL_ENTRY_BRANCHCODE = "batch.gl.entry.branchcode";
		public static final String BATCH_GL_DESTINATION_BRANCHCODE1 = "batch.gl.destination.branchcode1";
		public static final String BATCH_GL_DESCTICATION_OFFICECODE1 = "batch.gl.destication.officecode1";
		public static final String BATCH_GL_PRODUCTCODE1 = "batch.gl.productcode1";
		public static final String BATCH_GL_CHANNELCODE1 = "batch.gl.channelcode1";
		public static final String BATCH_GL_PROJECTCODE1 = "batch.gl.projectcode1";
		public static final String BATCH_GL_TAXCODE1 = "batch.gl.taxcode1";
		public static final String BATCH_GL_CURRENCY_CODE1 = "batch.gl.currency.code1";
		public static final String BATCH_GL_RD_PLACE = "batch.gl.rd.place";
		public static final String BATCH_GL_AMT_CURRENCY_CODE = "batch.gl.amt.currency.code";
		
		public static final String BATCH_GL_INDICATOR2 = "batch.gl.indicator2";
		public static final String BATCH_GL_LEDGER2 = "batch.gl.ledger2";
		public static final String BATCH_GL_SOURCECODE2 = "batch.gl.sourcecode2";
		public static final String BATCH_GL_EVENTDESC2 = "batch.gl.eventdesc2";
		public static final String BATCH_GL_ENTITYCODE2 = "batch.gl.entitycode2";
		public static final String BATCH_GL_INTERCODE2 = "batch.gl.intercode2";
		public static final String BATCH_GL_OWNDER_BRANCHCODE2 = "batch.gl.owner.branchcode2";
		public static final String BATCH_GL_DESTINATION_BRANCHCODE2 = "batch.gl.destination.branchcode2";
		public static final String BATCH_GL_DESCTICATION_OFFICECODE2 = "batch.gl.destication.officecode2";
		public static final String BATCH_GL_PRODUCTCODE2 = "batch.gl.productcode2";
		public static final String BATCH_GL_CHANNELCODE2 = "batch.gl.channelcode2";
		public static final String BATCH_GL_PROJECTCODE2 = "batch.gl.projectcode2";
		public static final String BATCH_GL_TAXCODE2 = "batch.gl.taxcode2";
		public static final String BATCH_GL_CURRENCY_CODE2 = "batch.gl.currency.code2";

		public static final String BATCH_GL_HEADER_SOURCE_FROM = "gl.header.source.from";
		public static final String BATCH_GL_HEADER_SOURCE_TO = "gl.header.source.to";
		public static final String BATCH_GL_HEADER_TYPE_DATA = "gl.header.type.data";
		public static final String BATCH_GL_HEADER_FILE_TYPE = "gl.header.file.type";
	}
	
	public static class HROFFICE_CODE {
		public static final String BATCH_HROFFICECODE_FILE_TYPE = "hrofficecode.file.type";
		public static final String BATCH_HROFFICECODE_ACTIVE_STATUS = "A";
	}
	
	public static final class OFFICE_CODE {
		public static final String OFFICE_1092 = "1092";
		public static final String OFFICE_1078 = "1078";
		public static final String OFFICE_SEG_0300700000 = "0300700000";
		public static final String OFFICE_SEG_0201200000 = "0201200000";
		public static final String OFFICE_SEG_DEFULT = "0000000000";
	}
	
	public static class ECERT_CUSTSEGMENT_CODE {
		public static final String SB = "20002";
		public static final String BB = "20003";
		public static final String CB = "20004";
		public static final String MB = "20005";
	}
	
	public static class GL_CUSTSEGMENT_CODE {
		public static final String SB = "400";
		public static final String BB = "300";
		public static final String CB = "200";
		public static final String MB = "100";
	}
	
	public static class PAID_TYPE {
		public static final String CUSTOMER_PAY_DBD_TMB = "30001";
		public static final String CUSTOMER_PAY_DBD = "30002";
		public static final String TMB_PAY_DBD_TMB = "30003";
	}
	
	public static class JOB_TYPE {
		public static final String GL_BATCH_JOB = "60003";
		public static final String HR_BATCH_JOB = "60004";
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
		public final static String IMPORT_ECM ="60007";
	}

	public static Integer RERUN_DEFAULT = 1;
	public static Integer BATCHRUN_DEFAULT = 0;

}
