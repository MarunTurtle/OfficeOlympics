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
	    System.out.println("Session ID: " + session.getId());
	    System.out.println("loginUserId: " + session.getAttribute("loginUserId"));
	    
	    Integer isLogin = (Integer) session.getAttribute("loginUserId");
	    if (isLogin == null) {
	        System.out.println("No loginUserId found");
	        return false;
	    }

	    System.out.println("User logged in with ID: " + isLogin);
	    return true;
	}
	
}
