package com.olympics.mvc.model.dto;

// challenge table
public class Challenge {
	int challenge_id;
	String challenge_name;
	String challenge_desc;
	String reg_date;
	
	// 챌린지 정보 생성자
	public Challenge(int challenge_id, String challenge_name, String challenge_desc, String reg_date) {
		super();
		this.challenge_id = challenge_id;
		this.challenge_name = challenge_name;
		this.challenge_desc = challenge_desc;
		this.reg_date = reg_date;
	}

	// Getters, Setters
	public int getChallenge_id() {
		return challenge_id;
	}

	public void setChallenge_id(int challenge_id) {
		this.challenge_id = challenge_id;
	}

	public String getChallenge_name() {
		return challenge_name;
	}

	public void setChallenge_name(String challenge_name) {
		this.challenge_name = challenge_name;
	}

	public String getChallenge_desc() {
		return challenge_desc;
	}

	public void setChallenge_desc(String challenge_desc) {
		this.challenge_desc = challenge_desc;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	
}
