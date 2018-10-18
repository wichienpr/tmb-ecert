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
		
	public static class CHANNEL {
		public static final String BATCH = "BATCH";
	}
	
	public static class SYSTEM{
		public static final String TMB_BANKCODE = "tmb.bankcode";
		public static final String TMB_TRANSACTION_CODE = "tmb.transactiono";
		public static final String TMB_SERVICE_CODE = "tmb.servicecode";
		public static final String DBD_ACCOUNT_NAME = "dbd.account.name";
		public static final String DBD_ACCOUNT_NUMBER = "dbd.accountno";
		public static final String TMB_TRAN_TYPE = "tmb.transaction.type";
	}
	
	public static class ENCODING {
		public static final String TIS_620 = "TIS-620";
	}
	
	public static class ACTION_AUDITLOG{
		public static final String LOGIN_CODE = "70001";
		public static final String LOGOUT_CODE = "70002";
		public static final String REQFORM_01_CODE = "70003";
		public static final String REQFORM_02_CODE = "70004";
		public static final String PAYMENT_CODE = "70005";
		public static final String APPROVE_PAYMENT_CODE = "70006";
		public static final String REJECT_PAYMENT_CODE = "70007";
		public static final String REJECT_REQ_CODE = "70008";
		public static final String CANCEL_REQ_CODE = "70009";
		public static final String RECEIPT_CODE = "70010";
		public static final String COVERSHEET_CODE = "70011";
		public static final String UPLOADCERTIFICATE_CODE = "70012";
		public static final String DOWNLOAD_REQFORM_CODE = "70013";
		public static final String DOWNLOAD_REGISTERID_CODE = "70014";
		public static final String DOWNLOAD_CHGNAME_CODE = "70015";
		public static final String DOWNLOAD_ECERT_CODE = "70016";
	}
	
	public static class ACTION_AUDITLOG_DESC{
		public static final String LOGIN = "auditlog.login";
		public static final String LOGOUT = "auditlog.logout";
		public static final String REQFORM_01 = "auditlog.reqform.01";
		public static final String REQFORM_02 = "70004";
		public static final String PAYMENT_CODE = "70005";
		public static final String APPROVE_PAYMENT_CODE = "70006";
		public static final String REJECT_PAYMENT_CODE = "70007";
		public static final String REJECT_REQ_CODE = "70008";
		public static final String CANCEL_REQ_CODE = "70009";
		public static final String RECEIPT_CODE = "70010";
		public static final String COVERSHEET_CODE = "70011";
		public static final String UPLOADCERTIFICATE_CODE = "70012";
		public static final String DOWNLOAD_REQFORM_CODE = "70013";
		public static final String DOWNLOAD_REGISTERID_CODE = "70014";
		public static final String DOWNLOAD_CHGNAME_CODE = "70015";
		public static final String DOWNLOAD_ECERT_CODE = "70016";
	}
	
}
