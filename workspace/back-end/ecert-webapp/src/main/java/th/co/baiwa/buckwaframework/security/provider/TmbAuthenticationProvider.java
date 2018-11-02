package th.co.baiwa.buckwaframework.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import th.co.baiwa.buckwaframework.security.domain.TMBPerson;
import th.co.baiwa.buckwaframework.security.service.UserDetailsService;

public class TmbAuthenticationProvider  implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(TmbAuthenticationProvider.class);
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	private UserDetailsService userDetailsService;


//	@Value("${ldap.useLdap}")
//	private String useLdap;

	@Autowired
	private TMBLDAPManager tmbldapManager;



//	@Autowired
//	@Qualifier("passwordEncoder")
//	@Override
//	public void setPasswordEncoder(Object passwordEncoder) {
//		if (FLAG.N_FLAG.equals(useLdap)) {
//			super.setPasswordEncoder(passwordEncoder);
//		}
//	}
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("authenticate");
		Assert.notNull(userDetailsService, "userDetailsService is null : [TmbAuthenticationProvider]");
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		
		UserDetails user = null;

		try {
			TMBPerson tmb = tmbldapManager.isAuthenticate(username, password);
			logger.info("AD is OK. user :{} ,  userid : {} " , username, tmb.getUserid());
			user = userDetailsService.loadUserByUsername(username, tmb);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BadCredentialsException(e.getMessage());
		}
		
		Assert.notNull(user, "UserDetails is null : [TmbAuthenticationProvider]");
		return createSuccessAuthentication(user,authentication,user);
				
	}
	
	protected Authentication createSuccessAuthentication(Object principal,	Authentication authentication, UserDetails user) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				principal, authentication.getCredentials(),
				authoritiesMapper.mapAuthorities(user.getAuthorities()));
		result.setDetails(authentication.getDetails());

		return result;
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }
	

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	
}