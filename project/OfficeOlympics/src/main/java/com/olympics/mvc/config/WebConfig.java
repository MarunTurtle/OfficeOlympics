package com.olympics.mvc.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer{
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOrigins("http://localhost:5173") // 정확한 클라이언트 도메인 설정
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 포함
//            .allowCredentials(true) // 쿠키 인증 허용
//            .allowedHeaders("*") // 모든 헤더 허용
//            .maxAge(3600); // preflight 요청 캐싱 시간
//    }
}
