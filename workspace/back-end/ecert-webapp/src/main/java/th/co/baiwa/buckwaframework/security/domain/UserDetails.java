package th.co.baiwa.buckwaframework.security.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetails extends User implements UserBean {
	
	private static final long serialVersionUID = 2637807472705815470L;
	
	private String userId;
	// Add More Information about USER here.
	private List<String> auths = new ArrayList<>();
	private String firstName;
	private String lastName;
	
	// Constructor
	public UserDetails(String username, String password, boolean enabled,boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	// Constructor
	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	
	// ==================================================
	// Getter & Setter Method
	// ==================================================
	
	
	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
