package com.olympics.mvc.model.dto;

// challenge_scores table
public class Score {
	int score_id;
	int challenge_id;
	int player_id;
	int score;
	String reg_date;
	String player_name;
	int total_score;
	int rank;
	
	// 챌린지 획득 점수 저장 생성자
	public Score(int score_id, int challenge_id, int player_id, int score, String reg_date) {
		super();
		this.score_id = score_id;
		this.challenge_id = challenge_id;
		this.player_id = player_id;
		this.score = score;
		this.reg_date = reg_date;
	}
	
	// 개별 챌린지 리더보드 생성자
	public Score(String player_name, int score) {
		this.player_name = player_name;
		this.score = score;
	}
	
	// 전체 리더보드용 생성자
	public Score(int rank, String player_name, int total_score) {
		super();
		this.rank = rank;
		this.player_name = player_name;
		this.total_score = total_score;
	}
	
	// Getters, Setters
	public int getScore_id() {
		return score_id;
	}

	public void setScore_id(int score_id) {
		this.score_id = score_id;
	}

	public int getChallenge_id() {
		return challenge_id;
	}

	public void setChallenge_id(int challenge_id) {
		this.challenge_id = challenge_id;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
	
	
	
}
