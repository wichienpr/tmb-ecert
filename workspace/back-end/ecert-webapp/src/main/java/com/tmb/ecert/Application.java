package com.tmb.ecert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(
	scanBasePackages = {
		"th.co.baiwa.buckwaframework",
		"com.tmb.ecert"
	},
	exclude = {
		HibernateJpaAutoConfiguration.class,
		QuartzAutoConfiguration.class
//	,ThymeleafAutoConfiguration.class
	}
)

public class Application extends SpringBootServletInitializer {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
}