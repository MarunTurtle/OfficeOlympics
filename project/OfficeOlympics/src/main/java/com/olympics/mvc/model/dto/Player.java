package com.olympics.mvc.model.dto;

// players, olympics table
public class Player {
	int olympicsId; // 참여한 올림픽 id
	int userId; // 사용자 id
	String olympicsName; // 올림픽 팀 이름
	int playerId; // 플레이어 id 
	String playerName; // 플레이어 이름
	int totalScore; // 누적 점수
	String regDate; // 등록 날짜
	
	public Player() {
		
	}
	
	// 전체 생성자
	public Player(int olympicsId, int userId, String olympicsName, int playerId, String playerName,
			int totalScore, String regDate) {
		super();
		this.olympicsId = olympicsId;
		this.userId = userId;
		this.olympicsName = olympicsName;
		this.playerId = playerId;
		this.playerName = playerName;
		this.totalScore = totalScore;
		this.regDate = regDate;
	}

	// 올림픽 관련 정보 포함 생성자
	public Player(int olympicsId, int userId, String olympicsName) {
		super();
		this.olympicsId = olympicsId;
		this.userId = userId;
		this.olympicsName = olympicsName;
	}

	// players 생성자
	public Player(int playerId, int olympicsId, String playerName, int totalScore, String regDate) {
		this.playerId = playerId;
		this.olympicsId = olympicsId;
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

	public int getUser_id() {
		return userId;
	}

	public void setUser_id(int userId) {
		this.userId = userId;
	}

	public String getOlympics_name() {
		return olympicsName;
	}

	public void setOlympics_name(String olympics_name) {
		this.olympicsName = olympics_name;
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
