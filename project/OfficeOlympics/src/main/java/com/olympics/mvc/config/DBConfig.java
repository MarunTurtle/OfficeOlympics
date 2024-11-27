package com.olympics.mvc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.olympics.mvc.model.dao")
public class DBConfig {
	
}
