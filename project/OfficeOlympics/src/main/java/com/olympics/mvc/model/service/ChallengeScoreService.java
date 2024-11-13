package com.olympics.mvc.model.service;

import java.util.List;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreService {
	
	// 챌린지 조회
    Challenge selectChallenge(int challengeId);
    
    // 챌린지 결과 기록
    boolean upsertScores(Score score);
    
    // 챌린지 점수 업데이트
    void updateTotalScore();
    
    // 현재 챌린지에서의 순위 조회
    List<Rank> selectChallengeScore(int challengeId);
    
    // 올림픽 팀 내 순위 조회
    List<Rank> selectFinalScore(int olympicsId);
    
    // 리더보드에서 순위 조회
    List<Rank> selectMainScore();
}
