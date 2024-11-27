package com.olympics.mvc.util;

import jakarta.servlet.http.HttpSession;

public class Validate {
	/**
     * 세션 검증 로직
     * 
     * @param session 현재 사용자 세션
     * @param userId  검증할 사용자 ID
     * @return 현재 userId와 세션 내 userId 일치 여부
     */
    public static boolean isValidSessionUser(HttpSession session, int userId) {
    	Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
    	return sessionUserId != null && sessionUserId.equals(userId);
    }
}
