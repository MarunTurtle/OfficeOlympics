package com.olympics.mvc.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Configuration
public class SessionConfig {
	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
		return new ServletContextInitializer() {
			
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.getSessionCookieConfig().setHttpOnly(true); // HTTP 전용 설정
				servletContext.getSessionCookieConfig().setSecure(false); // HTTPS 환경이 아니므로 false
			}
		};
	}
}
