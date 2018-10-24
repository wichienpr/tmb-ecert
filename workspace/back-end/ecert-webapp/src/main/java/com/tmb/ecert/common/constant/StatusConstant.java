package com.tmb.ecert.common.constant;

public class StatusConstant {
	public final static String NEW_REQUEST = "10001";
	public final static String PAYMENT_PROCESSING = "10002";
	public final static String REFUSE_REQUEST = "10003";
	public final static String CANCEL_REQUEST = "10004";
	public final static String WAIT_PAYMENT_APPROVAL = "10005";
	public final static String PAYMENT_APPROVALS = "10006";
	public final static String REJECT_PAYMENT = "10007";
	public final static String PAYMENT_FAILED = "10008";
	public final static String WAIT_UPLOAD_CERTIFICATE = "10009";
	public final static String SUCCEED = "10010";
	public final static String WAIT_SAVE_REQUEST = "10011";

	public static class JOBMONITORING {

		public final static String SUCCESS = "80001";
		public final static String FAILED = "80002";

		public final static String BATCH_DBD = "60001";
		public final static String BATCH_ONDEMAND = "60002";
		public final static String BATCH_GL = "60003";
		public final static String BATCH_HR = "60004";
		public final static String BATCH_AUDILOG = "60005";
		public final static String BATCH_HOUSEKEEP = "60006";
	}

	public static class PAYMENT_STATUS {
		public final static String TMB = "TMB";
		public final static String DBD = "DBD";
		public final static String SUCCESS = "0";
		public final static String SUCCESS_MSG = "SUCCESS";
		public final static String ERROR_MSG = "ERROR";
		public final static String PAY_TMB_DBD = "30001";
		public final static String PAY_DBD = "30002";
		public final static String PAY_TMB = "30003";
		public final static String PAY_NONE = "30004";
	}

	public static class ROLE_STATUS {
		public final static String STATUS_ALL = "90001";
		public final static String STATUS_ACTIVE = "90002";
		public final static String STATUS_INACTIVE = "90003";
	}

	public static class IMPORT_ECM_WS {
		public final static String SEGMENTCODE_MAP_1 = "20005";
		public final static String SEGMENTCODE_MAP_2 = "20004";
		public final static String SEGMENTCODE_MAP_3 = "20003";
		public final static String SEGMENTCODE_MAP_4 = "20002";

		public final static String IMPORT_STATUS_SUCCESS = "0000";
		public final static String CHECK_STATUS_PARTIAL_SUCCESS = "0001";
		public final static String CHECK_STATUS_SUCCESS = "0";
	}
}
