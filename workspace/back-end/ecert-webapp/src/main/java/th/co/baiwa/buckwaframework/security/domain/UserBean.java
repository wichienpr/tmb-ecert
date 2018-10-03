package th.co.baiwa.buckwaframework.security.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserBean extends UserDetails {
	
	public String getUsername();
	
	public Collection<GrantedAuthority> getAuthorities();
	
}
