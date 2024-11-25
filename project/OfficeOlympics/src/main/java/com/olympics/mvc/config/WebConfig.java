package com.olympics.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.olympics.mvc.interceptor.LoginInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
	private LoginInterceptor loginInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
		.excludePathPatterns("/auth/**", "/", "/error", "/static/**");
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173") // 정확한 클라이언트 도메인 설정
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 포함
            .allowCredentials(true) // 쿠키 인증 허용
            .allowedHeaders("*") // 모든 헤더 허용
            .maxAge(3600); // preflight 요청 캐싱 시간
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/profile/**")
        		.addResourceLocations("file:src/main/resources/static/uploads/profile/");
	}
	
}
