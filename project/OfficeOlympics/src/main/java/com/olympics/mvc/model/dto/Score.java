package com.olympics.mvc.model.dto;

import java.util.List;

// challenge_scores table
public class Score {
	private int scoreId; // 점수 PK
	private int challengeId; // 챌린지 id
	private List<String> playerNames; // 플레이어 이름
	private List<Integer> playerId; // 플레이어 ID
	private List<Integer> scores; // 획득점수
	private String regDate;
	
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