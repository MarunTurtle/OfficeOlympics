package com.olympics.mvc.model.dto;

// players, olympics table
public class Player {
	int olympicsId; // 참여한 올림픽 id
	int playerId; // 플레이어 id 
	String playerName; // 플레이어 이름
	int totalScore; // 누적 점수
	String regDate; // 등록 날짜
	
	public Player() {
		
	}
	
	// 전체 생성자
	public Player(int olympicsId, int playerId, String playerName,
			int totalScore, String regDate) {
		super();
		this.olympicsId = olympicsId;
		this.playerId = playerId;
		this.playerName = playerName;
		this.totalScore = totalScore;
		this.regDate = regDate;
	}
	
	// Getters, Setters

	public int getOlympics_id() {
		return olympicsId;
	}

	public void setOlympics_id(int olympicsId) {
		this.olympicsId = olympicsId;
	}

	public int getPlayer_id() {
		return playerId;
	}

	public void setPlayer_id(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayer_name() {
		return playerName;
	}

	public void setPlayer_name(String playerName) {
		this.playerName = playerName;
	}

	public int getTotal_score() {
		return totalScore;
	}

	public void setTotal_score(int totalScore) {
		this.totalScore = totalScore;
	}

	public String getReg_date() {
		return regDate;
	}

	public void setReg_date(String regDate) {
		this.regDate = regDate;
	}
	
}
