package com.olympics.mvc.model.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.User;

public interface UserService {
	
	// 회원 가입
	boolean insertUser(User user, MultipartFile profileImg);
	
	// 이메일로 회원 정보 조회
	User selectUser(String email);
	
	// id index로 회원 정보 조회
	User selectById(int id);
	
	// 비밀번호 확인
	boolean checkPassword(User user, String password);
	
	// 회원 삭제
	boolean deleteUser(int id);
		
	// 회원 정보 수정
	boolean modifyUser(User user, MultipartFile profileImg);
	
	// 전체 회원 정보 조회
	List<User> selectAccounts();
}
