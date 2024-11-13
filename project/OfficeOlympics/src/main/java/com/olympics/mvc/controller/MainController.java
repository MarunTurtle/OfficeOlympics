package com.olympics.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dao.ChallengeScoreDao;
import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;


@RestController
@RequestMapping("/")
public class MainController {
	
	private final ChallengeScoreDao challengeDao;
	
	public MainController(ChallengeScoreDao challengeDao) {
		super();
		this.challengeDao = challengeDao;
	}

	@GetMapping("")
	public ResponseEntity<?> mainPage() {
	    List<Rank> leaderBoard = challengeDao.selectMainScore();
	    List<Challenge> challengeList = challengeDao.getChallenges();

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
