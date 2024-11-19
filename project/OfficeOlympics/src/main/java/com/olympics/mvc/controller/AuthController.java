package com.olympics.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.model.service.PlayerService;
import com.olympics.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@Tag(name="User Auth Restful API", description = "계정관련 CRUD")
@CrossOrigin("*")
public class AuthController {
	
	private final UserService userService;
	private final PlayerService playerService;
		
	public AuthController(UserService userService, PlayerService playerService) {
		super();
		this.userService = userService;
		this.playerService = playerService;
	}

	
	
	// 로그인 폼 반환
	@GetMapping("/login")
	@Operation(summary = "로그인 폼 반환", description = "로그인 페이지로 이동합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 페이지 반환")
	public ResponseEntity<String> loginform(){
		return new ResponseEntity<>("로그인 페이지입니다.", HttpStatus.OK);
	}
	
	
	
	// 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인 로직 수행", description = "사용자 id와 pw확인 후, 일치하는 사용자가 있으면 로그인 정보를 세션에 담아 반환합니다.")
	@ApiResponses({
		 @ApiResponse(responseCode = "200", description = "로그인 성공"),
		 @ApiResponse(responseCode = "400", description = "로그인 실패: 이메일 또는 비밀번호 확인 필요")
	})
	@Parameters({
		 @Parameter(name = "userId", description = "로그인하려는 사용자의 이메일", required = true),
		 @Parameter(name = "password", description = "로그인하려는 사용자의 비밀번호", required = true),
	})
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session, HttpServletRequest request) {
		User loginUser = userService.selectUser(user.getEmail());
		
		if (loginUser == null || !userService.checkPassword(loginUser, user.getPassword())) {
			return ResponseEntity.badRequest().body("로그인 실패: 이메일 또는 비밀번호를 확인하세요");
		}
		
		session.invalidate(); // 기존 세션 무효화
		session = request.getSession(true); // 새로운 세션 생성 (세션 고정 공격 방지)
		session.setAttribute("loginUserId", loginUser.getUserId()); // 사용자 ID
		session.setMaxInactiveInterval(60 * 60); // 세션 유효기간 : 1시간
		
		Map<String, Object> data  = new HashMap<>();
		data.put("loginUserId", session.getAttribute("loginUserId"));

		int olympicsId = playerService.findOlympicsIdByUserId(loginUser.getUserId());
		
		if (olympicsId > 0) {
			data.put("olympicsId", olympicsId);
		}
		
		return ResponseEntity.ok(data);

	}
	

	// 로그아웃 처리
	@PostMapping("/logout")
	@Operation(summary = "로그아웃 로직 수행", description = "로그아웃 클릭 시 세션을 무효화시킵니다.")
	@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate(); // 세션 무효화하여 로그아웃 처리
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}
	
	
	// 회원가입 폼 반환
	@GetMapping("/regist")
	@Operation(summary = "회원가입 폼 반환", description = "회원가입 페이지로 이동합니다.")
	@ApiResponse(responseCode = "200", description = "회원가입 페이지 반환")
	public ResponseEntity<String> registform(){
		return ResponseEntity.ok("회원가입 페이지입니다.");
	}
	
	// 회원가입
	//required=false 속성을 추가하여 profile_img 파라미터가 선택 사항임을 지정
	@PostMapping("/regist")
	@Operation(summary = "회원가입 로직 수행", description = "회원가입을 수행합니다.")
	@ApiResponses({
		 @ApiResponse(responseCode = "200", description = "회원가입 성공"),
		 @ApiResponse(responseCode = "400", description = "회원가입 실패: 이미 존재하는 사용자 또는 잘못된 입력")
	})
	@Parameters({
		 @Parameter(name = "email", description = "회원가입할 사용자의 이메일", required = true),
		 @Parameter(name = "password", description = "회원가입할 사용자의 비밀번호", required = true),
		 @Parameter(name = "name", description = "회원가입할 사용자의 이름", required = true),
		 @Parameter(name = "nickname", description = "회원가입할 사용자의 닉네임", required = true),
		 @Parameter(name = "profileImg", description = "프로필 이미지 파일 (선택사항)", required = false)
	})
	public ResponseEntity<String> regist(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "profileImg", required = false) MultipartFile profileImg) {
		
		User user = new User();
		user.setName(name);
		user.setNickname(nickname);
		user.setEmail(email);
		user.setPassword(password);
		
		// 회원가입 로직 호출
		boolean isRegistered = userService.insertUser(user, profileImg);
		
		if (isRegistered) {
			return ResponseEntity.ok(name + "님 회원가입 성공");
		} else {
			return ResponseEntity.badRequest().body("회원가입 실패 : 이미 존재하는 사용자입니다.");
		}
	}
	
}


