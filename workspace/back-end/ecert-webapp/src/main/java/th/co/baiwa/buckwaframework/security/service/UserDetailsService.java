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

import com.tmb.ecert.common.constant.RoleConstant;
import com.tmb.ecert.common.dao.UserProfileDao;

import th.co.baiwa.buckwaframework.security.domain.TMBPerson;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService, TmbUserDetailsService {
	
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
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.ADMIN));
			userDetails.setFirstName("ผู้ดูแลระบบ");
			userDetails.setLastName("ธนาคารทหารไทย");
			userDetails.setUserId("0001");
		}
		if("IT".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.IT));
			userDetails.setFirstName("IT");
			userDetails.setLastName("Technologies");
			userDetails.setUserId("0002");
		}
		if("ISA".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.ISA));
			userDetails.setFirstName("ISA");
			userDetails.setLastName("Security");
			userDetails.setUserId("0003");
		}
		if("REQUESTOR".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.REQUESTOR));
			userDetails.setFirstName("Requestor");
			userDetails.setLastName("RM");
			userDetails.setUserId("0004");
		}
		if("MAKER".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.MAKER));
			userDetails.setFirstName("Maker");
			userDetails.setLastName("TMB Center");
			userDetails.setUserId("0005");
		}
		if("CHECKER".equalsIgnoreCase(username)) {
			grantedAuthorityList.add(new SimpleGrantedAuthority(RoleConstant.ROLE.CHECKER));
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
	
	@Override
	public UserDetails loadUserByUsername(String username, TMBPerson tMBPerson) throws UsernameNotFoundException {
		logger.info("loadUserByUsername username={}", username);
		
		// Initial Granted Authority
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
		//Roles
		List<String> rolesInAd = tMBPerson.getMemberOfs();
		for (String role : rolesInAd) {
			//TODO debug if AD OK
			logger.debug("rolesInAd : {}", role); // TODO plz checked
			grantedAuthorityList.add(new SimpleGrantedAuthority(role));
		}
		
		UserDetails rs = new UserDetails(username, "" ,grantedAuthorityList	);
		rs.setUserId(tMBPerson.getUserid());
		
		String fullName = tMBPerson.getName(); // firstname , lastname
		logger.debug("Full Name : {}", fullName); // TODO plz checked
		String[] spfullName = fullName.split(" ");
		 
		rs.setFirstName(spfullName[0]);
		rs.setLastName(spfullName[1]);
		
		List<String> roles = new ArrayList<>();
		for ( GrantedAuthority g : grantedAuthorityList) {
			roles.add(g.toString());
		}
		
		List<String> auths = userProfileDao.getAuthbyRole(roles);
		rs.setAuths(auths);
		return rs;
	}
	
	
}
