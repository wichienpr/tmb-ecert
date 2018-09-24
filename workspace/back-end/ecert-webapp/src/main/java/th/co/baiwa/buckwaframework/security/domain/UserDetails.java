//package th.co.baiwa.buckwaframework.security.domain;
//
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//public class UserDetails extends User implements UserBean {
//	
//	private static final long serialVersionUID = 2637807472705815470L;
//	
//	private Long userId;
//	// Add More Information about USER here.
//	
//	private String[] exciseBaseControl ;
//	
//	// Constructor
//	public UserDetails(String username, String password, boolean enabled,boolean accountNonExpired,
//			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
//		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//	}
//	
//	// Constructor
//	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//		super(username, password, authorities);
//	}
//	
//	
//	// ==================================================
//	// Getter & Setter Method
//	// ==================================================
//	public Long getUserId() {
//		return userId;
//	}
//	
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//
//	public String[] getExciseBaseControl() {
//		return exciseBaseControl;
//	}
//
//	public void setExciseBaseControl(String[] exciseBaseControl) {
//		this.exciseBaseControl = exciseBaseControl;
//	}
//	
//}
