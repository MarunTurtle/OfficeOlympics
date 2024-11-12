package com.olympics.mvc.model.dto;

import java.util.List;

// challenge_scores table
public class Score {
	int scoreId;
	int challengeId;
	List<String> playerNames;
	List<Integer> playerId;
	List<Integer> scores;
	String regDate;
	
	public Score() {
	}

	public Score(int scoreId, int challengeId, List<String> playerNames, List<Integer> playerId, List<Integer> scores,
			String regDate) {
		super();
		this.scoreId = scoreId;
		this.challengeId = challengeId;
		this.playerNames = playerNames;
		this.playerId = playerId;
		this.scores = scores;
		this.regDate = regDate;
	}
	
	// getter, setter

	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public List<String> getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(List<String> playerNames) {
		this.playerNames = playerNames;
	}

	public List<Integer> getPlayerId() {
		return playerId;
	}

	public void setPlayerId(List<Integer> playerId) {
		this.playerId = playerId;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public void setScores(List<Integer> scores) {
		this.scores = scores;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}