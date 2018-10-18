package th.co.baiwa.buckwaframework.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.dao.UserProfileDao;

import th.co.baiwa.buckwaframework.security.domain.UserDetails;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserProfileDao userProfileDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername username={}", username);
		
		// Initial Granted Authority
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
//		passwordEncoder.encode("password")
		UserDetails userDetails = new UserDetails(username,"",grantedAuthorityList);
		if("ADMIN".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("ADMIN"));
			userDetails.setFirstName("ผู้ดูแลระบบ");
			userDetails.setLastName("ธนาคารทหารไทย");
			userDetails.setUserId("0001");
		}
		if("IT".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("IT"));
			userDetails.setFirstName("IT");
			userDetails.setLastName("Technologies");
			userDetails.setUserId("0002");
		}
		if("ISA".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("ISA"));
			userDetails.setFirstName("ISA");
			userDetails.setLastName("Security");
			userDetails.setUserId("0003");
		}
		if("REQUESTOR".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("REQUESTOR"));
			userDetails.setFirstName("Requestor");
			userDetails.setLastName("RM");
			userDetails.setUserId("0004");
		}
		if("MAKER".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("MAKER"));
			userDetails.setFirstName("Maker");
			userDetails.setLastName("TMB Center");
			userDetails.setUserId("0005");
		}
		if("CHECKER".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority("CHECKER"));
			userDetails.setFirstName("Checker");
			userDetails.setLastName("TMB Center");
			userDetails.setUserId("0006");
		}
		UserDetails rs = new UserDetails(username, passwordEncoder.encode("password"),grantedAuthorityList	);
		rs.setUserId(userDetails.getUserId());
		rs.setFirstName(userDetails.getFirstName());
		rs.setLastName(userDetails.getLastName());
		
		List<String> roles = new ArrayList<>();
		for ( GrantedAuthority g : grantedAuthorityList) {
			roles.add(g.toString());
		}
		
		List<String> auths = userProfileDao.getAuthbyRole(roles);
		rs.setAuths(auths);
		return rs;
	}
	
}
