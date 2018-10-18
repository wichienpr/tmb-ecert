//package th.co.baiwa.buckwaframework.security.provider;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component("jdbcAuthenticationProvider")
//public class JdbcAuthenticationProvider extends DaoAuthenticationProvider {
//	
//	private static final Logger logger = LoggerFactory.getLogger(JdbcAuthenticationProvider.class);
//	
//	//private final UserAttemptService userAttemptService;
//	
//	@Autowired
//	public JdbcAuthenticationProvider(
//			@Qualifier("userDetailsService") UserDetailsService userDetailsService
////			,@Qualifier("passwordEncoder") PasswordEncoder passwordEncoder
//			) {
//		super.setUserDetailsService(userDetailsService);
////		super.setPasswordEncoder(passwordEncoder);
//		//this.userAttemptService = userAttemptService;
//	}
//
//	@Override
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//		logger.info("authenticate");
//		
//		try {
//			
//			Authentication auth = super.authenticate(authentication);
//			
//			// if reach here, means login success, else exception will be thrown
//			// reset the ADM_USER_ATTEMPT.ATTEMPTS
//			//userAttemptService.resetFailAttempt(authentication.getName());
//			if("admin".equals(authentication.getName()) && "password".equals(authentication.getName())) {
//				
//			}else {
//				
//			}
//			return auth;
//			
//		} catch (BadCredentialsException e) {
//			
//			// invalid login, update to ADM_USER_ATTEMPT.ATTEMPTS
//			//userAttemptService.updateFailAttempt(authentication.getName());
//			
//			logger.error(e.getMessage(), e);
//			throw e;
//			
//		} 
//		
//	}
//	
//}
