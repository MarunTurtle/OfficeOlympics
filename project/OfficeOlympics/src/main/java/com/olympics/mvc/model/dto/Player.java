package com.olympics.mvc.model.dto;

// players, olympics table
public class Player {
	int olympics_id; // 참여한 올림픽 id
	int user_id; // 사용자 id
	String olympics_name; // 올림픽 팀 이름
	int player_id; // 플레이어 id 
	String player_name; // 플레이어 이름
	int total_score; // 누적 점수
	String reg_date; // 등록 날짜

	// 올림픽 관련 정보 포함 생성자
	public Player(int olympics_id, int user_id, String olympics_name) {
		super();
		this.olympics_id = olympics_id;
		this.user_id = user_id;
		this.olympics_name = olympics_name;
	}

	// players 생성자
	public Player(int player_id, int olympics_id, String player_name, int total_score, String reg_date) {
		this.player_id = player_id;
		this.olympics_id = olympics_id;
		this.player_name = player_name;
		this.total_score = total_score;
		this.reg_date = reg_date;
	}
	
	// Getters, Setters
	public int getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	public int getOlympics_id() {
		return olympics_id;
	}

	public void setOlympics_id(int olympics_id) {
		this.olympics_id = olympics_id;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public int getTotal_score() {
		return total_score;
	}

	public void setTotal_score(int total_score) {
		this.total_score = total_score;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@Override
	public String toString() {
		return "PlayerDto [player_id=" + player_id + ", olympics_id=" + olympics_id + ", player_name=" + player_name
				+ ", total_score=" + total_score + ", reg_date=" + reg_date + "]";
	}
		
	
}
