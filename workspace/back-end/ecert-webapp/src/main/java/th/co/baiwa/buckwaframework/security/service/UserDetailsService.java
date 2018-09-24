//package th.co.baiwa.buckwaframework.security.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import th.co.baiwa.buckwaframework.security.domain.UserDetails;
//
//@Service
//public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
//	
//	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
//	
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		logger.info("loadUserByUsername username={}", username);
//		
//		// Initial Granted Authority
//		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
//		grantedAuthorityList.add(new SimpleGrantedAuthority(""));
//		
//		UserDetails userDetails = new UserDetails(
//			"",
//			"",
//			true,
//			true,
//			true,
//			true,
//			grantedAuthorityList
//		);
//		
//		return userDetails;
//	}
//	
//}
