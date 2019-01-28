package th.co.baiwa.buckwaframework.security.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG;
import com.tmb.ecert.common.constant.ProjectConstant.ACTION_AUDITLOG_DESC;
import com.tmb.ecert.common.constant.ProjectConstant.SERVICE_TIMMING;
import com.tmb.ecert.common.service.AuditLogService;

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

	@PostMapping("/forcekick")
	public void forceKickPreviousUser(HttpServletRequest request, HttpServletResponse response) {
		UserDetails user = null;
		try {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<SessionInformation> inallsess = sessionRegistry.getAllSessions(user, false);
			if (inallsess.size() >= 2) {
				String id = inallsess.get(0).getSessionId();
				inallsess.get(0).expireNow();
				sessionRegistry.removeSessionInformation(id);
			}
		} catch (Exception e) {
			logger.error("AuthenController.forceKickPreviousUser Error: {} ", e.getMessage());
		}
	}

	@PostMapping("/onloginseccess")
	public AjaxLoginVo onLoginSeccess(HttpServletRequest request, HttpServletResponse response) {

		AjaxLoginVo vo = new AjaxLoginVo();
		UserDetails user = null;
		Date currentDate = new Date();

		try {

			HttpSession session = request.getSession(false);

			logger.info("onLoginSeccess : {}", session.getId());

			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			vo.setUserId(user.getUserId());
			vo.setFirstName(user.getFirstName());
			vo.setLastName(user.getLastName());
			vo.setUsername(user.getUsername());

			for (GrantedAuthority item : user.getAuthorities()) {
				vo.getRoles().add(item.getAuthority());
			}

			vo.setStatus(LOGIN_STATUS.SUCCESS);
			vo.setAuths(user.getAuths());

			List<SessionInformation> inallsess = sessionRegistry.getAllSessions(user, false);

			logger.info("{}", inallsess.size());

			if (inallsess.size() >= 2) {
				vo.setStatus(LOGIN_STATUS.DUP_LOGIN);
				if (!session.getId().equals(inallsess.get(1).getSessionId())) { // Currently user
					this.forceLogOut(request, response);
					vo = new AjaxLoginVo();
					vo.setStatus(LOGIN_STATUS.FAIL);
					return vo;
				}
			} else {
				if (!session.getId().equals(inallsess.get(0).getSessionId())) { // Previously user
					this.forceLogOut(request, response);
					vo = new AjaxLoginVo();
					vo.setStatus(LOGIN_STATUS.FAIL);
					return vo;
				}
			}

			// check out off service
			String timefrom = ApplicationCache.getParamValueByName(SERVICE_TIMMING.SHUTDOWN_TIME_FROM);
			String timeto = ApplicationCache.getParamValueByName(SERVICE_TIMMING.SHUTDOWN_TIME_TO);
			String currentTime = DateFormatUtils.format(new Date(), "HHmm").trim();

			String strFrom = timefrom.replace(":", "").trim();
			String strTo = timeto.replace(":", "").trim();
			if (NumberUtils.toInt(strFrom) <= NumberUtils.toInt(currentTime)
					&& NumberUtils.toInt(strTo) >= NumberUtils.toInt(currentTime)) {
				logger.info("ON SERVICE");
				auditLogService.insertAuditLog(ACTION_AUDITLOG.LOGIN_CODE, ACTION_AUDITLOG_DESC.LOGIN,
						(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
						currentDate);
			} else {
				logger.info("OUTOFF SERVICE");
				vo.setStatus(LOGIN_STATUS.OUTOFF_SERVICE);
				vo.setDiscription(timefrom + "  ถึงเวลา " + timeto);
				this.forceLogOut(request, response);
			}
		} catch (Exception e) {
			logger.error("AuthenController.onLoginSeccess Error: {} ", e.getMessage());
		}
		return vo;
	}
	
	@PostMapping("/getUserlogin")
	public AjaxLoginVo getUserSession(HttpServletRequest request, HttpServletResponse response) {

		AjaxLoginVo vo = new AjaxLoginVo();
		UserDetails user = null;
		Date currentDate = new Date();

		try {

			HttpSession session = request.getSession(false);

			logger.info("onLoginSeccess : {}", session.getId());

			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			vo.setUserId(user.getUserId());
			vo.setFirstName(user.getFirstName());
			vo.setLastName(user.getLastName());
			vo.setUsername(user.getUsername());

			for (GrantedAuthority item : user.getAuthorities()) {
				vo.getRoles().add(item.getAuthority());
			}

			vo.setStatus(LOGIN_STATUS.SUCCESS);
			vo.setAuths(user.getAuths());

			List<SessionInformation> inallsess = sessionRegistry.getAllSessions(user, false);

			logger.info("{}", inallsess.size());

			if (inallsess.size() >= 2) {
				vo.setStatus(LOGIN_STATUS.DUP_LOGIN);
				if (!session.getId().equals(inallsess.get(1).getSessionId())) { // Currently user
					this.forceLogOut(request, response);
					vo = new AjaxLoginVo();
					vo.setStatus(LOGIN_STATUS.FAIL);
					return vo;
				}
			} else {
				if (!session.getId().equals(inallsess.get(0).getSessionId())) { // Previously user
					this.forceLogOut(request, response);
					vo = new AjaxLoginVo();
					vo.setStatus(LOGIN_STATUS.FAIL);
					return vo;
				}
			}

			// check out off service
			String timefrom = ApplicationCache.getParamValueByName(SERVICE_TIMMING.SHUTDOWN_TIME_FROM);
			String timeto = ApplicationCache.getParamValueByName(SERVICE_TIMMING.SHUTDOWN_TIME_TO);
			String currentTime = DateFormatUtils.format(new Date(), "HHmm").trim();

			String strFrom = timefrom.replace(":", "").trim();
			String strTo = timeto.replace(":", "").trim();
			if (NumberUtils.toInt(strFrom) <= NumberUtils.toInt(currentTime)
					&& NumberUtils.toInt(strTo) >= NumberUtils.toInt(currentTime)) {
				logger.info("ON SERVICE");
//				auditLogService.insertAuditLog(ACTION_AUDITLOG.LOGIN_CODE, ACTION_AUDITLOG_DESC.LOGIN,
//						(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//						currentDate);
			} else {
				logger.info("OUTOFF SERVICE");
				vo.setStatus(LOGIN_STATUS.OUTOFF_SERVICE);
				vo.setDiscription(timefrom + "  ถึงเวลา " + timeto);
				this.forceLogOut(request, response);
			}
		} catch (Exception e) {
			logger.error("AuthenController.onLoginSeccess Error: {} ", e.getMessage());
		}
		return vo;
	}

	@PostMapping("/onloginerror")
	public AjaxLoginVo onloginerror(@RequestParam String error) {
		AjaxLoginVo vo = new AjaxLoginVo();
		try {
			logger.info("onloginerror : {}", error);
			vo.setStatus(error);
		} catch (Exception e) {
			logger.error("AuthenController.onloginerror Error: {} ", e.getMessage());
		}
		return vo;
	}

	@RequestMapping(value = "/onlogout", method = RequestMethod.GET)
	public AjaxLoginVo logoutPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Date currentDate = new Date();
		AjaxLoginVo vo = new AjaxLoginVo();
		UserDetails user = null;
		logger.info("logoutPage : {}", session.getId());
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {

				user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				auditLogService.insertAuditLog(ACTION_AUDITLOG.LOGOUT_CODE, ACTION_AUDITLOG_DESC.LOGOUT, user,
						currentDate);

				new SecurityContextLogoutHandler().logout(request, response, auth);
				sessionRegistry.removeSessionInformation(session.getId());
			}
			vo.setStatus(LOGIN_STATUS.SUCCESS);
		} catch (Exception e) {
			logger.error("AuthenController.logoutPage Error: {} ", e.getMessage());
		}

		return vo;
	}

	// this for login out off service only not call
	private void forceLogOut(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			HttpSession session = request.getSession();
			new SecurityContextLogoutHandler().logout(request, response, auth);
			sessionRegistry.removeSessionInformation(session.getId());
			logger.info("forceLogOut : {}", session.getId());
		}
	}

}
