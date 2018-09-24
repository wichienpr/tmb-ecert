package th.co.baiwa.buckwaframework.security.constant;

public class SecurityConstants {
	
	public static final class URL {
		public static final String LOGIN_WEB = "/backend/login.htm";
		public static final String LOGIN_WEB_FAILURE = LOGIN_WEB + "?error";
		public static final String LOGIN_WEB_SUCCESS = "/backend/welcome.htm";
		public static final String LOGIN_REST = "/api/security/login";
	}
	
	public static final String USERNAME_PARAM = "username";
	public static final String PASSWORD_PARAM = "password";
	
	// Using in Security Module, for checking this User is authenticate already
	public static final class ROLE {
		public static final String USER = "USER";
		public static final String ADMIN = "ADMIN";
	}
}
