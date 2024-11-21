package com.olympics.mvc.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.service.ChallengeScoreService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/")
public class MainController {
	
	private final ChallengeScoreService challengeService;
	
	public MainController(ChallengeScoreService challengeService) {
		super();
		this.challengeService = challengeService;
	}

	
	/**
	 * 메인 페이지 반환
	 *
	 * @return 전체 챌린지 및 리더보드 정보
	 */
	@GetMapping("")
	@Operation(summary = "메인페이지", description = "전체 챌린지 정보와 리더보드 정보를 반환합니다.")
	public ResponseEntity<?> mainPage(HttpSession session) {

	    Integer userId = (Integer) session.getAttribute("loginUserId");
	    List<Challenge> challengeList = challengeService.getChallenges();

	    // 유저가 로그인하지 않은 경우, 챌린지 리스트만 반환
	    if (userId == null) {
	        return challengeList.isEmpty() 
	            ? ResponseEntity.noContent().build() 
	            : ResponseEntity.ok(challengeList);
	    }

	    List<Rank> leaderBoard = challengeService.selectFinalScore(userId);

	    // 챌린지 리스트와 리더보드가 모두 비어있는 경우
	    if ((challengeList == null || challengeList.isEmpty()) &&
	        (leaderBoard == null || leaderBoard.isEmpty())) {
	        return ResponseEntity.noContent().build();
	    }

	    // 결과 데이터를 담을 Map 초기화
	    Map<String, Object> response = new HashMap<>();
	    response.put("challengeList", challengeList != null ? challengeList : Collections.emptyList());
	    response.put("leaderBoard", leaderBoard != null ? leaderBoard : Collections.emptyList());

	    return ResponseEntity.ok(response);
	}


}
