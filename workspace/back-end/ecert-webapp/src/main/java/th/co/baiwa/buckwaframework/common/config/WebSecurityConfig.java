package th.co.baiwa.buckwaframework.common.config;

import java.io.IOException;

import javax.management.relation.RoleInfoNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import th.co.baiwa.buckwaframework.security.constant.SecurityConstants.LOGIN_STATUS;
import th.co.baiwa.buckwaframework.security.provider.TmbAuthenticationProvider;
import th.co.baiwa.buckwaframework.security.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${server.https.port}")
	private String httpsport;
	
	@Value("${server.http.port}")
	private String httpport;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.inMemoryAuthentication().withUser("admin").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("admin2").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("admin3").password("password").roles("USER");
		
		auth.inMemoryAuthentication().withUser("requester").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("requester_qa1").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("requester_qa2").password("password").roles("USER");

		auth.inMemoryAuthentication().withUser("maker").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("maker_qa1").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("maker_qa2").password("password").roles("USER");

		auth.inMemoryAuthentication().withUser("checker").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("checker_qa1").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("checker_qa2").password("password").roles("USER");

		auth.inMemoryAuthentication().withUser("isa").password("password").roles("USER");

		auth.inMemoryAuthentication().withUser("it").password("password").roles("USER");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/onlogout","/app/**").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
				.successForwardUrl("/onloginseccess")
				.failureHandler(customfailHandler()).and().logout().permitAll().and().csrf().disable();
		
		http.sessionManagement().maximumSessions(2).sessionRegistry(sessionRegistry()); 
//		redirect http to https 
		http.requiresChannel().antMatchers("/**","/app/**","/*.html").requiresSecure()
		.and().portMapper().http(Integer.valueOf(httpport)).mapsTo(Integer.valueOf(httpsport));
		
		http.headers().defaultsDisabled()
        .addHeaderWriter(new StaticHeadersWriter("Cache-Control"," no-cache,max-age=0, must-revalidate"))
        .addHeaderWriter(new StaticHeadersWriter("Expires","0")); 
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(tmbAuthenticationProvider());
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler customfailHandler () {
		return new SimpleUrlAuthenticationFailureHandler (){

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				
				System.out.println("customfailHandler");
				if(exception instanceof BadCredentialsException ) {
					request.getRequestDispatcher("/onloginerror?error=" + LOGIN_STATUS.FAIL).forward(request, response);
				}else if (exception  instanceof InternalAuthenticationServiceException) {
					request.getRequestDispatcher("/onloginerror?error=" + LOGIN_STATUS.MULTI_ROLE).forward(request, response);
				}
				else if(exception instanceof AuthenticationCredentialsNotFoundException ) {
					request.getRequestDispatcher("/onloginerror?error=" + LOGIN_STATUS.NOT_ALLOW).forward(request, response);
				}

			}
		};
	}
	
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
	@Bean("tmbAuthenticationProvider")
	public TmbAuthenticationProvider tmbAuthenticationProvider() {
		TmbAuthenticationProvider p = new TmbAuthenticationProvider();
		p.setUserDetailsService(userDetailsService);
		return p;
	}

}