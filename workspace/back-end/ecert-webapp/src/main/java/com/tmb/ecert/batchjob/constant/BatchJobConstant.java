package com.tmb.ecert.batchjob.constant;

public class BatchJobConstant {
	
	public static class PARAMETER_CONFIG {
		public static final String BATCH_GL_SUMMARY_IP = "gl.ftpserver.ip";
		public static final String BATCH_GL_SUMMARY_PORT = "gl.ftpserver.port";
		public static final String BATCH_GL_SUMMARY_USERNAME = "gl.ftpserver.username";
		public static final String BATCH_GL_SUMMARY_PASSWORD = "gl.ftpserver.password";
		public static final String BATCH_GL_SUMMARY_PATH = "gl.ftpserver.path";

		public static final String VAT_PERCENT = "vat.percent";
	}
	
	public static class PAYMENT_GL_SUMMARY {
		public static final String BATCH_GL_INDICATOR1 = "batch.gl.indicator1";
		public static final String BATCH_GL_LEDGER = "batch.gl.ledger";
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
		
		public static final class OFFICE_CODE {
			public static final String OFFICE_1092 = "1092";
			public static final String OFFICE_1078 = "1078";
		}
		
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
	}

	public static Integer RERUN_DEFAULT = 1;

}
