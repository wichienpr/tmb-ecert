//package th.co.baiwa.buckwaframework.common.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.security.web.savedrequest.NullRequestCache;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import th.co.baiwa.buckwaframework.security.constant.SecurityConstants;
//import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.ROLE;
//import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.URL;
//import th.co.baiwa.buckwaframework.security.rest.entrypoint.RestAuthenticationEntryPoint;
//import th.co.baiwa.buckwaframework.security.rest.handler.RestAuthenticationSuccessHandler;
//import th.co.baiwa.buckwaframework.security.rest.handler.RestLogoutSuccessHandler;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//	
//	@Autowired
//	@Qualifier("jdbcAuthenticationProvider")
//	private AuthenticationProvider jdbcAuthenticationProvider;
//	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(jdbcAuthenticationProvider);
//	}
//	
//	@Bean(name = "passwordEncoder")
//	public PasswordEncoder passwordEncoder() {
//		PasswordEncoder encoder = new BCryptPasswordEncoder();
//		return encoder;
//	}
//	
//	@Bean
//	public HttpSessionEventPublisher httpSessionEventPublisher() {
//	    return new HttpSessionEventPublisher();
//	}
//	
//	/*
//	 * Rest Login API
//	 */
//	@Configuration
//	@Order(1)
//	public class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//		
//		@Override
//		public void configure(WebSecurity web) throws Exception {
//			web.ignoring().antMatchers(HttpMethod.GET,
//				"/api/preferences/**"
//			);
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//				.antMatcher("/api/**")
//					.authorizeRequests().anyRequest()
//					//.hasAnyRole(ROLE.USER)
//					.hasAnyAuthority("ABC")
//				.and()
//				.formLogin()
//					.loginProcessingUrl(URL.LOGIN_REST).permitAll()
//					.successHandler(restAuthenticationSuccessHandler())
//					.failureHandler(restAuthenticationFailureHandler())
//					.usernameParameter(SecurityConstants.USERNAME_PARAM)
//					.passwordParameter(SecurityConstants.PASSWORD_PARAM)
//				.and()
//				.logout()
//					.permitAll()
//					.logoutRequestMatcher(new AntPathRequestMatcher(URL.LOGIN_REST, HttpMethod.DELETE.toString()))
//					.logoutSuccessHandler(restLogoutSuccessHandler())
//				.and()
//				.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
//				.and()
//				.requestCache().requestCache(new NullRequestCache())
//				.and()
//				.sessionManagement()
//					.maximumSessions(2)
//					.sessionRegistry(sessionRegistry());
//			
//			http.csrf().disable();
//		}
//		
//		@Bean
//		public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
//			return new RestAuthenticationSuccessHandler();
//		}
//		
//		@Bean
//		public SimpleUrlAuthenticationFailureHandler restAuthenticationFailureHandler() {
//			return new SimpleUrlAuthenticationFailureHandler();
//		}
//		
//		@Bean
//		public RestLogoutSuccessHandler restLogoutSuccessHandler() {
//			return new RestLogoutSuccessHandler();
//		}
//		
//		@Bean
//		public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
//			return new RestAuthenticationEntryPoint();
//		}
//		
//		@Bean
//		public SessionRegistry sessionRegistry() {
//			return new SessionRegistryImpl();
//		}
//		
//	}
//	
//	/*
//	 * From Login API
//	 */
//	@Configuration
//	@Order(2)
//	public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//		
//		@Override
//		public void configure(WebSecurity web) throws Exception {
//			web.ignoring().antMatchers(
//				// For Angular -- Start
//				"/inline.bundle.js",
//				"/polyfills.bundle.js",
//				"/scripts.bundle.js",
//				"/styles.bundle.js",
//				"/vendor.bundle.js",
//				"/main.bundle.js",
//				// For Angular -- End
//				// For Swagger2 -- Start
//				"/v2/api-docs/**",
//				"/swagger.json",
//				"/swagger-ui.html"
//				// For Swagger2 -- End
//			);
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//				.antMatcher("/backend/**")
//					.authorizeRequests().anyRequest()
//					.hasAnyRole(ROLE.USER, ROLE.ADMIN)
//				.and()
//				.formLogin()
//					.loginPage(URL.LOGIN_WEB).permitAll()
//					.defaultSuccessUrl(URL.LOGIN_WEB_SUCCESS)
//					.failureUrl(URL.LOGIN_WEB_FAILURE)
//					.usernameParameter(SecurityConstants.USERNAME_PARAM)
//					.passwordParameter(SecurityConstants.PASSWORD_PARAM)
//				.and()
//				.logout()
//					.permitAll()
//					.logoutRequestMatcher(new AntPathRequestMatcher("/logout.htm"))
//				.and()
//				.exceptionHandling().accessDeniedPage("/403.htm")
//				.and()
//				.sessionManagement()
//					.invalidSessionUrl(SecurityConstants.URL.LOGIN_WEB)
//					.maximumSessions(2)
//					.sessionRegistry(sessionRegistry());
//			
//			http.csrf().disable();
//		}
//		
//		@Bean
//		public SessionRegistry sessionRegistry() {
//			return new SessionRegistryImpl();
//		}
//		
//	}
//	
//}