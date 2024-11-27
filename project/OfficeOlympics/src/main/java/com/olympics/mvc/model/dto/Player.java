package com.olympics.mvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player DTO")
public class Player {
	private int olympicsId; // 참여한 올림픽 id
	private int playerId; // 플레이어 id 
	private String playerName; // 플레이어 이름
	private int totalScore; // 누적 점수
	private String regDate; // 등록 날짜
	
	public Player() {
		
	}
	
	// 전체 생성자
	public Player(int olympicsId, int playerId, String playerName, int totalScore, String regDate) {
		super();
		this.olympicsId = olympicsId;
		this.playerId = playerId;
		this.playerName = playerName;
		this.totalScore = totalScore;
		this.regDate = regDate;
	}

	// Getters, Setters
	
	
	public int getOlympicsId() {
		return olympicsId;
	}

	public void setOlympicsId(int olympicsId) {
		this.olympicsId = olympicsId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
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

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
}
