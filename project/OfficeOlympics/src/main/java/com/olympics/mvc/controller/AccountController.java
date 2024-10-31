package com.olympics.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	// 마이페이지 조회
	@GetMapping("/{id}")
	public ResponseEntity<String> mypage(@PathVariable("id") int id){
		return null;
	}
	
	// 정보 수정
	@PutMapping("/{id}")
	public ResponseEntity<String> modifyUser(@PathVariable("id") int id){
		return null;
	}
	
	// 회원 탈퇴
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") int id){
		return null;
	}
	
//	// 전체 회원 조회
//	@GetMapping(".")
//	public ResponseEntity<String> showAccounts(){
//		return null;
//	}
	
}