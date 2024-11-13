package com.olympics.mvc.model.dao;

import java.util.List;

import com.olympics.mvc.model.dto.User;

public interface UserDao {
	
	// 회원 가입
	int insertUser(User user);
	
	// 회원 삭제
	int deleteUser(int userId);
	
	// 이메일로 회원 정보 조회 (로그인 시 사용)
	User selectUser(String email);
	
	// id index로 회원 조회
	User selectById(int userId);
	
	// 회원 정보 수정
	int modifyUser(User user);
	
	// 전체 회원 정보 조회
	List<User> selectAccounts();
	
}
