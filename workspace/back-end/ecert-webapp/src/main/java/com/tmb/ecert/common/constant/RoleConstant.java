package com.tmb.ecert.common.constant;

public class RoleConstant {
	
	public static class ROLE {
		public final static String ADMIN = "DBDS_SysAdmin";
		public final static String IT ="IT";
		public final static String ISA ="ISA";
		public final static String REQUESTOR = "DBDS_Requester";
		public final static String MAKER = "DBDS_Checker";
		public final static String CHECKER ="DBDS_Maker";
		public final static String SUPERCHECKER ="DBDS_Super_Checker";
	}
	
	public static enum ROLES {
		ADMIN,
		IT,
		ISA,
		REQUESTOR,
		MAKER,
		CHECKER
	}
	
	/*public static enum ROLES {
		ADMIN,
		IT,
		ISA,
		REQUESTER,
		REVIEWER,
		APPROVER
	}*/
	
}
