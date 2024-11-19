package com.olympics.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;
import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.model.service.PlayerService;
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
@CrossOrigin("*")
public class AccountController {
	
	private final UserService userService;
	private final PlayerService playerService;
	
	public AccountController(UserService userService, PlayerService playerService) {
		this.userService = userService;
		this.playerService=playerService;
	}
	
	// 마이페이지 조회
	@GetMapping("/{userId}")
	@Operation(summary = "마이페이지 조회", description = "내 정보를 확인할 수 있습니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
	    @ApiResponse(responseCode = "204", description = "사용자 정보 없음")
	})
	@Parameter(name = "userId", description = "조회할 사용자의 ID", required = true)
	public ResponseEntity<?> mypage(@PathVariable("userId") int userId) {
	    // 주어진 ID로 사용자 정보, 올림픽 팀을 조회
	    User user = userService.selectById(userId);
	    if(user == null) {
	    	return ResponseEntity.noContent().build();
	    }

	    // 사용자 닉네임 HTML 이스케이프 처리
	    user.setNickname(HtmlUtils.htmlEscape(user.getNickname()));
	    
	    Map<String, Object> userData = new HashMap<>();
	    userData.put("userId", userId);
	    userData.put("email", user.getEmail());
	    userData.put("name", user.getName());
	    userData.put("nickname", user.getNickname());
	    userData.put("profileImg", user.getProfileImg());

	    int olympicsId = playerService.findOlympicsIdByUserId(userId);
	    List<Map<String, Object>> players = (olympicsId > 0) ? playerService.getPlayersByOlympicsId(olympicsId) : new ArrayList<>();
	    
	    Map<String, Object> myPageData = new HashMap<>();
	    myPageData.put("user", userData);
	    myPageData.put("players", players);
	    
	    return ResponseEntity.ok(myPageData);
	}

    // 정보 수정
    @PutMapping("/{userId}")
    @Operation(summary = "사용자 정보 수정", description = "내 정보를 수정할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 접근"),
        @ApiResponse(responseCode = "400", description = "회원 정보 수정 실패")
    })
    @Parameters({
        @Parameter(name = "userId", description = "수정할 사용자의 ID", required = true),
        @Parameter(name = "nickname", description = "수정할 닉네임", required = true),
        @Parameter(name = "profileImg", description = "업로드할 프로필 이미지 파일", required = false)
    })
    public ResponseEntity<String> modifyUser(@PathVariable("userId") int userId, 
    		@RequestParam("nickname") String nickname,
    		@RequestParam(value = "profileImg", required = false) MultipartFile profileImg, 
    		@RequestParam("olympicsName") String olympicsName,
    		@RequestParam("playerNames") List<String> playerNames,
            HttpSession session) {
    	
        if (!isValidSessionUser(session, userId)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 접근입니다.");
        }
        
        User user = userService.selectById(userId);
        user.setNickname(nickname);
        user.setUserId(userId);
        
        if (profileImg != null && profileImg.isEmpty()) {
            profileImg = null;
        }
 
        boolean isUserModified = userService.modifyUser(user, profileImg);
        
        OlympicsSetup olympic = new OlympicsSetup(userId, olympicsName, playerNames);
        olympic.setOlympicsId(playerService.findOlympicsIdByUserId(userId));
        
        boolean isOlympicModified = playerService.modifyOlympics(olympic);
        
        if (isUserModified && isOlympicModified) {
        	return ResponseEntity.ok(user.getName() + "님의 정보를 수정했습니다.");     	
        }
        
        // 실패 시 처리
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 수정 실패");
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
    	if (!isValidSessionUser(session, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자만 탈퇴할 수 있습니다.");
        }

        // 탈퇴 처리
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
        	// 회원 탈퇴 성공 시 세션 무효화
        	session.invalidate();
        	return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");        	
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
    
	
    // 세션 검증 로직
    public boolean isValidSessionUser(HttpSession session, int userId) {
    	Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
    	return sessionUserId != null && sessionUserId.equals(userId);
    }
    
    
    
    
    // 전체 회원 조회
// 	@GetMapping("/admin")
// 	@Operation(summary = "전체 회원 조회", description = "추가 기능으로 관리자 페이지에서 전체 회원 조회를 실행합니다.")
// 	@ApiResponses({
// 	    @ApiResponse(responseCode = "200", description = "전체 사용자 목록 반환")
// 	})
//	public ResponseEntity<List<User>> showAccounts(){
//		List<User> users = userService.selectAccounts();
//		return ResponseEntity.ok(users);
//	}
	
}
