package com.olympics.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.util.HtmlUtils;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.User;
import com.olympics.mvc.model.service.PlayerService;
import com.olympics.mvc.model.service.UserService;
import com.olympics.mvc.util.Validate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/accounts")
@Tag(name="User Accounts Restful API", description = "계정관련 CRUD")
public class AccountController {
	
	private final UserService userService;
	private final PlayerService playerService;
	
	public AccountController(UserService userService, PlayerService playerService) {
		this.userService = userService;
		this.playerService=playerService;
	}
	
	
	/**
	 * 마이페이지 조회
	 * 
	 * @param userId 조회할 사용자의 ID
	 * @return 사용자 정보와 올림픽 팀 정보
	 */
	@GetMapping("/{userId}")
    @Operation(summary = "마이페이지 조회", description = "내 정보를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
        @ApiResponse(responseCode = "204", description = "사용자 정보 없음")
    })
	public ResponseEntity<?> mypage(@PathVariable("userId") int userId, HttpSession session) {
		
		if (!Validate.isValidSessionUser(session, userId)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 접근입니다.");
        }
		
	    User user = userService.selectById(userId);
	    if(user == null) {
	    	return ResponseEntity.noContent().build();
	    }

	    user.setNickname(HtmlUtils.htmlEscape(user.getNickname()));
	    
	    Map<String, Object> userData = new HashMap<>();
	    userData.put("userId", userId);
	    userData.put("email", user.getEmail());
	    userData.put("name", user.getName());
	    userData.put("nickname", user.getNickname());
	    userData.put("profileImg", user.getProfileImg());
	    userData.put("ImgSrc", user.getImgSrc());

	    int olympicsId = playerService.findOlympicsIdByUserId(userId);
	    List<Map<String, Object>> players = (olympicsId > 0) ? playerService.getPlayersByOlympicsId(olympicsId) : new ArrayList<>();
	    
	    Map<String, Object> myPageData = new HashMap<>();
	    myPageData.put("userData", userData);
	    myPageData.put("players", players);
	    
	    return ResponseEntity.ok(myPageData);
	}

	
	/**
	 * 사용자 정보 수정
	 * 
	 * @param userId, nickname, profileImg 수정할 사용자의 ID, 닉네임, 프로필 이미지
	 * @param olympicsName 올림픽 이름
	 * @param playerNames  선수 목록
	 * @param session 	   현재 사용자 세션
	 * @return 수정 결과 메시지
	 */
	@PutMapping("/{userId}")
    @Operation(summary = "사용자 정보 수정", description = "내 정보를 수정할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 접근"),
        @ApiResponse(responseCode = "400", description = "회원 정보 수정 실패")
    })
    public ResponseEntity<String> modifyUser(@PathVariable("userId") int userId, 
    		@RequestParam("nickname") String nickname,
    		@RequestParam(value = "profileImg", required = false) MultipartFile profileImg, 
    		@RequestParam(value = "olympicsName", required = false) String olympicsName,
    		@RequestParam(value = "playerNames", required = false) List<String> playerNames,
            HttpSession session) {
		
        if (!Validate.isValidSessionUser(session, userId)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인의 정보만 수정할 수 있습니다.");
        }
        
        User user = userService.selectById(userId);
        user.setNickname(nickname);
        user.setUserId(userId);
        
        if (profileImg != null && profileImg.isEmpty()) {
            profileImg = null;
        }
 
        boolean isUserModified = userService.modifyUser(user, profileImg);
        
        int olympicsId = playerService.findOlympicsIdByUserId(userId);
        if(olympicsId == 0 && isUserModified) {
        	return ResponseEntity.ok(user.getName() + "님의 정보를 수정했습니다.");        	
        }
        
        OlympicsSetup olympic = new OlympicsSetup(userId, olympicsName, playerNames);
        olympic.setOlympicsId(olympicsId);
        
        boolean isOlympicModified = playerService.modifyOlympics(olympic);
        
        if (isUserModified && isOlympicModified) {
        	return ResponseEntity.ok(user.getName() + "님의 정보를 수정했습니다.");     	
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 수정 실패");
    }
	
    
    /**
     * 회원 탈퇴
     * 
     * @param userId  탈퇴할 사용자의 ID
     * @param session 현재 사용자 세션
     * @return 탈퇴 결과 메시지
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "회원 탈퇴 로직 수행", description = "세션 인증 후 회원 탈퇴하는 기능입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "401", description = "인증된 사용자만 탈퇴 가능"),
        @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패")
    })
    public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId, HttpSession session) {
    	
    	if (!Validate.isValidSessionUser(session, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자만 탈퇴할 수 있습니다.");
        }

        boolean isDeleted = userService.deleteUser(userId);
        
        if (isDeleted) {
        	session.invalidate();
        	return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");        	
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
    
	
}
