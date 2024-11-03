package com.olympics.mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@Tag(name="User Auth Restful API", description = "계정관련 CRUD")
public class AuthController {
	
	private final UserService userService;
		
	public AuthController(UserService userService) {
		super();
		this.userService = userService;
	}

	// 로그인 폼 반환
	@GetMapping("/login")
	@Operation(summary = "로그인 폼 반환", description = "로그인 페이지로 이동합니다.")
	public ResponseEntity<String> loginform(){
		return new ResponseEntity<>("로그인 페이지입니다.", HttpStatus.OK);
	}
	
	// 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인 로직 수행", description = "사용자 id와 pw확인 후, 일치하는 사용자가 있으면 로그인 정보를 세션에 담아 반환합니다.")
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session, HttpServletRequest request) {
	    try {
	        User loginUser = userService.selectUser(user.getEmail());
	        System.out.println(loginUser);

	        // 사용자가 존재하고 비밀번호가 일치할 경우
	        if (loginUser != null && userService.checkPassword(loginUser, user.getPassword())) {
	            session.invalidate(); // 기존 세션 무효화
	            session = request.getSession(true); // 새로운 세션 생성 (세션 고정 공격 방지)
	            
	            // 사용자 정보 세션에 저장
	            session.setAttribute("loginUserId", loginUser.getUser_id()); // 사용자 ID
	            session.setAttribute("loginUserName", loginUser.getName()); // 사용자 이름
	            session.setMaxInactiveInterval(60 * 60); // 세션 유효기간 : 1시간
	            System.out.println(session.getAttribute("loginUserId"));
	            
	            return new ResponseEntity<>(loginUser.getName()+"님 로그인 성공", HttpStatus.OK);
	        } else {
	            // 조회 실패 시 실패 메시지 반환
	            return new ResponseEntity<>("로그인 실패: 이메일 또는 비밀번호를 확인하세요", HttpStatus.BAD_REQUEST);
	        }
	    } catch (Exception e) {
	        // 서버에서 예외 발생 시 상세 정보를 로그로 남기고, 사용자에게는 일반 오류 메시지 전달
	        e.printStackTrace();
	        return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	// 로그아웃 처리
	@PostMapping("/logout")
	@Operation(summary = "로그아웃 로직 수행", description = "로그아웃 클릭 시 세션을 무효화시킵니다.")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate(); // 세션 무효화하여 로그아웃 처리
		return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
	}
	
	// 회원가입 폼 반환
	@GetMapping("/regist")
	@Operation(summary = "회원가입 폼 반환", description = "회원가입 페이지로 이동합니다.")
	public ResponseEntity<String> registform(){
		return new ResponseEntity<>("회원가입 페이지입니다.", HttpStatus.OK);
	}
	
	// 회원가입
	//required=false 속성을 추가하여 profile_img 파라미터가 선택 사항임을 지정
	@PostMapping("/regist")
	@Operation(summary = "회원가입 로직 수행", description = "회원가입을 수행합니다.")
	public ResponseEntity<String> regist(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam(value = "profile_img", required = false) MultipartFile profileImg) {
		try {
		// 사용자 객체 생성 및 정보 설정
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		
		// 회원가입 로직 호출
		boolean newUser = userService.insertUser(user, profileImg);
		System.out.println(newUser);
		
		if (newUser) {
			return new ResponseEntity<>(name + "님 회원가입 성공", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("회원가입 실패 : 이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST);
		}
		
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
	
}


