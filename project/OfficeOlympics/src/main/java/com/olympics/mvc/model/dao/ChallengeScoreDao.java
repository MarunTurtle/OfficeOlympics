package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreDao {
	
	// 전체 챌린지 조회
	List<Challenge> getChallenges();
    
    // 챌린지 조회
    Challenge selectChallenge(int challengeId);
    
    // 이름->id로 추출
    List<Integer> nameToId(List<String> playerNames);
    
    // score_id 존재 여부 확인
    int findScoreId(List<Map<String, Object>> score);
    
    // 챌린지 결과 기록 (삽입)
    int insertScore(List<Map<String, Object>> score);
    
    // 챌린지 결과 기록 (누적)
    int updateScore(List<Map<String, Object>> score);
    
    // 챌린지 총 점수 업데이트
    int updateTotalScore();
    
    // 현재 챌린지에서의 순위 조회
    List<Rank> selectChallengeScore(int challengeId);
    
    // 올림픽 팀 내 순위 조회
    List<Rank> selectFinalScore(int olympicsId);
    
    // 리더보드에서 전체 순위 조회
    List<Rank> selectMainScore();
}
