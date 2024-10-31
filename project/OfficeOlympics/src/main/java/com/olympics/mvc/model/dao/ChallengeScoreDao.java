package com.olympics.mvc.model.dao;

import java.util.List;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreDao {
    
    // 챌린지 조회 (필요 시 ChallengeDao로 이동 가능)
    Challenge selectChallenge(int challenge_id);
    
    // 챌린지 기록 제출
    int insertScore(Score score);
    
    // 현재 챌린지에서의 순위 조회
    List<Score> selectChallengeScore(int challenge_id);
    
    // 리더보드에서 순위 조회
    List<Score> selectMainScore();
}
