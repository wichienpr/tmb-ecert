package com.tmb.ecert.batchjob.constant;

public class BatchJobConstant {

	public static class STATUS {
		public static final String SUCCESS = "80001";
		public static final String FAIL = "80002";
		public static final String IN_PROGRESS ="80003";
	}
	
	public static class PARAMETER_CONFIG {
		public static final String BATCH_GL_SUMMARY_IP = "gl.ftpserver.ip";
		public static final String BATCH_GL_SUMMARY_PORT = "gl.ftpserver.port";
		public static final String BATCH_GL_SUMMARY_USERNAME = "gl.ftpserver.username";
		public static final String BATCH_GL_SUMMARY_PASSWORD = "gl.ftpserver.password";
		public static final String BATCH_GL_SUMMARY_PATH = "gl.ftpserver.path";
	}
	
	public static class JOB_TYPE {
		public static final String GL_BATCH_JOB = "60003";
	}

	public static Integer RERUN_DEFAULT = 1;

}
