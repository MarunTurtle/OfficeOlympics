package com.olympics.mvc.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.olympics.mvc.model.service.PlayerService;
import com.olympics.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@Tag(name="User Auth Restful API", description = "계정관련 CRUD")
public class AuthController {
	
	private final UserService userService;
	private final PlayerService playerService;
		
	public AuthController(UserService userService, PlayerService playerService) {
		super();
		this.userService = userService;
		this.playerService = playerService;
	}

	
	/**
	 * 로그인 폼 반환
	 * 
	 * @return 로그인 페이지 문자열
	 */
	@GetMapping("/login")
	@Operation(summary = "로그인 폼 반환", description = "로그인 페이지로 이동합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 페이지 반환")
	public ResponseEntity<String> loginform(){
		return new ResponseEntity<>("로그인 페이지입니다.", HttpStatus.OK);
	}
	
	
	/**
	 * 로그인 로직 수행
	 * 
	 * @param user 	  로그인 요청 사용자 정보 (email, password 포함)
	 * @param session 현재 사용자 세션
	 * @param request HTTP 요청 객체
	 * @return 로그인 결과 데이터 (사용자 정보)
	 */
	@PostMapping("/login")
    @Operation(summary = "로그인 로직 수행", description = "사용자 id와 pw 확인 후 로그인 정보를 세션에 저장합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "로그인 실패: 이메일 또는 비밀번호 확인 필요")
    })
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session, HttpServletRequest request) {
		
		User loginUser = userService.selectUser(user.getEmail());
		
		if (loginUser == null || !userService.checkPassword(loginUser, user.getPassword())) {
			return ResponseEntity.badRequest().body("로그인 실패: 이메일 또는 비밀번호를 확인하세요");
		}
		
		session.invalidate();
		session = request.getSession(true);
		session.setAttribute("loginUserId", loginUser.getUserId());
		session.setMaxInactiveInterval(60 * 60);
		
		Map<String, Object> data  = new HashMap<>();
		data.put("loginUserId", session.getAttribute("loginUserId"));
		data.put("nickname", loginUser.getNickname());

		int olympicsId = playerService.findOlympicsIdByUserId(loginUser.getUserId());
		
		if (olympicsId > 0) {
			data.put("olympicsId", olympicsId);
		}
		
		return ResponseEntity.ok(data);
	}
	

	/**
	 * 로그아웃 로직 수행
	 * 
	 * @param session 현재 사용자 세션
	 * @return 로그아웃 결과 메시지
	 */
	@PostMapping("/logout")
	@Operation(summary = "로그아웃 로직 수행", description = "로그아웃 클릭 시 세션을 무효화시킵니다.")
	@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate();
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}
	
	
	/**
	 * 회원가입 폼 반환
	 * 
	 * @return 회원가입 페이지 문자열
	 */
	@GetMapping("/register")
	@Operation(summary = "회원가입 폼 반환", description = "회원가입 페이지로 이동합니다.")
	@ApiResponse(responseCode = "200", description = "회원가입 페이지 반환")
	public ResponseEntity<String> registform(){
		return ResponseEntity.ok("회원가입 페이지입니다.");
	}
	
	
	/**
	 * 회원가입 로직 수행
	 * 
	 * @param email 	 회원가입할 사용자의 이메일
	 * @param password   회원가입할 사용자의 비밀번호
	 * @param name 		 회원가입할 사용자의 이름
	 * @param nickname   회원가입할 사용자의 닉네임
	 * @param profileImg 프로필 이미지 파일 (선택사항)
	 * @return 회원가입 결과 메시지
	 */
	@PostMapping("/register")
    @Operation(summary = "회원가입 로직 수행", description = "회원가입을 수행합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "회원가입 실패: 이미 존재하는 사용자 또는 잘못된 입력")
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
		
		boolean isRegistered = userService.insertUser(user, profileImg);
		
		if(isRegistered) {
			return ResponseEntity.ok(name + "님 회원가입 성공");
		}
		return ResponseEntity.badRequest().body("회원가입 실패 : 이미 존재하는 사용자입니다.");
	}
}