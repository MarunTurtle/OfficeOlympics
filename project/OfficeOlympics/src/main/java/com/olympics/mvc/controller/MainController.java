package com.olympics.mvc.controller;

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
		
	    List<Rank> leaderBoard = challengeService.selectFinalScore(userId);

	    List<Challenge> challengeList = challengeService.getChallenges();

	    if ((leaderBoard == null || leaderBoard.isEmpty()) && (challengeList == null || challengeList.isEmpty())) {
	        return ResponseEntity.noContent().build();
	    } 

	    if (leaderBoard != null && !leaderBoard.isEmpty() && challengeList != null && !challengeList.isEmpty()) {
	        Map<String, Object> boardList = new HashMap<>();
	        boardList.put("leaderBoard", leaderBoard);
	        boardList.put("challengeList", challengeList);
	        return ResponseEntity.ok(boardList);
	    }
	    
	    return ResponseEntity.accepted().body(leaderBoard != null && !leaderBoard.isEmpty() ? leaderBoard : challengeList);
	}

}
