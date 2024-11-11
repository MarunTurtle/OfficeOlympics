package com.olympics.mvc.model.dto;

// challenge_scores table
public class Score {
	int scoreId;
	int challengeId;
	int playerId;
	int score;
	String regDate;
	String playerName;
	int totalScore;
	int rank;
	
	// 챌린지 획득 점수 저장 생성자
	public Score(int scoreId, int challengeId, int playerId, int score, String regDate) {
		super();
		this.scoreId = scoreId;
		this.challengeId = challengeId;
		this.playerId = playerId;
		this.score = score;
		this.regDate = regDate;
	}
	
	// 개별 챌린지 리더보드 생성자
	public Score(String playerName, int score) {
		this.playerName = playerName;
		this.score = score;
	}
	
	// 전체 리더보드용 생성자
	public Score(int rank, String playerName, int totalScore) {
		super();
		this.rank = rank;
		this.playerName = playerName;
		this.totalScore = totalScore;
	}

	// Getters, Setters
	
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

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
