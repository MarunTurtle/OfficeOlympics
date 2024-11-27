package com.olympics.mvc.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
	    
	    Integer isLogin = (Integer) session.getAttribute("loginUserId");
	    if (isLogin == null) {
	        System.out.println("No loginUserId found");
	        return false;
	    }

	    return true;
	}
	
}
