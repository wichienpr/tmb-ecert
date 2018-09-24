//package th.co.baiwa.buckwaframework.security.web.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.security.web.WebAttributes;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.URL;
//
//@Controller
//public class AuthenController {
//	
//	private static final Logger logger = LoggerFactory.getLogger(AuthenController.class);
//	
//	@RequestMapping(value = URL.LOGIN_WEB, method = RequestMethod.GET)
//	public ModelAndView login(
//		@RequestParam(value = "error", required = false) String error,
//		@RequestParam(value = "logout", required = false) String logout,
//		HttpServletRequest request) {
//		
//		logger.info("login");
//		
//		if (StringUtils.isNotBlank(error)) {
//			logger.warn("login error: " + error);
//		}
//		
//		ModelAndView mav = new ModelAndView();
//		if (error != null) {
//			mav.addObject("error", getErrorMessage(request, WebAttributes.AUTHENTICATION_EXCEPTION));
//		}
//		
//		if (logout != null) {
//			mav.addObject("msg", "You've been logged out successfully.");
//		}
//		
//		mav.setViewName("security/login");
//		
//		return mav;
//	}
//	
//	// customize the error message
//	private String getErrorMessage(HttpServletRequest request, String key) {
//		
//		Exception exception = (Exception) request.getSession().getAttribute(key);
//		
//		String error = "";
//		if (exception instanceof BadCredentialsException) {
//			error = "Invalid username or password!";
//		} else if (exception instanceof LockedException) {
//			error = exception.getMessage();
//		} else {
//			error = "Invalid username and password!";
//		}
//		
//		return error;
//	}
//	
//}
