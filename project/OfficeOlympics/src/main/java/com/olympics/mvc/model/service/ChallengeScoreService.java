package com.olympics.mvc.model.service;

import java.util.List;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

public interface ChallengeScoreService {
	
	List<Challenge> getChallenges();

    Challenge selectChallenge(int challengeId);

    boolean upsertScores(Score score);

    void updateTotalScore();

    List<Rank> selectChallengeScore(int challengeId, int olympicsId);

    List<Rank> selectFinalScore(int olympicsId);
    
}
