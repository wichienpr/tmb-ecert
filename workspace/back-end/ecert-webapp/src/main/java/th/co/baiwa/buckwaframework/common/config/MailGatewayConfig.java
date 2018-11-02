package th.co.baiwa.buckwaframework.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.tmb.ecert.batchjob.constant.BatchJobConstant.PARAMETER_CONFIG;
import com.tmb.ecert.common.constant.ProjectConstant;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Configuration
@ConditionalOnProperty(name = "mail.gateway.host")
public class MailGatewayConfig {

	@Value("${mail.gateway.host}")
	private String host;
	
	@Value("${mail.gateway.port}")
	private int pot;
	
	@Value("${mail.gateway.username}")
	private String username;
	
	@Value("${mail.gateway.password}")
	private String password;
	
	
	@Bean(name="mailGateWaySender")
	public JavaMailSenderImpl mailGateWaySender(){
		JavaMailSenderImpl m = new JavaMailSenderImpl();
		
//		m.setHost(appHost);
//		m.setPort(Integer.parseInt(appport));
		m.setUsername(username);
		m.setPassword(password);
		m.setProtocol("smtp");
		Properties prop = m.getJavaMailProperties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.quitwait", false);
		m.setDefaultEncoding("UTF-8");
		return m;
	}
	
	

}
