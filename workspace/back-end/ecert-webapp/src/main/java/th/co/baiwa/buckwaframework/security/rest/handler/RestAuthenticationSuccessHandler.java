//package th.co.baiwa.buckwaframework.security.rest.handler;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//		clearAuthenticationAttributes(request);
//	}
//	
//}
