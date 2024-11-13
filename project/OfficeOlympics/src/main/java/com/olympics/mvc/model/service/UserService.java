package com.olympics.mvc.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.Player;
import com.olympics.mvc.model.dto.User;

public interface UserService {
	
	// 회원 가입
	boolean insertUser(User user, MultipartFile profileImg);
	
	// 회원 삭제
	boolean deleteUser(int id);

	// 이메일로 회원 정보 조회 (로그인 시 사용)
	User selectUser(String email);
	
	// 비밀번호 확인
	boolean checkPassword(User user, String password);
	
	// id index로 회원 정보 조회 (마이페이지 조회)
	User selectById(int id);
		
	// 회원 정보 수정
	boolean modifyUser(User user, MultipartFile profileImg);
	
	// 전체 회원 정보 조회
	List<User> selectAccounts();
}
