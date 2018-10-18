//package th.co.baiwa.buckwaframework.security.web.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//public class CommonController {
//	
//	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
//	
//	/**
//	 * Home page
//	 */
//	@RequestMapping(value = {"/", "index.htm"})
//	public String root() {
//		return "redirect:/index.html";
//	}
//	
//	@RequestMapping(value = "/anonymous.htm")
//	public ModelAndView anonymous() {
//		logger.info(" # anonymous 0 ");
//		
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("anonymous");
//		
//		logger.info(" # anonymous success ");
//		
//		return mav;
//	}
//	
//	@RequestMapping(value = "/welcome.html", method = RequestMethod.GET)
//	public ModelAndView welcome(HttpServletRequest httpRequest) {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("welcome");
//		
//		return mav;
//	}
//	
//	// for 403 access denied page
//	@RequestMapping(value = "/403.html", method = RequestMethod.GET)
//	public ModelAndView accesssDenied() {
//		
//		ModelAndView mav = new ModelAndView();
//		
//		// check if user is login
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//			UserDetails userDetail = (UserDetails) auth.getPrincipal();
//			mav.addObject("username", userDetail.getUsername());
//		}
//		
//		mav.setViewName("403");
//		
//		return mav;
//	}
//	
//	@RequestMapping(value = "/404.htm")
//	public ModelAndView erorr404() {
//		logger.info(" ### in error404");
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("404");
//		return mav;
//	}
//	
//	@RequestMapping(value = "/500.htm")
//	public ModelAndView erorr500() {
//		logger.info(" ### in error500");
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("500");
//		return mav;
//	}
//
//}
