package com.olympics.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	// 로그인 폼 반환
	@GetMapping("/login")
	public ResponseEntity<String> loginform(){
		return null;
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<String> login(){
		return null;
	}
	
	// 로그아웃
	@PostMapping("logout")
	public ResponseEntity<String> logout(){
		return null;
	}
	
	// 회원가입 폼 반환
	public ResponseEntity<String> registform(){
		return null;
	}
	
	// 회원가입
	public ResponseEntity<String> regist(){
		return null;
	}
	
}
