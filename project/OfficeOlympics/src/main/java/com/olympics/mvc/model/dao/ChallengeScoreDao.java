package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreDao {
    
    // 챌린지 조회 (필요 시 ChallengeDao로 이동 가능)
    Challenge selectChallenge(int challengeId);
    
    // 이름->id로 전환
    List<Integer> nameToId(List<String> playerNames);
    
    // 챌린지 기록 제출
    int insertScore(List<Map<String, Object>> score);
    
    // 챌린지 점수 업데이트
    int updateScore();
    
    // 현재 챌린지에서의 순위 조회
    List<Rank> selectChallengeScore(int challengeId);
    
    // 올림픽 팀 내 순위 조회
    List<Rank> selectFinalScore(int olympicsId);
    
    // 리더보드에서 순위 조회
    List<Rank> selectMainScore();
}
