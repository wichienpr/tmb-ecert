package th.co.baiwa.buckwaframework.security.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import th.co.baiwa.buckwaframework.security.domain.TMBPerson;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;

public interface TmbUserDetailsService {

	public UserDetails loadUserByUsername(String username, TMBPerson tMBPerson) throws UsernameNotFoundException ;
	
}
