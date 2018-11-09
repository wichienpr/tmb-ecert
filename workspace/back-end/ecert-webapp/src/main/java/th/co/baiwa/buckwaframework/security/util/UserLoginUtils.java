package th.co.baiwa.buckwaframework.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.tmb.ecert.common.constant.AuthConstant.AUTHS;
import com.tmb.ecert.common.constant.RoleConstant.ROLES;

import th.co.baiwa.buckwaframework.security.domain.UserBean;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;

public class UserLoginUtils {

	public static UserBean getCurrentUserBean() {
		UserBean userBean = null;

		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserBean) {
				userBean = (UserBean) principal;
			} else if (principal instanceof User) {
				// UnitTest @WithMockUser
				User user = (User) principal;
				userBean = new UserDetails(user.getUsername(), "", user.getAuthorities());
			} else {
				// "anonymous" user
				String username = principal.toString();
				userBean = new UserDetails(username, "", AuthorityUtils.NO_AUTHORITIES);
			}
		} else {
			String username = "NO LOGIN";
			userBean = new UserDetails(username, "", AuthorityUtils.NO_AUTHORITIES);
		}

		return userBean;
	}

	public static String getCurrentUsername() {
		return UserLoginUtils.getCurrentUserBean().getUsername();
	}

	public static UserDetails getCurrentUserLogin() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}

	public static boolean ishasAuth(UserDetails user, AUTHS auth) {
		for (String us : user.getAuths()) {
			if (us.equalsIgnoreCase(auth.toString())) {
				return true;
			}
		}
		return false;
	}

	public static boolean ishasRole(UserDetails user, ROLES role) {
		for (GrantedAuthority us : user.getAuthorities()) {
			if (us.getAuthority().equalsIgnoreCase(role.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean ishasRoleName(UserDetails user, String role) {
		for (GrantedAuthority us : user.getAuthorities()) {
			if (us.getAuthority().equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}

}
