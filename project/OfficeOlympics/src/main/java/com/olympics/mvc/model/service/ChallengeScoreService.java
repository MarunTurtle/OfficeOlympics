package com.olympics.mvc.model.service;

import java.util.List;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreService {
	
	// 전체 챌린지 조회
	List<Challenge> getChallenges();
	
	// 챌린지 조회
    Challenge selectChallenge(int challengeId);
    
    // 챌린지 결과 기록
    boolean upsertScores(Score score);
    
    // 챌린지 총 점수 업데이트
    void updateTotalScore();
    
    // 현재 챌린지에서의 순위 조회
    List<Rank> selectChallengeScore(int challengeId, int olympicsId);
    
    // 올림픽 팀 내 순위 조회
    List<Rank> selectFinalScore(int olympicsId);
    
    // 리더보드에서 전체 순위 조회
    List<Rank> selectMainScore();
}
