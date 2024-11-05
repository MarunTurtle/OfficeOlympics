package com.olympics.mvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/accounts")
@Tag(name="User Accounts Restful API", description = "계정관련 CRUD")
public class AccountController {
	
	private final UserService userService;
	
	public AccountController(UserService userService) {
		this.userService = userService;
	}

	
	
	// 마이페이지 조회
	@GetMapping("/{userId}")
	@Operation(summary = "마이페이지 조회", description = "내 정보를 확인할 수 있습니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
	    @ApiResponse(responseCode = "204", description = "사용자 정보 없음")
	})
	@Parameter(name = "userId", description = "조회할 사용자의 ID", required = true)
	public ResponseEntity<User> mypage(@PathVariable("userId") int userId) {
	    // 주어진 ID로 사용자 정보를 조회
	    User user = userService.selectById(userId);
	    System.out.println(user);

	    // 사용자가 존재할 경우 사용자 정보를 반환
	    if (user != null)
	        return new ResponseEntity<>(user, HttpStatus.OK);

	    // 사용자가 존재하지 않을 경우 NO_CONTENT 상태 반환
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	
    // 정보 수정
    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "내 정보를 수정할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 접근"),
        @ApiResponse(responseCode = "400", description = "회원 정보 수정 실패")
    })
    @Parameters({
        @Parameter(name = "userId", description = "수정할 사용자의 ID", required = true),
        @Parameter(name = "email", description = "수정할 이메일 주소", required = true),
        @Parameter(name = "name", description = "수정할 이름", required = true),
        @Parameter(name = "profileImg", description = "업로드할 프로필 이미지 파일", required = false)
    })
    public ResponseEntity<String> modifyUser(@PathVariable("userId") int userId, @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam(value = "profileImg", required = false) MultipartFile profileImg, 
            HttpSession session) {
    	
    	// 세션에서 로그인한 사용자 ID 확인
        Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
        if (sessionUserId == null || sessionUserId != userId) {
            return new ResponseEntity<>("잘못된 접근입니다.", HttpStatus.UNAUTHORIZED);
        }
        
        // 수정할 User 객체 생성 및 초기화
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setName(name);
        
        // 수정 처리
    	user.setUserId(userId);
        boolean isModify = userService.modifyUser(user, profileImg);
        if (isModify) 
            return new ResponseEntity<>(user.getName() + "님의 정보를 수정했습니다.", HttpStatus.OK);
        return new ResponseEntity<>("회원 정보 수정 실패", HttpStatus.BAD_REQUEST);
    }
	
    
    
    // 회원 탈퇴 (본인 확인)
    @DeleteMapping("/{userId}")
    @Operation(summary = "회원 탈퇴 로직 수행", description = "세션 인증 후 회원 탈퇴하는 기능입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "401", description = "인증된 사용자만 탈퇴할 수 있음"),
        @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패")
    })
    @Parameter(name = "userId", description = "탈퇴할 사용자의 ID", required = true)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId, HttpSession session) {
    	
        // 세션에서 로그인한 사용자 ID 확인
        Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
        System.out.println(sessionUserId);
        if (sessionUserId == null || sessionUserId != userId) {
            return new ResponseEntity<>("인증된 사용자만 탈퇴할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 탈퇴 처리
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
        	// 회원 탈퇴 성공 시 세션 무효화
        	session.invalidate();
        	return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);        	
        }
        
        return new ResponseEntity<>("회원 탈퇴 실패", HttpStatus.BAD_REQUEST);
    }
	
    
    
    // 전체 회원 조회
 	@GetMapping("/admin")
 	@Operation(summary = "전체 회원 조회", description = "추가 기능으로 관리자 페이지에서 전체 회원 조회를 실행합니다.")
 	@ApiResponses({
 	    @ApiResponse(responseCode = "200", description = "전체 사용자 목록 반환")
 	})
	public ResponseEntity<List<User>> showAccounts(){
		List<User> users = userService.selectAccounts();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
}
