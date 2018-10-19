package th.co.baiwa.buckwaframework.security.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

import com.tmb.ecert.auditlog.persistence.dao.AuditLogsDao;
import com.tmb.ecert.batchjob.domain.AuditLog;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.service.AuditLogService;

import th.co.baiwa.buckwaframework.common.util.EcerDateUtils;
import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.LOGIN_STATUS;
import th.co.baiwa.buckwaframework.security.domain.AjaxLoginVo;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RestController
public class AuthenController {
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private AuditLogService auditLogService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenController.class);
	
	@PostMapping("/onloginseccess")
	public AjaxLoginVo onLoginSeccess(HttpServletRequest request) {
		
		AjaxLoginVo vo = new AjaxLoginVo();
		UserDetails user = null;
		Date currentDate = new Date();
		
		try {
			
			HttpSession session = request.getSession(false);
		
			logger.info("onLoginSeccess : {}" ,session.getId());
			
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			vo.setUserId(user.getUserId());
			vo.setFirstName(user.getFirstName());
			vo.setLastName(user.getLastName());
			vo.setUsername(user.getUsername());
			
			for ( GrantedAuthority item : user.getAuthorities()) {
				vo.getRoles().add(item.getAuthority());
			} 
			
			vo.setStatus(LOGIN_STATUS.SUCCESS);
			vo.setAuths(user.getAuths());
			
			List<SessionInformation> inallsess = sessionRegistry.getAllSessions(user, false);
			
			logger.info("{}", inallsess.size());
			
			if(inallsess.size() >= 2) {
				vo.setStatus(LOGIN_STATUS.DUP_LOGIN);
			}
			
		}catch(Exception e) {
			logger.error("AuthenController.onLoginSeccess Error: {} ", e.getMessage());
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.LOGIN_CODE,
					ACTION_AUDITLOG_DESC.LOGIN,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		return vo;	
	}

	@PostMapping("/onloginerror")
	public AjaxLoginVo onloginerror(@RequestParam String error) {
		AjaxLoginVo vo = new AjaxLoginVo();
		try{
			logger.info("onloginerror : {}", error);
			vo.setStatus(error);
		}catch(Exception e) {
			logger.error("AuthenController.onloginerror Error: {} ", e.getMessage());
		}
		return vo;
	}
	
	
	@RequestMapping(value="/onlogout", method = RequestMethod.GET)
	public AjaxLoginVo logoutPage (HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Date currentDate = new Date();
		AjaxLoginVo vo = new AjaxLoginVo();
		UserDetails user = null;
		logger.info("logoutPage : {}" ,session.getId());
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null){
		    	user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		        new SecurityContextLogoutHandler().logout(request, response, auth);
		        sessionRegistry.removeSessionInformation(session.getId());
		    }
			vo.setStatus(LOGIN_STATUS.SUCCESS);
		}catch(Exception e) {
			logger.error("AuthenController.logoutPage Error: {} ", e.getMessage());
		}finally {
			auditLogService.insertAuditLog(ACTION_AUDITLOG.LOGOUT_CODE,
					ACTION_AUDITLOG_DESC.LOGOUT,
					(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					currentDate);
		}
		return vo;
	}
	
}
