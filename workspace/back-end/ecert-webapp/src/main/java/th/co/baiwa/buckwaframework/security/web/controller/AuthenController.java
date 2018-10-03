package th.co.baiwa.buckwaframework.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.LOGIN_STATUS;
import th.co.baiwa.buckwaframework.security.domain.AjaxLoginVo;
import th.co.baiwa.buckwaframework.security.domain.UserBean;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;

@RestController
public class AuthenController {
	@Autowired
	private SessionRegistry sessionRegistry;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenController.class);
	
	@PostMapping("/onloginseccess")
	public AjaxLoginVo onLoginSeccess(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		logger.info("onLoginSeccess : {}" ,session.getId());
		AjaxLoginVo vo = new AjaxLoginVo();
		UserBean user = UserLoginUtils.getCurrentUserBean();
		
		vo.setUserId(user.getUsername());
		vo.setFirstName("Jonh");
		vo.setLastName("smit");
		vo.setUsername(user.getUsername());
		for ( GrantedAuthority item : user.getAuthorities()) {
			vo.getRoles().add(item.getAuthority());
		} 
		vo.setStatus(LOGIN_STATUS.SUCCESS);
		
		
		List<SessionInformation> inallsess = sessionRegistry.getAllSessions(user, false);
		logger.info("{}", inallsess.size());
		
		if(inallsess.size() >= 2) {
			vo.setStatus(LOGIN_STATUS.DUP_LOGIN);
		}
		return vo;
	}

	@PostMapping("/onloginerror")
	public AjaxLoginVo onloginerror(@RequestParam String error) {
		AjaxLoginVo vo = new AjaxLoginVo();
		logger.info("onloginerror : {}", error);
		vo.setStatus(error);
		return vo;
	}
	
	
	@RequestMapping(value="/onlogout", method = RequestMethod.GET)
	public AjaxLoginVo logoutPage (HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		logger.info("logoutPage : {}" ,session.getId());

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	        sessionRegistry.removeSessionInformation(session.getId());
	    }
		AjaxLoginVo vo = new AjaxLoginVo();
		vo.setStatus(LOGIN_STATUS.SUCCESS);
		return vo;
	}
	
}
