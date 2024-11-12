package com.olympics.mvc.model.dto;

// challenge table
public class Challenge {
	int challengeId;
	String challengeName;
	String challengeDesc;
	String challengeUrl;
	String regDate;
	
	public Challenge() {};
	
	// 챌린지 정보 생성자
	
	public Challenge(int challengeId, String challengeName, String challengeDesc, String challengeUrl, String regDate) {
		super();
		this.challengeId = challengeId;
		this.challengeName = challengeName;
		this.challengeDesc = challengeDesc;
		this.challengeUrl = challengeUrl;
		this.regDate = regDate;
	}

	// Getters, Setters
		
	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getChallengeDesc() {
		return challengeDesc;
	}

	public void setChallengeDesc(String challengeDesc) {
		this.challengeDesc = challengeDesc;
	}

	public String getChallengeUrl() {
		return challengeUrl;
	}

	public void setChallengeUrl(String challengeUrl) {
		this.challengeUrl = challengeUrl;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

}