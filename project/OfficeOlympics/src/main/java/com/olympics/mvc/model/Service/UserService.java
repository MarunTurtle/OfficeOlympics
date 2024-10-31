package com.olympics.mvc.model.Service;

import java.util.List;

import com.olympics.mvc.model.dto.User;

public interface UserService {
	
	// 회원 가입
	boolean insertUser(User user);
	
	// 이메일로 회원 정보 조회
	User selectUser(User user);
	
	// 회원 삭제
	boolean deleteUser(int id);
		
	// 회원 정보 수정
	boolean modifyUser(User user);
	
	// 전체 회원 정보 조회
	List<User> selectAccounts();
}
