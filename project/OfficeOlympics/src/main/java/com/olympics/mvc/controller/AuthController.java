package com.olympics.mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.Service.UserService;
import com.olympics.mvc.model.dto.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UserService userService;
		
	public AuthController(UserService userService) {
		super();
		this.userService = userService;
	}

	// 로그인 폼 반환
	@GetMapping("/login")
	public ResponseEntity<String> loginform(){
		return new ResponseEntity<>("로그인 페이지입니다.", HttpStatus.OK);
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session, HttpServletRequest request){
		try {
			
			User logincheck = userService.selectUser(user); 
			
			// 조회 결과가 있을 경우 세션에 사용자 정보 저장 및 성공 메시지 반환
			if (logincheck != null && logincheck.getEmail() != null) {
				session.invalidate(); // 기존 세션 무효화 
				session = request.getSession(true); // 새로운 세션 생성 (세션 고정 공격 방지)
				session.setAttribute("loginUser", logincheck.getName());
				session.setMaxInactiveInterval(60*60); // 세션 유효기간 : 1시간
				return new ResponseEntity<String>("로그인 성공", HttpStatus.OK);
			} else {
				// 조회 실패 시 실패 메시지 반환
				return new ResponseEntity<>("로그인 실패: 이메일 또는 비밀번호를 확인하세요", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// 서버에서 예외 발생 시 상세 정보를 로그로 남기고, 사용자에게는 일반 오류 메시지 전달
			e.printStackTrace();
			return new ResponseEntity<String>("요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// 로그아웃 처리
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate(); // 세션 무효화하여 로그아웃 처리
		return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
	}
	
	// 회원가입 폼 반환
	@GetMapping("/regist")
	public ResponseEntity<String> registform(){
		return new ResponseEntity<>("회원가입 페이지입니다.", HttpStatus.OK);
	}
	
	// 회원가입
	@PostMapping("/regist")
	public ResponseEntity<String> regist(@RequestBody User user){
		try {
			// UserService를 사용해 새 사용자 정보 삽입 시도
			boolean newUser = userService.insertUser(user);
			if (newUser) {
				// 회원가입 성공 시 사용자 이름을 포함한 메시지 반환
				String name = user.getName();
				return new ResponseEntity<String>(name + "님 회원가입 성공", HttpStatus.OK);
			} else {
				// 이미 존재하는 사용자일 경우 실패 메시지 반환
				return new ResponseEntity<>("회원가입 실패 : 이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// 서버에서 예외 발생 시 상세 정보를 로그로 남기고, 사용자에게는 일반 오류 메시지 전달
			e.printStackTrace();
			return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);
		}
	}
	
}
