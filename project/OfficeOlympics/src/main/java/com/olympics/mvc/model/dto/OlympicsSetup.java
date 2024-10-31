package com.olympics.mvc.model.dto;

import java.util.List;

// Olympics 팀 Setting
public class OlympicsSetup {
	
    private int userId; // 올림픽 팀을 생성한 사용자 ID
    private String olympicsName; // 올림픽 팀 이름
    private List<String> playerNames; // 플레이어 이름 목록 (최대 10명 등 제한 가능)
	
    // Getters and Setters
    public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOlympicsName() {
		return olympicsName;
	}
	public void setOlympicsName(String olympicsName) {
		this.olympicsName = olympicsName;
	}
	public List<String> getPlayerNames() {
		return playerNames;
	}
	public void setPlayerNames(List<String> playerNames) {
		this.playerNames = playerNames;
	}

}

